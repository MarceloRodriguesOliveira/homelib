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
        BookRepository.saveToDatabase(book);
    }

    public static List<Book> findAll(){
        List<Book> bookList = new ArrayList<>(BookRepository.findAllBooks());
        return bookList;
    }

    public static void findById(UUID id){
        Optional<Book> bookFromDb = BookRepository.findById(id);
        if(bookFromDb.isEmpty()){
            System.out.println("No such identifier");
        }

        System.out.println(bookFromDb.toString());
    }

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
        BookRepository.saveToDatabase(createBook);
    }



}
