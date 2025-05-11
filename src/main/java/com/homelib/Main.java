package com.homelib;

import com.homelib.entities.Book;
import com.homelib.entities.GlobalStore;
import com.homelib.enums.MenuType;
import com.homelib.factory.MenuFactory;
import com.homelib.repository.BookRepository;
import com.homelib.service.BookService;
import com.homelib.utils.Menu;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;
import java.util.UUID;

@Log4j2
public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int menuOption;
        Menu menu = MenuFactory.getMenu(MenuType.MAIN);
        UUID uuid = UUID.randomUUID();
        Book book = Book.BookBuilder
                .builder()
                .title("Dune")
                .firstNameAuthor("Frank")
                .lastNameAuthor("Herbert")
                .year(1966)
                .edition(1)
                .build();
        BookService.save(book);
        while (true){
            menu.options();
            menuOption = Integer.parseInt(SCANNER.nextLine());
            if(menuOption == 0){break;}
            switch (menuOption){
                case 1 -> BookService.findAll();
                case 2 -> BookService.findById();
                case 3 -> BookService.createNewBook();
                case 4 -> BookService.deleteBookById();
            }
        }

    }

}