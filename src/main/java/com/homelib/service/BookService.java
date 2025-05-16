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
    private static final Scanner SCANNER = new Scanner(System.in);
    static BookInputReader bookInputReader;
    public static void save(Book book){
        BookRepository.save(book);
    }

    public static List<Book> findBookByName(String title){
        return new ArrayList<>(BookRepository.findAllBooks(title));
    }

    public static Optional<Book> findById(int id){
        return BookRepository.findById(id);
       /* if(bookFromDb.isEmpty()){
            System.out.println("A identificação não existe");
            return Optional.empty();
        }

        System.out.println(bookFromDb.toString());
        return boo

        Book book = bookFromDb.get();

        System.out.println(book);*/
    }

    public static int createNewBook(Book book){
        var createdBook = BookRepository.save(book);
        return createdBook.getId();
    }


    public static void deleteBookById(){
        System.out.println("Type the identification of the book: ");
        int id = Integer.parseInt(SCANNER.nextLine());
        BookRepository.deleteBookById(id);

    }



}
