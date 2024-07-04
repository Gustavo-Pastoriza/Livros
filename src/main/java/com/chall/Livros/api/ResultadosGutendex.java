package com.chall.Livros.api;

import com.chall.Livros.livros.DadosLivro;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultadosGutendex {
    private int count;
    private List<DadosLivro> results;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DadosLivro> getResults() {
        return results;
    }

    public void setResults(List<DadosLivro> results) {
        this.results = results;
    }
}