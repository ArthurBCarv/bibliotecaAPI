package br.com.biblioteca.dto.livro;

import java.time.LocalDate;
import java.util.List;

public class LivroDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private String editora;
    private LocalDate dataPublicacao;
    private List<Long> autorIds;
    private Double mediaNota; // computed

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }
    public LocalDate getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDate dataPublicacao) { this.dataPublicacao = dataPublicacao; }
    public List<Long> getAutorIds() { return autorIds; }
    public void setAutorIds(List<Long> autorIds) { this.autorIds = autorIds; }
    public Double getMediaNota() { return mediaNota; }
    public void setMediaNota(Double mediaNota) { this.mediaNota = mediaNota; }
}
