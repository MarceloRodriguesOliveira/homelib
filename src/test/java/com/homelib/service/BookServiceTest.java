package com.homelib.service;

import com.homelib.entities.Book;
import com.homelib.repository.BookRepository;
import com.homelib.utils.BookInputReader;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    BookInputReader bookInputReader;

    @InjectMocks
    private BookService bookService;

    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> titleArgumentCaptor;

    @Captor
    private ArgumentCaptor<Integer> idArgumentCaptor;

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
    class createNewBook{
        @Test
        @DisplayName("Should create a new book class")
        void shouldCreateNewBook(){
            //Arrange
            doReturn(book).when(bookRepository).save(any(Book.class));


            //Act
            var output = bookService.createNewBook(book);


            //Assert
            assertNotNull(output);
            verify(bookRepository).save(bookArgumentCaptor.capture());
            var bookCaptured = bookArgumentCaptor.getValue();
            assertEquals(book.getTitle(), bookCaptured.getTitle());
            assertEquals(book.getId(),  bookCaptured.getId());
            assertEquals(book.getFirstNameAuthor(),  bookCaptured.getFirstNameAuthor());
            assertEquals(book.getLastNameAuthor(),  bookCaptured.getLastNameAuthor());

        }

        @Test
        @DisplayName("Should launch exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs(){
            doThrow(new RuntimeException()).when(bookRepository).save(any());

            assertThrows(RuntimeException.class, ()-> bookService.createNewBook(book));
        }
    }

    @Nested
    class findBookByName{
        @Test
        @DisplayName("Should return list of books with similar titles")
        void shouldReturnListOfBooks(){
            List<Book> bookList = List.of(book);
            doReturn(bookList).when(bookRepository).findAllBooks(anyString());

            var output = bookService.findBookByName(book.getTitle());

            assertNotNull(output);
            verify(bookRepository).findAllBooks(titleArgumentCaptor.capture());
            assertEquals(book.getTitle(), titleArgumentCaptor.getValue());

        }

        @Test
        @DisplayName("should return empty list when there is no matching data")
        void shouldReturnEmptyListWhenThereIsNoMatchingData(){
            List<Book> bookList = List.of();
            doReturn(bookList).when(bookRepository).findAllBooks(anyString());

            var output = bookService.findBookByName(book.getTitle());

            assertNotNull(output);
            verify(bookRepository).findAllBooks(titleArgumentCaptor.capture());
            assertTrue(output.isEmpty());
        }
    }

    @Nested
    class findBookById{
        @Test
        @DisplayName("Should return a Optional book object when id matches")
        void shouldReturnOptionalWhenIdMatches(){
            Optional<Book> bookFromDb = Optional.of(book);
            doReturn(bookFromDb).when(bookRepository).findById(anyInt());

            var output = bookService.findById(book.getId());

            assertNotNull(output);
            assertTrue(output.isPresent());
            assertEquals(book, output.get());
            verify(bookRepository).findById(idArgumentCaptor.capture());
            assertEquals(book.getId(), idArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should return empty optional when there is no matching id")
        void shouldReturnEmptyOptionalWhenThereIsNoMatchingId(){
            doReturn(Optional.empty()).when(bookRepository).findById(anyInt());

            var output = bookService.findById(book.getId());

            assertNotNull(output);

            verify(bookRepository).findById(idArgumentCaptor.capture());
            assertEquals(book.getId(), idArgumentCaptor.getValue());

        }
    }

    @Nested
    class deleteBookByiId{
        @Test
        @DisplayName("Should delete book when book exists")
        void shouldDeleteBookWhenBookExists(){
            //Arrange
            int anyId = 42;
            doReturn(Optional.of(book)).when(bookRepository).findById(anyInt());
            doNothing().when(bookRepository).deleteBookById(anyInt());

            //Act
            bookService.deleteBookById(anyId);


            //Assert
            verify(bookRepository, times(1)).findById(idArgumentCaptor.capture());
            verify(bookRepository, times(1)).deleteBookById(idArgumentCaptor.capture());

            var idList = idArgumentCaptor.getAllValues();
            assertEquals(2, idList.size());
            assertEquals(anyId, idList.get(0));
            assertEquals(anyId, idList.get(1));
            assertEquals(idList.get(0), idList.get(1));
        }

        @Test
        @DisplayName("Should not delete book when book does not exist")
        void shouldNotDeleteBookIfBookDoesNotExist(){
            int anyId = 42;
            doReturn(Optional.empty()).when(bookRepository).findById(anyInt());


            bookService.deleteBookById(anyId);

            verify(bookRepository, times(1)).findById(idArgumentCaptor.capture());
            int capturedId = idArgumentCaptor.getValue();
            assertEquals(anyId, capturedId);
            verify(bookRepository, never()).deleteBookById(anyInt());

        }
    }

}