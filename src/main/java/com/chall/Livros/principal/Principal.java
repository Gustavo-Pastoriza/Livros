package com.chall.Livros.principal;

import com.chall.Livros.livros.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Principal {

    private Scanner leitura = new Scanner(System.in);

    @Autowired
    private LivroService livroService;


    public Principal() {}


        public void exibeMenu () {

                if (livroService == null) {
                    System.out.println("livroService não foi inicializado corretamente.");
                }
                var opcao = -1;
                while (opcao != 0) {

                    var menu = """
                            ------------------------
                            1 - Buscar livro pelo titulo
                            2 - Listar livros cadastrados
                            3 - Listar autores cadastrado
                            4 - Listar autores vivos em um determinado ano
                            5 - Listar livros em um terminado idioma
                            0 - Sair
                            ------------------------
                            """;
                    System.out.println(menu);
                    opcao = leitura.nextInt();
                    leitura.nextLine();

                    switch (opcao) {
                        case 1:
                            livroService.buscarLivroWeb();
                            break;
                        case 2:
                            livroService.listarLivrosCadastrados();
                            break;
                        case 3:
                            livroService.listarAutoresCadastrados();
                            break;
                        case 4:
                            livroService.autoresVivosPorAno();
                            break;
                        case 5:
                            livroService.livrosPorIdioma();
                            break;
                        case 0:
                            System.out.println("Terminando pesquisa...");
                            break;
                        default:
                            System.out.println("Opção inválida");
                    }

                }

        }





}