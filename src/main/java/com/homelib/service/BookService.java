package com.homelib.service;

import com.homelib.entities.Book;
import com.homelib.entities.GlobalStore;
import com.homelib.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookService {
    public static void save(Book book){
        BookRepository.saveToDatabase(book);
    }

    public static List<Book> findAll(){
        List<Book> bookList = new ArrayList<>(BookRepository.findAllBooks());
        return bookList;
    }


}
