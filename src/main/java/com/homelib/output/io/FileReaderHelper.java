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

    public static void readListFromCsv(){
        List<Book> bookList_CSV = new ArrayList<Book>();
        JFileChooser chooser = FileSelectorHelper.selectFile();
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION){
            try(BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()))){
                String line;
                while ((line = reader.readLine()) != null){
                    bookList_CSV.add(BookFormatter.getCsvValueAsBook(line));
                }
                BookFormatter.formattedBookListFromCsv(bookList_CSV);
                return;
            }catch (FileNotFoundException e){
                System.out.println("File not found");

            }catch (IOException e){
                System.out.println("Error while acessing file");

            }
        }

    }
}
