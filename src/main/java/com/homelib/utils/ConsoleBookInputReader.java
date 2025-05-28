package com.homelib.utils;

import com.homelib.entities.Book;

import java.util.Scanner;

public class ConsoleBookInputReader implements BookInputReader {
    private final Scanner SCANNER;

    public ConsoleBookInputReader(Scanner SCANNER) {
        this.SCANNER = SCANNER;
    }

    @Override
    public Book readBook() {
        System.out.println("Type the title of the book: ");
        String title = SCANNER.nextLine();
        System.out.println("Type author's first name: ");
        String firstName = SCANNER.nextLine();
        System.out.println("Type author's last name: ");
        String lastName = SCANNER.nextLine();
        System.out.println("Type the year of publication");
        Integer year = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Type the edition number: ");
        Integer edition = Integer.parseInt(SCANNER.nextLine());

        Book createBook = Book
                .BookBuilder
                .builder()
                .title(title)
                .firstNameAuthor(firstName)
                .lastNameAuthor(lastName)
                .year(year)
                .edition(edition)
                .build();
        return createBook;
    }

    @Override
    public int readId() {
        System.out.println("Type the id of the book: ");
        return Integer.parseInt(SCANNER.nextLine());
    }

    @Override
    public String listBooks() {
        System.out.println("Digite o título do livro ou deixe em branco para listar todos: ");
        return SCANNER.nextLine();
    }

    @Override
    public int readIdDelete() {
        System.out.println("Digite o id do livro que deseja apagar");
        return Integer.parseInt(SCANNER.nextLine());
    }

    @Override
    public int readIdUpdate() {
        System.out.println("Digite a id do livro que deseja atualizar");
        return Integer.parseInt(SCANNER.nextLine());
    }
}
