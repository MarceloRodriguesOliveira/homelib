package com.homelib.output.formatter;

import com.homelib.entities.Author;
import com.homelib.entities.Book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

public class BookFormatter {
    private BookFormatter(){}

    /*public static String toExport(Book book){
        return String.format("%s - %s, %d ed - %d", book.getTitle(), book.getAuthor(), book.getEdition(), book.getYear());
    }*/

    /*public static String formatAsCsv(Book book){
        return String.format("%s,%s,%s,%d ed,%d", book.getTitle(), book.getFirstNameAuthor(),book.getLastNameAuthor(), book.getEdition(), book.getYear());
    }*/

    /*public static Book getCsvValueAsBook(String line){
        Pattern csvSplit = Pattern.compile(",");
        String[] fields = csvSplit.split(line);

        if(fields.length < 5  ){
            throw new IllegalArgumentException("Formato da linha invalido "+ line);
        }

        return Book.BookBuilder
                .builder()
                .title(fields[0])
                .firstNameAuthor(fields[1])
                .lastNameAuthor(fields[2])
                .edition(Integer.parseInt(fields[3].substring(0,1)))
                .year(Integer.parseInt(fields[4]))
                .build();
    }*/

    public static void formattedBookList(List<Book> books){

        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        System.out.printf(
                "| %-26s | %-46s | %-4s | %-7s | %-36s |\n",
                "Título", "Autor", "Ano", "Edição", "ID");
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        for (Book book: books){
            String authorNames = book.getAuthorsFullName(book.getAuthors());
            System.out.printf("| %-26s | %-46s | %-4d | %-7s | %-36s |\n", book.getTitle(), authorNames, book.getYear(), book.getEdition(), book.getId());

        }
        System.out.print("----------------------------------------------------------------------------------------------------------------------\n");
    }

    public static void formattedSingleBook(Book book){
        System.out.printf("%s (%d) by %s.%s (%s)\n", book.getTitle(),book.getYear() ,book.getAuthorsFullName(book.getAuthors()), book.getPublisher(), book.getLocale());
    }

    /*public static void formattedBookListFromCsv(List<Book> books){
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        System.out.printf(
                "| %-26s | %-29s | %-4s | %-7s |\n",
                "Título", "Autor", "Ano", "Edição");
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        for (Book book: books){
            System.out.printf("| %-26s | %-29s | %-4d | %-7s |\n", book.getTitle(), book.getAuthor(), book.getYear(), book.getEdition());

        }
        System.out.print("----------------------------------------------------------------------------------------------------------------------\n");
    }*/


    /*public static void formattedImportedList(){
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        System.out.printf(
                "| %-26s | %-29s | %-4s | %-7s | %-36s |\n",
                "Título", "Autor", "Ano", "Edição", "ID");
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");

        System.out.print("----------------------------------------------------------------------------------------------------------------------\n");
    }*/

}
