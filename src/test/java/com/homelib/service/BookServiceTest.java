package com.homelib.service;

import com.homelib.entities.Author;
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
    private ArgumentCaptor<Long> idArgumentCaptor;

    private Book book;
    private List<Author> authors = new ArrayList<>();

    private List<Book> importedBooks;

    @BeforeEach
    void setUp(){
        Author authorMocked1 = new Author("Jane", "Doe");
        Author authorMocked2 = new Author("John", "Doe");
        authors = List.of(authorMocked1, authorMocked2);
        book = Book.BookBuilder
                    .builder()
                    .title("title")
                    .authors(authors)
                    .year(1970)
                    .edition(1)
                    .build();
        Long anyId = 42L;
        book.setId(anyId);
        importedBooks = List.of(book);
    }

    @Nested
    class createNewBook{
        @Test
        @DisplayName("Should create a new book class")
        void shouldCreateNewBook(){
            //Arrange
            doReturn(book).when(bookRepository).SaveWithAuthors(book);


            //Act
            var output = bookService.createNewBook(book);


            //Assert
            assertNotNull(output);
            verify(bookRepository).SaveWithAuthors(bookArgumentCaptor.capture());
            var bookCaptured = bookArgumentCaptor.getValue();
            assertEquals(book.getTitle(), bookCaptured.getTitle());
            assertEquals(book.getId(),  bookCaptured.getId());

            assertIterableEquals(book.getAuthors(), bookCaptured.getAuthors());

        }

        @Test
        @DisplayName("Should launch exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs(){
            doThrow(new RuntimeException()).when(bookRepository).SaveWithAuthors(any());

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
            doReturn(bookFromDb).when(bookRepository).findById(book.getId());

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
            doReturn(Optional.empty()).when(bookRepository).findById(anyLong());

            var output = bookService.findById(book.getId());

            assertNotNull(output);

            verify(bookRepository).findById(idArgumentCaptor.capture());
            assertEquals(book.getId(), idArgumentCaptor.getValue());

        }
    }

    @Nested
    class deleteBookById{
        @Test
        @DisplayName("Should delete book when book exists")
        void shouldDeleteBookWhenBookExists(){
            //Arrange
            long anyId = 42;
            doReturn(Optional.of(book)).when(bookRepository).findById(anyLong());
            doNothing().when(bookRepository).deleteBookById(anyLong());

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
        void shouldNotDeleteBookIfBookDoesNotExists(){
            long anyId = 40;
            doReturn(Optional.empty()).when(bookRepository).findById(anyLong());


            bookService.deleteBookById(anyId);

            verify(bookRepository, times(1)).findById(idArgumentCaptor.capture());
            long capturedId = idArgumentCaptor.getValue();
            assertEquals(anyId, capturedId);
            verify(bookRepository, never()).deleteBookById(anyLong());

        }
    }

    @Nested
    class updateBook{
        @Test
        @DisplayName("Should update values if book exists")
        void shouldUpdateBookIfBookExists(){
            long anyId = 42;
            book.setId(anyId);

            bookService.updateBook(book);

            verify(bookRepository).update(bookArgumentCaptor.capture());

            assertEquals(anyId, bookArgumentCaptor.getValue().getId());

        }
    }

    @Nested
    class saveBookInBatch{
        @Test
        @DisplayName("Should save batch data retrieved from csv file")
        void shouldSaveBatchDataRetrievedFromCsvFile(){
            bookService.saveBookInBatch(importedBooks);

            verify(bookRepository).saveBatchAndLinkAuthor(importedBooks);
        }
    }

}