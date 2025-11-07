package br.com.biblioteca.service;

import br.com.biblioteca.dto.livro.LivroDTO;
import br.com.biblioteca.exception.ResourceNotFoundException;
import br.com.biblioteca.model.Autor;
import br.com.biblioteca.model.Critica;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.AutorRepository;
import br.com.biblioteca.repository.LivroRepository;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LivroService {
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    public LivroService(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public LivroDTO toDTO(Livro l) {
        LivroDTO dto = new LivroDTO();
        dto.setId(l.getId());
        dto.setTitulo(l.getTitulo());
        dto.setIsbn(l.getIsbn());
        dto.setEditora(l.getEditora());
        dto.setDataPublicacao(l.getDataPublicacao());
        dto.setAutorIds(l.getAutores().stream().map(Autor::getId).collect(Collectors.toList()));
        // calcular média de nota
        List<Critica> criticas = l.getCriticas();
        if (criticas != null && !criticas.isEmpty()) {
            double avg = criticas.stream().mapToInt(Critica::getNota).average().orElse(0.0);
            dto.setMediaNota(Math.round(avg * 100.0) / 100.0);
        } else {
            dto.setMediaNota(null);
        }
        return dto;
    }

    public Livro fromCreateDTO(LivroDTO dto) {
        Livro l = new Livro();
        l.setTitulo(dto.getTitulo());
        l.setIsbn(dto.getIsbn());
        l.setEditora(dto.getEditora());
        l.setDataPublicacao(dto.getDataPublicacao());
        if (dto.getAutorIds() != null) {
            Set<Autor> autores = dto.getAutorIds().stream()
                    .map(id -> autorRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado: " + id)))
                    .collect(Collectors.toSet());
            l.setAutores(autores);
        }
        return l;
    }

    public Livro fromUpdateDTO(Livro existing, LivroDTO dto) {
        existing.setTitulo(dto.getTitulo());
        existing.setIsbn(dto.getIsbn());
        existing.setEditora(dto.getEditora());
        existing.setDataPublicacao(dto.getDataPublicacao());
        if (dto.getAutorIds() != null) {
            Set<Autor> autores = dto.getAutorIds().stream()
                    .map(id -> autorRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado: " + id)))
                    .collect(Collectors.toSet());
            existing.setAutores(autores);
        } else {
            existing.getAutores().clear();
        }
        return existing;
    }

    public LivroDTO create(LivroDTO dto) {
        Livro l = fromCreateDTO(dto);
        Livro saved = livroRepository.save(l);
        return toDTO(saved);
    }

    public LivroDTO getOne(Long id) {
        Livro l = livroRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));
        return toDTO(l);
    }

    public void delete(Long id) {
        Livro l = livroRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));
        livroRepository.delete(l);
    }

    public LivroDTO update(Long id, LivroDTO dto) {
        Livro existing = livroRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));
        Livro updated = fromUpdateDTO(existing, dto);
        return toDTO(livroRepository.save(updated));
    }

    // LIST com paginação, ordenação e filtros: title, authorName, minNota, maxNota
    public Page<LivroDTO> list(String titulo, String autorNome, Integer minNota, Integer maxNota, Pageable pageable) {
        Specification<Livro> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();

            if (titulo != null && !titulo.isBlank()) {
                preds.add(cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"));
            }

            if (autorNome != null && !autorNome.isBlank()) {
                // join com autores
                Join<Livro, Autor> joinAutor = root.join("autores", JoinType.LEFT);
                preds.add(cb.like(cb.lower(joinAutor.get("nome")), "%" + autorNome.toLowerCase() + "%"));
                query.distinct(true);
            }

            // filtros por nota média: precisamos subquery para calcular média
            if (minNota != null || maxNota != null) {
                Subquery<Double> sub = query.subquery(Double.class);
                Root<Livro> l2 = sub.from(Livro.class);
                Join<Object, Object> c = l2.join("criticas", JoinType.LEFT);
                sub.select(cb.avg(c.get("nota")));
                sub.where(cb.equal(l2.get("id"), root.get("id")));
                // Compare subquery value
                if (minNota != null) {
                    preds.add(cb.greaterThanOrEqualTo(sub, minNota.doubleValue()));
                }
                if (maxNota != null) {
                    preds.add(cb.lessThanOrEqualTo(sub, maxNota.doubleValue()));
                }
            }

            return cb.and(preds.toArray(new Predicate[0]));
        };

        Page<Livro> page = livroRepository.findAll(spec, pageable);
        return page.map(this::toDTO);
    }
}
