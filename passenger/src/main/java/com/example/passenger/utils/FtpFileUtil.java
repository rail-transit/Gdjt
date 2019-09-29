package com.example.passenger.utils;

import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;


@Component
public class FtpFileUtil {
    //ftp服务器ip地址
    private static final String FTP_ADDRESS = "127.0.0.1";
    //端口号
    private static final int FTP_PORT = 21;
    //用户名
    private static final String FTP_USERNAME = "ftp";
    //密码
    private static final String FTP_PASSWORD = "123456";
    //存放路径
    private static final String FTP_BASEPATH = "/";

    // 下载的文件目录
    private static final String DOWNLOAD_DIR="D:\\preview_20190923\\downloads";

    // ftp客户端
    private static FTPClient ftpClient = new FTPClient();
    private static String encoding = System.getProperty("file.encoding");


    public static String getPath(){
        String path="ftp://"+FTP_ADDRESS+FTP_BASEPATH;
        return path;
    }

    private static final Logger logger = LoggerFactory.getLogger(FtpFileUtil.class);

    //文件上传
    public  static boolean uploadFile(String originFileName,InputStream input){
        boolean success = false;
        try {
            int reply;
            // 连接FTP服务器
            ftpClient.connect(FTP_ADDRESS, FTP_PORT);
            // ftpClient.connect(url,port);// 连接FTP服务器
            // 登录
            ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
            // 转码
            ftpClient.setControlEncoding(encoding);
            // 设置传输二进制文件
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置PassiveMode传输
            ftpClient.enterLocalPassiveMode();
            // 检验是否连接成功
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("连接FTP服务器失败!");
                ftpClient.disconnect();
                return success;
            }
            // 转移工作目录至指定目录下
            boolean change =ftpClient.changeWorkingDirectory(FTP_BASEPATH );
            if (change) {
                success =ftpClient.storeFile(originFileName,input);
                if (success) {
                    System.out.println("上传成功!");
                }
            }
            input.close();
            ftpClient.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

    //文件下载
    public static boolean  downloadFile(String filename) {
        boolean success = false;
        try {
            ftpClient.setControlEncoding(encoding);//转码
            ftpClient.connect(FTP_ADDRESS, FTP_PORT);// 连接FTP服务器
            ftpClient.login(FTP_USERNAME, FTP_PASSWORD);// 登录

            // 设置PassiveMode传输
            ftpClient.enterLocalPassiveMode();
            // 设置传输二进制文件
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            // 验证是否登陆成功
            if(!FTPReply.isPositiveCompletion(reply))
            {
                ftpClient.disconnect();
                System.out.println("无法连接到ftp服务器");
                return success;
            }
            // 转移到FTP服务器目录至指定的目录下
            ftpClient.changeWorkingDirectory(FTP_BASEPATH);
            // 获取文件列表
            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(filename)) {
                    File localFile = new File(DOWNLOAD_DIR + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(ff.getName(),is);
                    is.close();
                }
            }
            ftpClient.logout();
            success = true;
        } catch (FTPConnectionClosedException e) {
            logger.error("ftp连接被关闭！", e);
        } catch (Exception e) {
            logger.error("ERR : upload file "+ filename+ " from ftp : failed!", e);
        }
        return success;
    }


}
