package com.homelib.service;

import com.homelib.entities.Book;
import com.homelib.repository.BookRepository;

import java.util.List;

public class BookService {
    public static void save(Book book){
        BookRepository.saveToDatabase(book);
    }

    public static List<Book> findAll(){
        return BookRepository.findAllBooks();
    }
}
