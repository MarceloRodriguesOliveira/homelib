package com.homelib.utils;

import com.homelib.entities.Book;
import com.homelib.output.formatter.BookFormatter;
import com.homelib.output.io.FileReaderHelper;
import com.homelib.output.io.OutputFileWriterHelper;
import com.homelib.repository.BookRepository;
import com.homelib.service.BookService;

import java.util.List;
import java.util.Optional;

public class BookOperations {
    private final BookService bookService;
    private final FileReaderHelper fileReaderHelper;
    private final OutputFileWriterHelper outputFileWriterHelper;
    private final BookInputReader bookInputReader;

    public BookOperations(BookService bookService, FileReaderHelper fileReaderHelper, OutputFileWriterHelper outputFileWriterHelper, BookInputReader bookInputReader ) {
        this.bookService = bookService;
        this.fileReaderHelper = fileReaderHelper;
        this.outputFileWriterHelper = outputFileWriterHelper;
        this.bookInputReader = bookInputReader;
    }

    public void inputBook(){
        Book createBook = bookInputReader.readBook();
        bookService.createNewBook(createBook);
    }

    public void inputId(){
        int id = bookInputReader.readId();
        Optional<Book> bookFromDb = bookService.findById((long) id);
        if(bookFromDb.isEmpty()){
            System.out.println("Não existe livro com essa identificação");
            return;
        }
        Book book = bookFromDb.get();
        BookFormatter.formattedSingleBook(book);

    }

    public void listBookByName(){
        String title = bookInputReader.listBooks();
        List<Book> bookList = bookService.findBookByName(title);
        if(bookList.isEmpty()){
            System.out.println("Não existe nenhum livro com esse título");
            return;
        }
        BookFormatter.formattedBookList(bookList);
    }

    public void deleteBook(){
        int id = bookInputReader.readIdDelete();
        bookService.deleteBookById((long) id);
    }

    public void updateBook(){
        Long id = bookInputReader.readIdUpdate();
        var output = bookService.findById(id);

        if (output.isEmpty()){
            System.out.println("Id não existente");
            return;
        }

        Book updatedBook = bookInputReader.readBook();
        updatedBook.setId(id);
        bookService.updateBook(updatedBook);
    }

    public void readImportList(){
        Optional<List<Book>> listFromCsv = fileReaderHelper.readListFromCsv();
        if(listFromCsv.isEmpty() || listFromCsv.get().isEmpty()){
            return;
        }
        List<Book> bookList = listFromCsv.get();
        //BookFormatter.formattedBookListFromCsv(bookList);
    }

    public void exportListAsCsv(){
        List<Book> bookListFromDb = bookService.findBookByName("");
        outputFileWriterHelper.exportCsv(bookListFromDb);
    }

    public void importListFromFile(){
        Optional<List<Book>> list = fileReaderHelper.readListFromCsv();
        if(list.isEmpty() || list.get().isEmpty()){
            System.out.println("Essa lista ta vazia");
            return;
        }
        List<Book> recList = list.get().stream().toList();
        bookService.saveBookInBatch(recList);
    }


}
