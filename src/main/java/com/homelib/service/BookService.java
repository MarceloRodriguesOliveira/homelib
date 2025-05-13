package com.homelib.service;

import com.homelib.entities.Book;
import com.homelib.entities.GlobalStore;
import com.homelib.repository.BookRepository;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

@Log4j2
public class BookService {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static void save(Book book){
        BookRepository.save(book);
    }

    public static void findBooksByName(){
        System.out.println("Digite o título lo livro ou deixe em branco para listar todos:");
        String title = SCANNER.nextLine();
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        System.out.printf(
                "| %-26s | %-29s | %-4s | %-7s | %-36s |\n",
                "Título", "Autor", "Ano", "Edição", "ID");
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        List<Book> bookList = new ArrayList<>(BookRepository.findAllBooks(title));
        for (Book book: bookList){
            System.out.printf("| %-26s | %-29s | %-4d | %-7s | %-36s |\n", book.getTitle(), book.getAuthor(), book.getYear(), book.getEdition(), book.getId());

        }
        System.out.print("----------------------------------------------------------------------------------------------------------------------\n");
    }

    /*public static void findById(){
        int id = null;
        while (id == null){
            System.out.print("Insert book id: ");
            try {
                id = Integer.parseInt(SCANNER.nextLine());
            }catch (IllegalArgumentException e){
                log.warn("Id is not formatted as UUID");
                System.out.println("Id não está no formato adequado. Tentando novamente...");
            }
        }

        Optional<Book> bookFromDb = BookRepository.findById(id);
        if(bookFromDb.isEmpty()){
            System.out.println("A identificação não existe");
            return;
        }

        Book book = bookFromDb.get();

        System.out.println(book);
    }*/

    public static void createNewBook(){
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
        log.info("saving '{}' by {}, {} ", title, lastName, firstName );
        System.out.println(createBook.toString());
        BookRepository.save(createBook);
    }


    public static void deleteBookById(){
        System.out.println("Type the identification of the book: ");
        int id = Integer.parseInt(SCANNER.nextLine());
        BookRepository.deleteBookById(id);

    }



}
