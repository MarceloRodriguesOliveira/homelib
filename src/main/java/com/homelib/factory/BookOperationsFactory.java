package com.homelib.factory;

import com.homelib.enums.OperationType;
import com.homelib.utils.BookOperations;

public class BookOperationsFactory {
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
