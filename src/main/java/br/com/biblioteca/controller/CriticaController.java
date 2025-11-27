package br.com.biblioteca.controller;

import br.com.biblioteca.dto.critica.CriticaDTO;
import br.com.biblioteca.service.CriticaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/criticas")
public class CriticaController {
    private final CriticaService service;

    public CriticaController(CriticaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CriticaDTO> create(@Valid @RequestBody CriticaDTO dto) {
        CriticaDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/criticas/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CriticaDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CriticaDTO> update(@PathVariable Long id, @Valid @RequestBody CriticaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/livro/{livroId}")
    public ResponseEntity<List<CriticaDTO>> listByLivro(@PathVariable Long livroId) {
        return ResponseEntity.ok(service.listByLivro(livroId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
