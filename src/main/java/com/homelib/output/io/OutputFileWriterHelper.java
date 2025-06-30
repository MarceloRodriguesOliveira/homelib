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
    private final FileSelectorHelper fileSelectorHelper;
    public OutputFileWriterHelper(FileSelectorHelper fileSelectorHelper){
        this.fileSelectorHelper = fileSelectorHelper;
    }

    public void exportCsv(List<Book> books){
        JFileChooser chooser = fileSelectorHelper.selectFile();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            String fullPath = chooser.getSelectedFile().getAbsolutePath() + File.separator + "exportedList.csv";
            try(FileWriter writer = new FileWriter(fullPath)){
                /*for(Book book: books){
                    String csvString = BookFormatter.formatAsCsv(book);
                    writer.write(csvString + "\n");
                }*/
            }catch (IOException e){
                System.out.println("Could not proceed with operation");
            }
        }
    }

}
