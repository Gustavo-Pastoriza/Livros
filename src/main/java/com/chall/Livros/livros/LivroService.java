package com.chall.Livros.livros;

import com.chall.Livros.api.ConsumoApi;
import com.chall.Livros.api.ResultadosGutendex;
import com.chall.Livros.autor.Autor;
import com.chall.Livros.autor.AutorRepository;
import com.chall.Livros.autor.AutorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorService autorService;

    private ObjectMapper mapper = new ObjectMapper();
    private ConsumoApi consumo = new ConsumoApi();
    private final String ENDERECO = "https://gutendex.com/books/?search=";

    Scanner leitura = new Scanner(System.in);

    public void buscarLivroWeb() {
        System.out.println("Digite o nome do livro:");
        var livro = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + livro.replace(" ", "%20"));

        ResultadosGutendex dados = obterDados(json, ResultadosGutendex.class);

        if (dados.getCount() > 0) {
            DadosLivro primeiroLivro = dados.getResults().get(0);
            exibirLivroFormatado(primeiroLivro);
            salvaLivro(primeiroLivro);
        } else {
            System.out.println("Nenhum livro encontrado.");
        }
    }

    private <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void exibirLivroFormatado(DadosLivro livro) {


        if (livro != null) {
            System.out.println("----------LIVRO----------");
            System.out.println("Nome: " + livro.getTitulo());
            if (!livro.getAutores().isEmpty()) {
                System.out.println("Autor: " + livro.getAutores().get(0).getNome());
            }
            if (!livro.getIdioma().isEmpty()) {
                System.out.println("Idioma: " + livro.getIdioma().get(0));
            }
            System.out.println("Número de Downloads: " + livro.getNumeroDownloads());
            System.out.println("-------------------------");
        } else {
            System.out.println("Nenhum livro encontrado.");
        }
    }

    private void salvaLivro(DadosLivro dadosLivro) {
        if (livroRepository.existsByTitulo(dadosLivro.getTitulo())) {
            return;
        }
        Livro livro = new Livro();
        livro.setTitulo(dadosLivro.getTitulo());
        livro.setNumeroDownloads(dadosLivro.getNumeroDownloads());
        livro.setIdioma(Collections.singletonList(dadosLivro.getIdioma().stream().findFirst().orElse(null)));
        livro.setAutores(dadosLivro.getAutores().stream()
                .map(autor -> {
                    Autor autorEntity = new Autor();
                    autorEntity.setNome(autor.getNome());
                    autorEntity.setNascimento(autor.getNascimento());
                    autorEntity.setMorte(autor.getMorte());
                    return autorEntity;
                }).collect(Collectors.toList()));

        livroRepository.save(livro);
    }


    public void listarLivrosCadastrados() {
        List<Livro> livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }


        List<DadosLivro> dadosLivros = mapearLivrosParaDadosLivros(livros);

        for (DadosLivro dadosLivro : dadosLivros) {
            exibirLivroFormatado(dadosLivro);
        }
    }

    private List<DadosLivro> mapearLivrosParaDadosLivros(List<Livro> livros) {
        return livros.stream()
                .map(livro -> {
                    DadosLivro dadosLivro = new DadosLivro();
                    dadosLivro.setTitulo(livro.getTitulo());
                    dadosLivro.setAutores(livro.getAutores());
                    dadosLivro.setNumeroDownloads(livro.getNumeroDownloads());
                    dadosLivro.setIdioma(livro.getIdioma());


                    return dadosLivro;
                })
                .collect(Collectors.toList());
    }

    public void listarAutoresCadastrados() {
        List<Autor> autores = autorService.listarAutoresCadastrados();

        Map<Autor, List<Livro>> autoresComObras = autores.stream()
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.flatMapping(autor -> autor.getLivros().stream(), Collectors.toList())));

        if (autoresComObras.isEmpty()) {
            System.out.println("Nenhum autor cadastrado.");
            return;
        }

        autoresComObras.forEach((autor, livros) -> {
            exibirAutorFormatado(autor, livros);
        });
    }

    public void autoresVivosPorAno() {
        System.out.println("Digite o ano para listar autores vivos:");
        int ano = leitura.nextInt();
        leitura.nextLine();

        List<Autor> autoresVivos = listarAutoresVivosNoAno(ano);

        if (autoresVivos.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo no ano " + ano);
            return;
        }

        Map<Autor, List<Livro>> autoresComObras = autoresVivos.stream()
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.flatMapping(autor -> autor.getLivros().stream(), Collectors.toList())));


        autoresComObras.forEach((autor, livros) -> {
            exibirAutorFormatado(autor, livros);
        });
    }

    private List<Autor> listarAutoresVivosNoAno(int ano) {
        return autorRepository.listarAutoresVivosNoAno(ano);
    }

    public void livrosPorIdioma() {
        String idioma = selecionarIdioma();

        List<Livro> livros = listarLivrosPorIdioma(idioma);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado com esse idioma.");
            return;
        }

        List<DadosLivro> dadosLivros = mapearLivrosParaDadosLivros(livros);
        for (DadosLivro dadosLivro : dadosLivros) {
            exibirLivroFormatado(dadosLivro);
        }
    }

    private List<Livro> listarLivrosPorIdioma(String idioma) {
        return livroRepository.findByIdioma(idioma);
    }

    private String selecionarIdioma() {
        var opcao = -1;
        while (opcao < 1 || opcao > 4) {
            System.out.println("Selecione o idioma:");
            System.out.println("1 - Inglês (en)");
            System.out.println("2 - Português (pt)");
            System.out.println("3 - Francês (fr)");
            System.out.println("4 - Espanhol (es)");
            opcao = leitura.nextInt();
            leitura.nextLine();

            if (opcao < 1 || opcao > 4) {
                System.out.println("Opção inválida. Por favor, escolha um número entre 1 e 4.");
            }
        }

        String idioma = "";
        switch (opcao) {
            case 1:
                idioma = "[en]";
                break;
            case 2:
                idioma = "[pt]";
                break;
            case 3:
                idioma = "[fr]";
                break;
            case 4:
                idioma = "[es]";
                break;
        }
        return idioma;
    }




    private void exibirAutorFormatado(Autor autor, List<Livro> livros) {
        System.out.println("----------AUTOR----------");
        System.out.println("Nome: " + autor.getNome());
        System.out.println("Nascimento: " + autor.getNascimento());
        System.out.println("Morte: " + autor.getMorte());
        System.out.println("Obras:");
        livros.forEach(livro -> System.out.println("- " + livro.getTitulo()));
        System.out.println("------------------------");
    }
    }

