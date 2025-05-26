package com.homelib.output.writer;

import com.homelib.entities.Book;
import com.homelib.output.formatter.BookFormatter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Only txt files",
                "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            try(BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()))){
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
        System.out.println("Nenhum arquivo selecionado. Retornando...");

    }
}
