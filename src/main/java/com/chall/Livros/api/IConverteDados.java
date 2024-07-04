package com.chall.Livros.api;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
