package com.chall.Livros.livros;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByTitulo(String titulo);

    List<Livro> findByIdioma(String idioma);
}
