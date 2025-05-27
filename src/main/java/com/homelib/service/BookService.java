package com.homelib.service;

import com.homelib.entities.Book;
import com.homelib.entities.GlobalStore;
import com.homelib.output.io.FileReaderHelper;
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

    public void updateBook(Book book){
        bookRepository.update(book);
    }

    public void saveBookInBatch(){
        Optional<List<Book>> list = FileReaderHelper.readListFromCsv();
        if(list.isEmpty()){
            System.out.println("Essa lista ta vazia");
            return;
        }
        List<Book> recList = list.get().stream().toList();
        bookRepository.saveFromImportedList(recList);
    }
}
