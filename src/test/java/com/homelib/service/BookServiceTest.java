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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    BookInputReader bookInputReader;

    @InjectMocks
    private BookService bookService;

    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;

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
            doReturn(book).when(bookRepository).save(bookArgumentCaptor.capture());

            //Act
            var output = bookService.createNewBook(book);


            //Assert
            assert


        }



    }

    @Test
    void findBookByName() {
    }

}