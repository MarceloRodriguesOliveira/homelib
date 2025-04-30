package com.homelib;

import com.homelib.entities.Book;
import com.homelib.entities.GlobalStore;
import com.homelib.repository.BookRepository;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;

@Log4j2
public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int menuOption;
        while (true){
            mainMenu();
            menuOption = Integer.parseInt(SCANNER.nextLine());
            if(menuOption == 0){break;}
            switch (menuOption){
                case 1 -> GlobalStore.getInstance().getData();
                case 2 -> BookRepository.findAllBooks();
                //default -> throw new IllegalArgumentException("Opção não existe");
            }
        }

    }

    private static void mainMenu(){
        System.out.println("1 - Listar todos os livros salvos");
        System.out.println("2. Listar todos os livros via service");
        System.out.println("0. Sair do programa");
    }
}