package br.com.biblioteca.service;

import br.com.biblioteca.dto.autor.AutorDTO;
import br.com.biblioteca.exception.ResourceNotFoundException;
import br.com.biblioteca.model.Autor;
import br.com.biblioteca.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public AutorDTO toDTO(Autor a) {
        AutorDTO dto = new AutorDTO();
        dto.setId(a.getId());
        dto.setNome(a.getNome());
        dto.setBiografia(a.getBiografia());
        return dto;
    }

    public Autor fromDTO(AutorDTO dto) {
        Autor a = new Autor();
        a.setId(dto.getId());
        a.setNome(dto.getNome());
        a.setBiografia(dto.getBiografia());
        return a;
    }

    public List<AutorDTO> getAll() {
        return autorRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AutorDTO getOne(Long id) {
        var a = autorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado: " + id));
        return toDTO(a);
    }

    public AutorDTO create(AutorDTO dto) {
        Autor autor = fromDTO(dto);
        Autor saved = autorRepository.save(autor);
        return toDTO(saved);
    }

    public AutorDTO update(Long id, AutorDTO dto) {
        var existing = autorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado: " + id));
        existing.setNome(dto.getNome());
        existing.setBiografia(dto.getBiografia());
        return toDTO(autorRepository.save(existing));
    }

    public void delete(Long id) {
        var existing = autorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado: " + id));
        autorRepository.delete(existing);
    }
}
