package com.example.passenger.utils;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperation {
    // 新建文件
    // 输入：新建文件路径
    public static boolean createTxtFile(String fileName,String content) {
        boolean flag = false;
        try {
            File newfile = new File("D:\\preview_20190923\\"+fileName+".xml");
            if (!newfile.exists()) {
                newfile.createNewFile();
                FileWriter fwriter = null;
                try {
                    // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
                    fwriter = new FileWriter("D:\\preview_20190923\\" + fileName + ".xml", false);
                    fwriter.write(content);
                    flag = true;
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        fwriter.flush();
                        fwriter.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("文件创建失败！" + e);
        }
        return flag;
    }
}
