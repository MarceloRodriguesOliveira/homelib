package com.homelib;


import com.homelib.entities.Book;
import com.homelib.enums.FileIOOperationType;
import com.homelib.enums.MenuType;
import com.homelib.enums.OperationType;
import com.homelib.factory.BookOperationsFactory;
import com.homelib.factory.MenuFactory;

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
                case 1 -> BookOperationsFactory.getInputMenu(OperationType.LIST_BOOKS);
                case 2 -> BookOperationsFactory.getInputMenu(OperationType.SEARCH_ID);
                case 3 -> BookOperationsFactory.getInputMenu(OperationType.CREATE);
                case 4 -> BookOperationsFactory.getInputMenu(OperationType.DELETE_BOOK);
                case 5 -> BookOperationsFactory.getInputMenu(OperationType.UPDATE);
                case 6 -> BookOperationsFactory.getIOMenu(FileIOOperationType.CSV_READ);
                case 7 -> BookOperationsFactory.getIOMenu(FileIOOperationType.CSV_EXPORT);
                case 8 -> BookOperationsFactory.getIOMenu(FileIOOperationType.CSV_IMPORT);
            }
        }

    }

}