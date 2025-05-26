package com.homelib.output.io;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderHelper {
    public static void readFromFile(){
        JFileChooser chooser = FileSelector.selectFile();
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
}
