package br.com.biblioteca.controller;

import br.com.biblioteca.dto.autor.AutorDTO;
import br.com.biblioteca.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {
    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOne(id));
    }

    @PostMapping
    public ResponseEntity<AutorDTO> create(@Valid @RequestBody AutorDTO dto) {
        AutorDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/autores/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorDTO> update(@PathVariable Long id, @Valid @RequestBody AutorDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
