package com.homelib.utils;

import com.homelib.entities.Book;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public interface BookInputReader {
    Book readBook();
    int readId();
    String listBooks();
    int readIdDelete();
    Long readIdUpdate();
}
