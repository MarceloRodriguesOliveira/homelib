package com.homelib.service;

import com.homelib.entities.Book;
import com.homelib.entities.GlobalStore;
import com.homelib.repository.BookRepository;
import com.homelib.utils.BookInputReader;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

@Log4j2
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;

    }


    public List<Book> findBookByName(String title){
        return new ArrayList<>(bookRepository.findAllBooks(title));
    }

    public Optional<Book> findById(int id){
        return bookRepository.findById(id);
    }

    public int createNewBook(Book book){
        Book createdBook = bookRepository.save(book);
        return createdBook.getId();
    }


    public void deleteBookById(int id){
        var idExists = findById(id);

        if(idExists.isPresent()){
            bookRepository.deleteBookById(id);
            return;
        }

        System.out.println("Id não existe. Saindo...");

    }

    public void updateBook(int id){
        Optional<Book> bookFromDb = findById(id);

        if(bookFromDb.isPresent()){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type the title of the book: ");
            String title = scanner.nextLine();
            System.out.println("Type author's first name: ");
            String firstName = scanner.nextLine();
            System.out.println("Type author's last name: ");
            String lastName = scanner.nextLine();
            System.out.println("Type the year of publication");
            Integer year = Integer.parseInt(scanner.nextLine());
            System.out.println("Type the edition number: ");
            Integer edition = Integer.parseInt(scanner.nextLine());

            Book bookToBeUpdated = Book
                        .BookBuilder
                        .builder()
                        .title(title)
                        .firstNameAuthor(firstName)
                        .lastNameAuthor(lastName)
                        .year(year)
                        .edition(edition)
                        .id(id)
                        .build();
            bookRepository.update(bookToBeUpdated);
            return;
        }

        log.info("Could not match id to any existing book");
    }



}
