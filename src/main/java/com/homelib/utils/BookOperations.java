package com.homelib.utils;

import com.homelib.entities.Book;
import com.homelib.service.BookService;

import java.util.List;
import java.util.Optional;

public class BookOperations implements BookInputReader {
    private final BookService bookService;

    public BookOperations(BookService bookService) {
        this.bookService = bookService;
    }

    public void inputBook(){
        Book createBook = this.readBook();
        bookService.createNewBook(createBook);
    }

    public void inputId(){
        int id = this.readId();
        Optional<Book> bookFromDb = bookService.findById(id);
        if(bookFromDb.isEmpty()){
            System.out.println("Não existe livro com essa identificação");
            return;
        }
        Book book = bookFromDb.get();
        System.out.println(book);

    }

    public void listBookByName(){
        String title = this.listBooks();
        List<Book> bookList = bookService.findBookByName(title);
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

    public void deleteBook(){
        int id = readIdDelete();
        bookService.deleteBookById(id);
    }

    public void updateBook(){
        int id = readIdUpdate();
        var output = bookService.findById(id);

        if (output.isEmpty()){
            System.out.println("Id não existente");
            return;
        }

        Book updatedBook = readBook();
        updatedBook.setId(id);
        bookService.updateBook(updatedBook);
    }


}
