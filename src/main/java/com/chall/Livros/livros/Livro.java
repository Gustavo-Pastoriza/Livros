package com.chall.Livros.livros;

import com.chall.Livros.autor.Autor;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.List;


@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer numeroDownloads;
    private String idioma;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(Integer numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }


    public List<String> getIdioma() {
        return Collections.singletonList(idioma);
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = String.valueOf(idioma);
    }
}