package com.homelib.output.io;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelectorHelper {
    public FileSelectorHelper(){}

    public JFileChooser selectFile(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only txt and csv files", "txt", "csv");
        fileChooser.setFileFilter(filter);
        return fileChooser;
    }

}
