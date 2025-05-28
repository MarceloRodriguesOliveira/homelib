package com.homelib.utils;

import com.homelib.entities.Book;
import com.homelib.output.io.FileReaderHelper;
import com.homelib.output.io.OutputFileWriterHelper;
import com.homelib.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private Book book;

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


}