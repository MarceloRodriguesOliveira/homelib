package com.homelib.utils;

import com.homelib.entities.Book;
import com.homelib.output.formatter.BookFormatter;
import com.homelib.output.io.FileReaderHelper;
import com.homelib.output.io.OutputFileWriterHelper;
import com.homelib.repository.BookRepository;
import com.homelib.service.BookService;

import java.util.List;
import java.util.Optional;

public class BookOperations implements BookInputReader {
    private final BookService bookService;
    private final FileReaderHelper fileReaderHelper;
    private final OutputFileWriterHelper outputFileWriterHelper;

    public BookOperations(BookService bookService, FileReaderHelper fileReaderHelper, OutputFileWriterHelper outputFileWriterHelper ) {
        this.bookService = bookService;
        this.fileReaderHelper = fileReaderHelper;
        this.outputFileWriterHelper = outputFileWriterHelper;
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
        BookFormatter.formattedBookList(bookList);

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

    public void exportBookList(){
        List<Book> bookListFromDb = bookService.findBookByName("");
        outputFileWriterHelper.exportToTxt(bookListFromDb);
    }

    public void readImportList(){
        fileReaderHelper.readListFromCsv();
    }

    public void exportListAsCsv(){
        List<Book> bookListFromDb = bookService.findBookByName("");
        outputFileWriterHelper.exportCsv(bookListFromDb);
    }

    public void importListFromFile(){
        Optional<List<Book>> list = fileReaderHelper.readListFromCsv();
        if(list.isEmpty()){
            System.out.println("Essa lista ta vazia");
            return;
        }
        List<Book> recList = list.get().stream().toList();
        bookService.saveBookInBatch(recList);
    }


}
