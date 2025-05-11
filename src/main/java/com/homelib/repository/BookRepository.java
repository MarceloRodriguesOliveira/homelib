package com.homelib.repository;


import com.homelib.entities.Book;
import com.homelib.entities.GlobalStore;
import com.homelib.service.BookService;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
public class BookRepository {
    public static void saveToDatabase(Book book){
        GlobalStore.getInstance().getData().add(book);
    }

    public static List<Book> findAllBooks(){
        return GlobalStore.getInstance().getData();
    }

    public static Optional<Book> findById(UUID targetID){
        List<Book> books = GlobalStore.getInstance().getData();
        for (Book book:books){
            if(book.getId().equals(targetID)){
                Book targetBook = Book.BookBuilder
                        .builder()
                        .title(book.getTitle())
                        .firstNameAuthor(book.getFirstNameAuthor())
                        .lastNameAuthor(book.getLastNameAuthor())
                        .year(book.getYear())
                        .edition(book.getEdition())
                        .id(book.getId())
                        .build();
                log.info("found '{}' by {}, {} ", targetBook.getTitle(), targetBook.getLastNameAuthor(), targetBook.getFirstNameAuthor());
                return Optional.of(targetBook);
            }
        }
        return Optional.empty();
    }

    public static void deleteBookById(UUID searchId){
        Optional<Book> bookFromDb = findById(searchId);
        if (bookFromDb.isPresent()){
            Book book = bookFromDb.get();
            GlobalStore.getInstance().getData().removeIf(b -> b.getId().equals(searchId));
            System.out.println("Deleted book of Id: " + searchId);
            return;
        }
        log.info("Id is not present on any book register");

    }




}
