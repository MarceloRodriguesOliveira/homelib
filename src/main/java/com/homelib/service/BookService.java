package com.homelib.service;

import com.homelib.entities.Book;
import com.homelib.repository.BookRepository;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;

    }


    public List<Book> findBookByName(String title){
        return new ArrayList<>(bookRepository.findAllBooks(title));
    }

    public Optional<Book> findById(Long id){
        return bookRepository.findById(id);
    }

    public Long createNewBook(Book book){
        Book createdBook = bookRepository.SaveWithAuthors(book);
        return createdBook.getId();
    }


    public void deleteBookById(Long id){
        var idExists = findById(id);

        if(idExists.isPresent()){
            bookRepository.deleteBookById(id);
            return;
        }

        System.out.println("Id não existe. Saindo...");

    }

    public void updateBook(Book book){
        bookRepository.update(book);
    }

    public void saveBookInBatch(List<Book> importedList){
        bookRepository.saveFromImportedList(importedList);
    }
}
