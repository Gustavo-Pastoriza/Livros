package com.chall.Livros.autor;

import com.chall.Livros.livros.Livro;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("name")
    private String nome;

    @JsonAlias("birth_year")
    private Integer nascimento;

    @JsonAlias("death_year")
    private Integer morte;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Livro> livros;

    public List<Livro> getLivros() {
        return livros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNascimento() {
        return nascimento;
    }

    public void setNascimento(Integer nascimento) {
        this.nascimento = nascimento;
    }

    public Integer getMorte() {
        return morte;
    }

    public void setMorte(Integer morte) {
        this.morte = morte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return Objects.equals(nome, autor.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}


