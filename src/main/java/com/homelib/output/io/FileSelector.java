package com.homelib.output.io;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class FileSelector {
    private FileSelector(){}

    public static JFileChooser selectFile(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only txt files", "txt");
        fileChooser.setFileFilter(filter);
        return fileChooser;
    }

}
