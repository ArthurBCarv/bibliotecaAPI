package br.com.biblioteca.controller;

import br.com.biblioteca.dto.livro.LivroDTO;
import br.com.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService service;
    private final int defaultPageSize;

    public LivroController(LivroService service, @Value("${app.default-page-size:10}") int defaultPageSize) {
        this.service = service;
        this.defaultPageSize = defaultPageSize;
    }

    @PostMapping
    public ResponseEntity<LivroDTO> create(@Valid @RequestBody LivroDTO dto) {
        var created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/livros/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroDTO> update(@PathVariable Long id, @Valid @RequestBody LivroDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/livros
     * Query params:
     *  - titulo
     *  - autorNome
     *  - minNota
     *  - maxNota
     *  - page (0-based)
     *  - size
     *  - sort (ex: titulo,asc or dataPublicacao,desc) multiple allowed
     */
    @GetMapping
    public ResponseEntity<Page<LivroDTO>> list(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autorNome,
            @RequestParam(required = false) Integer minNota,
            @RequestParam(required = false) Integer maxNota,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false, defaultValue = "id,asc") String[] sort
    ) {
        int pageSize = (size == null) ? defaultPageSize : size;
        Sort sorting = Sort.by(parseSort(sort));
        Pageable pageable = PageRequest.of(page, pageSize, sorting);
        Page<LivroDTO> result = service.list(titulo, autorNome, minNota, maxNota, pageable);
        return ResponseEntity.ok(result);
    }

    private Sort.Order[] parseSort(String[] sortParams) {
        // each element like "titulo,asc"
        return java.util.Arrays.stream(sortParams).map(s -> {
            String[] parts = s.split(",");
            String prop = parts[0];
            Sort.Direction dir = parts.length > 1 && parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            return new Sort.Order(dir, prop);
        }).toArray(Sort.Order[]::new);
    }
}
