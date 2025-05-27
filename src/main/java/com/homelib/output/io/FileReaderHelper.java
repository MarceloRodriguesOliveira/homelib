package com.homelib.output.io;

import com.homelib.entities.Book;
import com.homelib.output.formatter.BookFormatter;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileReaderHelper {
    public static void readFromFile(){
        JFileChooser chooser = FileSelectorHelper.selectFile();
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            try(BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()))){
                String line;
                while ((line = reader.readLine()) != null){
                    System.out.println(line);
                }

            }catch (FileNotFoundException e){
                System.out.println("File no found");
            } catch (IOException e) {
                System.out.println("Error while accessing file");
            }
        }
    }

    public static Optional<List<Book>> readListFromCsv(){
        List<Book> bookList_CSV = new ArrayList<Book>();
        JFileChooser chooser = FileSelectorHelper.selectFile();
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION){
            try(BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()))){
                String line;
                while ((line = reader.readLine()) != null){
                    bookList_CSV.add(BookFormatter.getCsvValueAsBook(line));
                }
                return bookList_CSV.isEmpty() ? Optional.empty() : Optional.of(bookList_CSV);
            }catch (FileNotFoundException e){
                System.out.println("File not found");

            }catch (IOException e){
                System.out.println("Error while accessing file");

            }
        }
        return Optional.empty();
    }
}
