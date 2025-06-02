package com.homelib.utils;

public class MainMenu implements Menu {
    @Override
    public void options() {
        System.out.println("1. Procurar por nome/listar todos os livros");
        System.out.println("2. Buscar livro via identificador");
        System.out.println("3. Registrar novo livro");
        System.out.println("4. Apagar livro");
        System.out.println("5. Atualizar dados de um livro");
        System.out.println("6. Ler dados de arquivo");
        System.out.println("7. Criar arquivo csv");
        System.out.println("8. Importar dados de arquivo csv");
        System.out.println("0. Sair do programa");
    }
}
