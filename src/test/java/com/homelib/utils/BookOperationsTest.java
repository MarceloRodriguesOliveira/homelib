package com.homelib.utils;

import com.homelib.entities.Book;
import com.homelib.output.formatter.BookFormatter;
import com.homelib.output.io.FileReaderHelper;
import com.homelib.output.io.OutputFileWriterHelper;
import com.homelib.service.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookOperationsTest {
    @Mock
    private BookService bookService;
    @Mock
    private FileReaderHelper fileReaderHelper;
    @Mock
    private OutputFileWriterHelper outputFileWriterHelper;
    @Mock
    private BookInputReader bookInputReader;
    @InjectMocks
    private BookOperations bookOperations;

    @Captor
    ArgumentCaptor<Book> bookArgumentCaptor;

    @Captor
    ArgumentCaptor<Integer> idArgumentCaptor;

    @Captor
    ArgumentCaptor<String> titleArgumentCaptor;

    @Captor
    ArgumentCaptor<List<Book>> listArgumentCaptor;

    private Book book;
    private List<Book> bookList;
    private MockedStatic<BookFormatter> bookFormatterMockedStatic;

    @BeforeEach
    void setUp(){
        book = Book.BookBuilder
                .builder()
                .title("title")
                .firstNameAuthor("John")
                .lastNameAuthor("Doe")
                .year(1970)
                .edition(1)
                .build();

        bookFormatterMockedStatic = Mockito.mockStatic(BookFormatter.class);
        bookList = List.of(book);
    }

    @AfterEach
    void tearDown(){
        bookFormatterMockedStatic.close();
    }

    @Nested
    class inputBook{
        @Test
        @DisplayName("should get input and create book")
        void shouldGetInputAndCreateBook(){
            when(bookInputReader.readBook()).thenReturn(book);

            bookOperations.inputBook();

            verify(bookService).createNewBook(bookArgumentCaptor.capture());
            assertSame(book, bookArgumentCaptor.getValue());
        }
    }

    @Nested
    class inputId{
        @Test
        @DisplayName("Should read id value and return book if id matches")
        void shouldReadIdValueAndReturnBookValueOnIdMatch(){
            Optional<Book> bookFromDb = Optional.of(book);
            int anyId = 42;
            doReturn(anyId).when(bookInputReader).readId();
            doReturn(bookFromDb).when(bookService).findById(anyId);

            bookOperations.inputId();

            verify(bookInputReader,times(1)).readId();
            verify(bookService, times(1)).findById(idArgumentCaptor.capture());
            assertEquals(anyId, idArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should return nothing when Optional is Empty")
        void shouldReturnNothingWhenOptionalIsEmpty(){
            int anyId = 42;
            doReturn(anyId).when(bookInputReader).readId();
            doReturn(Optional.empty()).when(bookService).findById(anyInt());

            bookOperations.inputId();

            verify(bookInputReader).readId();
            verify(bookService).findById(idArgumentCaptor.capture());
            assertEquals(anyId, idArgumentCaptor.getValue());
        }
    }

    @Nested
    class listBookByName{
        @Test
        @DisplayName("Should return formatted list")
        void shouldReturnFormattedList(){
            String title = "title";
            List<Book> bookListFromDb = List.of(book, book);
            doReturn(title).when(bookInputReader).listBooks();
            doReturn(bookListFromDb).when(bookService).findBookByName(anyString());

            bookOperations.listBookByName();

            verify(bookInputReader).listBooks();
            verify(bookService).findBookByName(titleArgumentCaptor.capture());
            bookFormatterMockedStatic.verify(()->BookFormatter.formattedBookList(bookListFromDb), times(1));

            assertEquals(title, titleArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should not format list if list is empty")
        void shouldNotFormatListIfEmpty(){
            List<Book> expectedEmptyList = Collections.emptyList();
            String title = "title";
            doReturn(title).when(bookInputReader).listBooks();
            doReturn(expectedEmptyList).when(bookService).findBookByName(anyString());

            bookOperations.listBookByName();

            verify(bookInputReader).listBooks();
            verify(bookService).findBookByName(titleArgumentCaptor.capture());
            bookFormatterMockedStatic.verify(()-> BookFormatter.formattedBookList(expectedEmptyList), times(0));

            assertEquals(title, titleArgumentCaptor.getValue());
        }
    }

    @Nested
    class deleteBook{
        @Test
        @DisplayName("Should delete book given id")
        void shouldDeleteBookGivenId(){
            int anyId = 42;
            doReturn(anyId).when(bookInputReader).readIdDelete();
            doNothing().when(bookService).deleteBookById(anyInt());

            bookOperations.deleteBook();

            verify(bookInputReader).readIdDelete();
            verify(bookService).deleteBookById(idArgumentCaptor.capture());

            assertEquals(anyId, idArgumentCaptor.getValue());
        }
    }

    @Nested
    class updateBook{
        @Test
        @DisplayName("Should update book if id exists")
        void shouldUpdateBookIfIdExists(){
            int anyId = 42;
            Optional<Book> bookFromDb = Optional.of(book);
            assertEquals(0, book.getId());

            doReturn(anyId).when(bookInputReader).readIdUpdate();
            doReturn(bookFromDb).when(bookService).findById(anyInt());
            doReturn(book).when(bookInputReader).readBook();

            bookOperations.updateBook();

            verify(bookInputReader).readIdUpdate();
            verify(bookService).findById(idArgumentCaptor.capture());
            verify(bookService).updateBook(bookArgumentCaptor.capture());

            assertEquals(anyId, idArgumentCaptor.getValue());
            assertEquals(anyId, bookArgumentCaptor.getValue().getId());

            assertNotEquals(0, bookArgumentCaptor.getValue().getId());
        }

        @Test
        @DisplayName("Should not update book if id does not exist")
        void shouldNotUpdateBookIfIdDoesNotExist(){
            int anyId = 42;
            Optional<Book> bookFromDb = Optional.empty();
            doReturn(anyId).when(bookInputReader).readIdUpdate();
            doReturn(Optional.empty()).when(bookService).findById(anyInt());

            bookOperations.updateBook();

            verify(bookInputReader).readIdUpdate();
            verify(bookService).findById(idArgumentCaptor.capture());
            verify(bookService, times(0)).updateBook(bookArgumentCaptor.capture());

            assertEquals(anyId, idArgumentCaptor.getValue());
        }
    }

    @Nested
    class exportListAsCsv{
        @Test
        @DisplayName("Should export book list to csv format")
        void shouldExportBookListToCsvFormat(){
            String allBooks = "";
            List<Book> allBooksFromDb = List.of(book);
            doReturn(allBooksFromDb).when(bookService).findBookByName(anyString());
            doNothing().when(outputFileWriterHelper).exportCsv(allBooksFromDb);

            bookOperations.exportListAsCsv();

            verify(bookService).findBookByName(titleArgumentCaptor.capture());
            verify(outputFileWriterHelper).exportCsv(listArgumentCaptor.capture());

            assertEquals(allBooks, titleArgumentCaptor.getValue());
            assertEquals(allBooksFromDb, listArgumentCaptor.getValue());
        }
    }

    @Nested
    class importListFromFile{
        @Test
        @DisplayName("Should import data from list if file is not empty")
        void shouldImportData_From_List_If_File_IsNotEmpty(){
            Optional<List<Book>> listFromFile = Optional.of(bookList);
            doReturn(listFromFile).when(fileReaderHelper).readListFromCsv();

            bookOperations.importListFromFile();

            verify(fileReaderHelper).readListFromCsv();
            verify(bookService).saveBookInBatch(listArgumentCaptor.capture());

            assertFalse(listArgumentCaptor.getValue().isEmpty());
            assertEquals(bookList, listArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should not import data from file if file is empty")
        void shouldNotImportDataOnEmptyFile(){
            Optional<List<Book>> listFromFile = Optional.of(Collections.emptyList());
            doReturn(listFromFile).when(fileReaderHelper).readListFromCsv();

            bookOperations.importListFromFile();

            verify(fileReaderHelper).readListFromCsv();
            verify(bookService, never()).saveBookInBatch(anyList());
        }
    }

    @Nested
    class readImportList{
        @Test
        @DisplayName("Should read an imported list on terminal")
        void shouldReadImportedListOnTerminal(){
            Optional<List<Book>> listFromFile = Optional.of(bookList);
            doReturn(listFromFile).when(fileReaderHelper).readListFromCsv();

            bookOperations.readImportList();


            verify(fileReaderHelper).readListFromCsv();
            bookFormatterMockedStatic.verify(()-> BookFormatter.formattedBookListFromCsv(listFromFile.get()), times(1));
        }

        @Test
        @DisplayName("Should not read if file is empty")
        void shouldNotReadIfFileIsEmpty(){
            Optional<List<Book>> emptyListFromFile = Optional.of(Collections.emptyList());
            doReturn(emptyListFromFile).when(fileReaderHelper).readListFromCsv();

            bookOperations.readImportList();

            verify(fileReaderHelper).readListFromCsv();
            bookFormatterMockedStatic.verify(()-> BookFormatter.formattedBookListFromCsv(emptyListFromFile.get()), never());

        }
    }



}