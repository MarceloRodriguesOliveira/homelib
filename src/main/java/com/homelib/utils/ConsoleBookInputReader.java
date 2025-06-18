package com.homelib.utils;

import com.homelib.entities.Author;
import com.homelib.entities.Book;
import com.homelib.enums.PublisherLocale;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;

public class ConsoleBookInputReader implements BookInputReader {
    private final Scanner SCANNER;

    public ConsoleBookInputReader(Scanner SCANNER) {
        this.SCANNER = SCANNER;
    }

    @Override
    public Book readBook() {
        boolean arg = true;
        List<Author> authorList = new ArrayList<>();
        System.out.println("Type the title of the book: ");
        String title = SCANNER.nextLine();
        System.out.println("Insert authors/blank to finish operation");
        while (true){
            System.out.println("Type author's first name: ");
            String firstName = SCANNER.nextLine();

            if(firstName.isEmpty()) break;

            System.out.println("Type author's last name: ");
            String lastName = SCANNER.nextLine();

            if(lastName.isEmpty()) break;
            authorList.add(new Author(firstName, lastName));
        }
        System.out.println("Type the year of publication");
        Integer year = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Type the edition number: ");
        Integer edition = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Type the publisher");
        String publisher = SCANNER.nextLine();
        Book createBook = Book
                .BookBuilder
                .builder()
                .title(title)
                .authors(authorList)
                .year(year)
                .edition(edition)
                .publisher(publisher)
                .locale(PublisherLocale.BRAZIL)
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
    public Long readIdUpdate() {
        System.out.println("Digite a id do livro que deseja atualizar");
        return Long.parseLong(SCANNER.nextLine());
    }
}
