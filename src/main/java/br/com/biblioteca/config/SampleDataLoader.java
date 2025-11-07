package br.com.biblioteca.config;

import br.com.biblioteca.model.Autor;
import br.com.biblioteca.model.Critica;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.AutorRepository;
import br.com.biblioteca.repository.CriticaRepository;
import br.com.biblioteca.repository.LivroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class SampleDataLoader implements CommandLineRunner {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;
    private final CriticaRepository criticaRepository;

    public SampleDataLoader(AutorRepository autorRepository, LivroRepository livroRepository, CriticaRepository criticaRepository) {
        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
        this.criticaRepository = criticaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (autorRepository.count() > 0) return; // já carregado

        Autor a1 = new Autor();
        a1.setNome("J. K. Rowling");
        a1.setBiografia("Autora britânica, conhecida pela série Harry Potter.");
        autorRepository.save(a1);

        Autor a2 = new Autor();
        a2.setNome("George R. R. Martin");
        a2.setBiografia("Autor da série 'A Song of Ice and Fire'.");
        autorRepository.save(a2);

        Livro l1 = new Livro();
        l1.setTitulo("Harry Potter e a Pedra Filosofal");
        l1.setIsbn("9780747532699");
        l1.setEditora("Bloomsbury");
        l1.setDataPublicacao(LocalDate.of(1997, 6, 26));
        l1.setAutores(Set.of(a1));
        livroRepository.save(l1);

        Livro l2 = new Livro();
        l2.setTitulo("A Guerra dos Tronos");
        l2.setIsbn("9780553103540");
        l2.setEditora("Bantam Spectra");
        l2.setDataPublicacao(LocalDate.of(1996, 8, 6));
        l2.setAutores(Set.of(a2));
        livroRepository.save(l2);

        Critica c1 = new Critica();
        c1.setConteudo("Clássico infantil — maravilhoso para começar a ler.");
        c1.setNota(5);
        c1.setUsuario("maria");
        c1.setLivro(l1);
        criticaRepository.save(c1);

        Critica c2 = new Critica();
        c2.setConteudo("Enredo complexo, personagens memoráveis.");
        c2.setNota(4);
        c2.setUsuario("joao");
        c2.setLivro(l2);
        criticaRepository.save(c2);
    }
}
