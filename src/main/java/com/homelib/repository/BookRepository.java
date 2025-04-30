package com.homelib.repository;


import com.homelib.entities.Book;
import com.homelib.entities.GlobalStore;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class BookRepository {
    public static void saveToDatabase(Book book){
        GlobalStore.getInstance().getData().add(book);
    }

    public static List<Book> findAllBooks(){
        return GlobalStore.getInstance().getData();
    }


}
