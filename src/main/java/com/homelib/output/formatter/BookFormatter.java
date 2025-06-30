package com.homelib.output.formatter;

import com.homelib.entities.Author;
import com.homelib.entities.Book;
import com.homelib.enums.PublisherLocale;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
public class BookFormatter {
    private BookFormatter(){}

    /*public static String toExport(Book book){
        return String.format("%s - %s, %d ed - %d", book.getTitle(), book.getAuthor(), book.getEdition(), book.getYear());
    }*/

    /*public static String formatAsCsv(Book book){
        return String.format("%s,%s,%s,%d ed,%d", book.getTitle(), book.getFirstNameAuthor(),book.getLastNameAuthor(), book.getEdition(), book.getYear());
    }*/

    public static Book getCsvValueAsBook(String line){
        List<Author> authorListFromLine = new ArrayList<>();
        Pattern csvSplit = Pattern.compile(",");
        Pattern splitNames = Pattern.compile(";");
        String[] fields = csvSplit.split(line);
        String[] names = splitNames.split(fields[5]);

        if(line.length() < 6){
            throw new IllegalArgumentException("line format not supported. Check FAQ for details.");
        }

        Arrays.stream(names).map(a->{
            int lastSpace = a.lastIndexOf(" ");
            if(lastSpace == -1){
                log.warn("Linha mal formatada {}, inserindo apenas primeiro nome", a);
                return new Author(a.trim(), "");
            }
            String lastname = a.substring(lastSpace+1).trim();
            String firstname = a.substring(0, lastSpace);
            return new Author(firstname, lastname);
        }).forEachOrdered(authorListFromLine::add);


        return Book.BookBuilder
                .builder()
                .title(fields[0])
                .authors(authorListFromLine)
                .year(Integer.parseInt(fields[1]))
                .edition(Integer.parseInt(fields[2]))
                .publisher(fields[3])
                .locale(PublisherLocale.valueOf(fields[4]))
                .build();
    }

    public static void formattedBookList(List<Book> books){

        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        System.out.printf(
                "| %-26s | %-46s | %-4s | %-7s | %-36s |\n",
                "Título", "Autor", "Ano", "Edição", "ID");
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        for (Book book: books){
            String authorNames = book.getAuthorInitials(book.getAuthors());
            System.out.printf("| %-26s | %-46s | %-4d | %-7s | %-36s |\n", book.getTitle(), authorNames, book.getYear(), book.getEdition(), book.getId());

        }
        System.out.print("----------------------------------------------------------------------------------------------------------------------\n");
    }

    public static void formattedSingleBook(Book book){
        System.out.printf("%s (%d) by %s.%s (%s)\n", book.getTitle(),book.getYear() ,book.getAuthorsFullName(book.getAuthors()), book.getPublisher(), book.getLocale());
    }

    public static void formattedBookListFromCsv(List<Book> books){
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        System.out.printf(
                "| %-26s | %-29s | %-4s | %-7s |\n",
                "Título", "Autor", "Ano", "Edição");
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        for (Book book: books){
            String authorNames = book.getAuthorsFullName(book.getAuthors());

            System.out.printf("| %-26s | %-29s | %-4d | %-7s |\n", book.getTitle(), authorNames, book.getYear(), book.getEdition());

        }
        System.out.print("----------------------------------------------------------------------------------------------------------------------\n");
    }


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
