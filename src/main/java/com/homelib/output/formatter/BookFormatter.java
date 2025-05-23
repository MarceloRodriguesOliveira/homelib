package com.homelib.output.formatter;

import com.homelib.entities.Book;

public class BookFormatter {
    private BookFormatter(){}

    public static String toExport(Book book){
        return String.format("%s - %s, %d ed - %d", book.getTitle(), book.getAuthor(), book.getEdition(), book.getYear());
    }
}
