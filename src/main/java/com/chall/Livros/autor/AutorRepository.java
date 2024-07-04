package com.chall.Livros.autor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.nascimento <= :ano AND (a.morte IS NULL OR a.morte >= :ano)")
    List<Autor> listarAutoresVivosNoAno(@Param("ano") int ano);
}
