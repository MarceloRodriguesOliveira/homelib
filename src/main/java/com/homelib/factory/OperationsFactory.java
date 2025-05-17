package com.homelib.factory;

import com.homelib.repository.BookRepository;
import com.homelib.service.BookService;
import com.homelib.utils.BookOperations;

public class OperationsFactory {
    public static BookOperations createBookOperations(){
        BookRepository bookRepository = new BookRepository();
        BookService bookService = new BookService(bookRepository);
        return new BookOperations(bookService);

    }
}
