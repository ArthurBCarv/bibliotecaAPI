package br.com.biblioteca.service;

import br.com.biblioteca.dto.critica.CriticaDTO;
import br.com.biblioteca.exception.ResourceNotFoundException;
import br.com.biblioteca.model.Critica;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.CriticaRepository;
import br.com.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CriticaService {
    private final CriticaRepository criticaRepository;
    private final LivroRepository livroRepository;

    public CriticaService(CriticaRepository criticaRepository, LivroRepository livroRepository) {
        this.criticaRepository = criticaRepository;
        this.livroRepository = livroRepository;
    }

    public CriticaDTO toDTO(Critica c) {
        CriticaDTO dto = new CriticaDTO();
        dto.setId(c.getId());
        dto.setConteudo(c.getConteudo());
        dto.setNota(c.getNota());
        dto.setUsuario(c.getUsuario());
        dto.setDataCriacao(c.getDataCriacao());
        dto.setLivroId(c.getLivro() != null ? c.getLivro().getId() : null);
        return dto;
    }

    public CriticaDTO create(CriticaDTO dto) {
        Livro l = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + dto.getLivroId()));

        Critica c = new Critica();
        c.setConteudo(dto.getConteudo());
        c.setNota(dto.getNota());
        c.setUsuario(dto.getUsuario());
        c.setLivro(l);

        Critica saved = criticaRepository.save(c);
        return toDTO(saved);
    }

    public void delete(Long id) {
        Critica c = criticaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Crítica não encontrada: " + id));
        criticaRepository.delete(c);
    }

    public List<CriticaDTO> listByLivro(Long livroId) {
        if (!livroRepository.existsById(livroId)) {
            throw new ResourceNotFoundException("Livro não encontrado: " + livroId);
        }
        return criticaRepository.findByLivroId(livroId).stream().map(this::toDTO).collect(Collectors.toList());
    }
}
