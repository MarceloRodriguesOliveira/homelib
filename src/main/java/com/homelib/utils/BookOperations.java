package com.homelib.utils;

import com.homelib.entities.Book;
import com.homelib.service.BookService;

import java.util.List;

public class BookOperations implements BookInputReader {

    public void inputBook(){
        Book createBook = this.readBook();
        BookService.createNewBook(createBook);
    }

    public void inputId(){
        int id = this.readId();
        BookService.deleteBookById();
    }

    public void listBookByName(){
        String title = this.listBooks();
        List<Book> bookList = BookService.findBookByName(title);
        if(bookList.isEmpty()){
            System.out.println("Não existe nenhum livro com esse título");
            return;
        }
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        System.out.printf(
                "| %-26s | %-29s | %-4s | %-7s | %-36s |\n",
                "Título", "Autor", "Ano", "Edição", "ID");
        System.out.print(
                "+----------------------------+-------------------------------+------+---------+--------------------------------------+\n");
        for (Book book: bookList){
            System.out.printf("| %-26s | %-29s | %-4d | %-7s | %-36s |\n", book.getTitle(), book.getAuthor(), book.getYear(), book.getEdition(), book.getId());

        }
        System.out.print("----------------------------------------------------------------------------------------------------------------------\n");

    }
}
