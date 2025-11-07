package br.com.biblioteca.repository;

import br.com.biblioteca.model.Critica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CriticaRepository extends JpaRepository<Critica, Long> {
    List<Critica> findByLivroId(Long livroId);
}
