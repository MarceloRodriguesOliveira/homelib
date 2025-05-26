package com.homelib.output.writer;

import com.homelib.entities.Book;
import com.homelib.output.formatter.BookFormatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Writer {
    public static void exportToTxt(List<Book> bookList){
        try(FileWriter writer = new FileWriter("C:\\Users\\Marcelo\\Desktop\\DEV\\ExportedListTest.txt")){
            for (Book book:bookList){
                String fmString = BookFormatter.toExport(book);
                writer.write(fmString + "\n");
            }
            System.out.println("File has been Written");

        }catch (IOException e){
            System.out.println("Could not write file");

        }
    }

    public static void readFromFile(){
        String filePath = ("C:\\Users\\Marcelo\\Desktop\\DEV\\ExportedListTest.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }

        }catch (FileNotFoundException e){
            System.out.println("File not found");

        } catch (IOException e) {
            System.out.println("Erro ao acessar arquivo");
        }
    }
}
