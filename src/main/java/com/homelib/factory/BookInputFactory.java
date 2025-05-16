package com.homelib.factory;

import com.homelib.enums.OperationType;
import com.homelib.service.BookService;
import com.homelib.utils.BookInputReader;
import com.homelib.utils.BookOperations;
import com.homelib.utils.ServiceMenu;

import java.util.Optional;

public class BookInputFactory {
    public static void getMenu(OperationType action){
        BookOperations bookOperations = new BookOperations();
        switch (action){
            case CREATE -> {
                bookOperations.inputBook();
            }
            case SEARCH_ID -> {
                var id = bookOperations.readId();
                var book = BookService.findById(id);
                System.out.println(book);
            }
            case LIST_BOOKS -> {
                bookOperations.listBookByName();
            }
        };

    }
}
