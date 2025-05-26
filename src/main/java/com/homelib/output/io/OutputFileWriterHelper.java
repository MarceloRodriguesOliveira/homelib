package com.homelib.output.io;

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

public class OutputFileWriterHelper {
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

}
