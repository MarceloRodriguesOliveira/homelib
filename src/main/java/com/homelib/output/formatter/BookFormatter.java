package com.homelib.output.formatter;

import com.homelib.entities.Book;

import java.util.List;

public class BookFormatter {
    private BookFormatter(){}

    public static String toExport(Book book){
        return String.format("%s - %s, %d ed - %d", book.getTitle(), book.getAuthor(), book.getEdition(), book.getYear());
    }

    public static void formattedBookList(List<Book> books){
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        System.out.printf(
                "| %-26s | %-29s | %-4s | %-7s | %-36s |\n",
                "Título", "Autor", "Ano", "Edição", "ID");
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        for (Book book: books){
            System.out.printf("| %-26s | %-29s | %-4d | %-7s | %-36s |\n", book.getTitle(), book.getAuthor(), book.getYear(), book.getEdition(), book.getId());

        }
        System.out.print("----------------------------------------------------------------------------------------------------------------------\n");
    }
}
