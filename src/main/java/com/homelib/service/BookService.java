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
       /* if(bookFromDb.isEmpty()){
            System.out.println("A identificação não existe");
            return Optional.empty();
        }

        System.out.println(bookFromDb.toString());
        return boo

        Book book = bookFromDb.get();

        System.out.println(book);*/
    }

    public int createNewBook(Book book){
        Book createdBook = bookRepository.save(book);
        return createdBook.getId();
    }


    public void deleteBookById(){
        System.out.println("Type the identification of the book: ");
        //int id = Integer.parseInt(SCANNER.nextLine());
        //BookRepository.deleteBookById(id);

    }



}
