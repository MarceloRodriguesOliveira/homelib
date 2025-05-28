package com.homelib.factory;

import com.homelib.output.io.FileReaderHelper;
import com.homelib.output.io.FileSelectorHelper;
import com.homelib.output.io.OutputFileWriterHelper;
import com.homelib.repository.BookRepository;
import com.homelib.service.BookService;
import com.homelib.utils.BookOperations;

public class OperationsFactory {
    public static BookOperations createBookOperations(){
        BookRepository bookRepository = new BookRepository();
        BookService bookService = new BookService(bookRepository);

        FileSelectorHelper fileSelectorHelper = new FileSelectorHelper();
        FileReaderHelper fileReaderHelper = new FileReaderHelper(fileSelectorHelper);
        OutputFileWriterHelper outputFileWriterHelper = new OutputFileWriterHelper(fileSelectorHelper);
        return new BookOperations(bookService, fileReaderHelper, outputFileWriterHelper);

    }


}
