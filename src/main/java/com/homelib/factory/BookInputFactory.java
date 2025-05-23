package com.homelib.factory;

import com.homelib.enums.OperationType;
import com.homelib.service.BookService;
import com.homelib.utils.BookInputReader;
import com.homelib.utils.BookOperations;
import com.homelib.utils.ServiceMenu;

import java.util.Optional;

public class BookInputFactory {
    public static void getMenu(OperationType action){
        BookOperations bookOperations = OperationsFactory.createBookOperations();
        switch (action){
            case CREATE -> {
                bookOperations.inputBook();
            }
            case SEARCH_ID -> {
                bookOperations.inputId();
            }
            case LIST_BOOKS -> {
                bookOperations.listBookByName();
            }
            case DELETE_BOOK -> {
                bookOperations.deleteBook();
            }
            case UPDATE -> {
                bookOperations.updateBook();
            }
            case EXPORT ->{
                bookOperations.exportBookList();
            }
        };

    }
}
