package com.homelib;


import com.homelib.enums.MenuType;
import com.homelib.enums.OperationType;
import com.homelib.factory.BookInputFactory;
import com.homelib.factory.MenuFactory;

import com.homelib.service.BookService;
import com.homelib.utils.BookOperations;
import com.homelib.utils.Menu;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;


@Log4j2
public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int menuOption;
        Menu menu = MenuFactory.getMenu(MenuType.MAIN);
        while (true){
            menu.options();
            menuOption = Integer.parseInt(SCANNER.nextLine());
            if(menuOption == 0){break;}
            switch (menuOption){
                case 1 -> BookInputFactory.getMenu(OperationType.LIST_BOOKS);
                case 2 -> BookInputFactory.getMenu(OperationType.SEARCH_ID);
                case 3 -> BookInputFactory.getMenu(OperationType.CREATE);
                case 4 -> BookInputFactory.getMenu(OperationType.DELETE_BOOK);
                case 5 -> BookInputFactory.getMenu(OperationType.UPDATE);
                case 6 -> BookInputFactory.getMenu(OperationType.EXPORT);
            }
        }

    }

}