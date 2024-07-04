package com.chall.Livros.autor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarAutoresComObras() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresCadastrados() {
        return autorRepository.findAll();
    }


}