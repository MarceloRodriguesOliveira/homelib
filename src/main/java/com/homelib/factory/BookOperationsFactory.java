package com.homelib.factory;

import com.homelib.enums.FileIOOperationType;
import com.homelib.enums.OperationType;
import com.homelib.utils.BookOperations;

public class BookOperationsFactory {
    private static final BookOperations bookOperations = OperationsFactory.createBookOperations();
    public static void getInputMenu(OperationType action){
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
        };
    }
    public static void getIOMenu(FileIOOperationType action){
        switch (action){
            case READ -> bookOperations.readImportList();
            case EXPORT -> bookOperations.exportBookList();
            case CSV_EXPORT -> bookOperations.exportListAsCsv();
            case CSV_IMPORT -> bookOperations.importListFromFile();
        }
    }
}
