package com.homelib.repository;


import com.homelib.entities.Book;
import com.homelib.entities.GlobalStore;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.UUID;

@Log4j2
public class BookRepository {
    public static void saveToDatabase(Book book){
        GlobalStore.getInstance().getData().add(book);
    }

    public static List<Book> findAllBooks(){
        System.out.println(GlobalStore.getInstance().getData());
        return GlobalStore.getInstance().getData();
    }

    public static void findById(UUID targetID){
        List<Book> books = GlobalStore.getInstance().getData();
        for (Book book:books){
            if(book.getId().equals(targetID)){
                System.out.println(book);
            }
        }
    }



}
