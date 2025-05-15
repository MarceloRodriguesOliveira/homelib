package com.homelib.factory;

import com.homelib.enums.OperationType;
import com.homelib.service.BookService;
import com.homelib.utils.BookInputReader;
import com.homelib.utils.BookOperations;
import com.homelib.utils.ServiceMenu;

public class BookInputFactory {
    public static void getMenu(OperationType action){
        BookOperations bookOperations = new BookOperations();
        switch (action){
            case CREATE -> {
                var book = bookOperations.readBook();
                BookService.createNewBook(book);
            }
            case SEARCH_ID -> {
                var id = bookOperations.readId();
                //BookService.deleteBookById();
            }
        };

    }
}
