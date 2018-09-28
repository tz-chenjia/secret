package cn.tz.chenjia.ui;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileChooser {

    private static final Logger log = Logger.getLogger(FileChooser.class);

    public static File fileChooser() {
        File file = null;
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("."));
        //是否可多选
        fc.setMultiSelectionEnabled(false);
        //选择模式，可选择文件和文件夹
        // fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // 设置是否显示隐藏文件
        fc.setFileHidingEnabled(false);
        fc.setAcceptAllFileFilterUsed(false);
        //设置文件筛选器
        fc.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".sql") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "*.sql";
            }
        });

        int returnValue = fc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }
        return file;
    }

}
