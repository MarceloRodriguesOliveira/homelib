package com.homelib.output.io;

import com.homelib.entities.Book;
import com.homelib.output.formatter.BookFormatter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
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

    public static void exportCsv(List<Book> books){
        JFileChooser chooser = FileSelectorHelper.selectFile();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            String fullPath = chooser.getSelectedFile().getAbsolutePath() + File.separator + "exportedList.csv";
            try(FileWriter writer = new FileWriter(fullPath)){
                for(Book book: books){
                    String csvString = BookFormatter.formatAsCsv(book);
                    writer.write(csvString + "\n");
                }
            }catch (IOException e){
                System.out.println("Could not proceed with operation");
            }
        }
    }

}
