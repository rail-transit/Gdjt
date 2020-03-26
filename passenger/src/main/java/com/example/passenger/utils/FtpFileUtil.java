package com.example.passenger.utils;

import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


@Component
public class FtpFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpFileUtil.class);

    //ftp服务器ip地址
    private static final String FTP_ADDRESS = "10.1.9.11";

    //端口号
    private static final int FTP_PORT = 21;
    //用户名
    private static final String FTP_USERNAME = "ftp";
    //密码
    private static final String FTP_PASSWORD = "123456";
    //存放路径
    private static final String FTP_BASEPATH = "/fodder/";

    // 下载的文件目录
    private static final String DOWNLOAD_DIR = "D:\\preview_20190923\\downloads";

    // ftp客户端
    private static FTPClient ftpClient = new FTPClient();
    private static String encoding = System.getProperty("file.encoding");


    public static String getPath() {
        String path = "ftp://ftp:FTPmedia@" + FTP_ADDRESS + FTP_BASEPATH;
        return path;
    }


    //文件上传
    public static boolean uploadFile(String originFileName, InputStream input) {
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
            //增大缓存区
            ftpClient.setBufferSize(1024 * 1024 * 10);
            // 设置PassiveMode传输
            ftpClient.enterLocalPassiveMode();
            // 检验是否连接成功
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                logger.warn("连接FTP服务器失败!");
                ftpClient.disconnect();
                return success;
            }
            // 转移工作目录至指定目录下
            boolean change = ftpClient.changeWorkingDirectory(FTP_BASEPATH);
            if (change) {
                success = ftpClient.storeFile(originFileName, input);
                if (success) {
                    logger.info("上传成功!");
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
    public static boolean downloadFile(HttpServletResponse response) {
        boolean success = false;
        FileInputStream in = null;
        OutputStream out = null;
        String savePath = "D:\\ftp\\2039\\1.gif";
        try {
            //获取文件名
            String filename = savePath.substring(savePath.lastIndexOf("\\") + 1);
            filename = new String(filename.getBytes("iso8859-1"), "UTF-8");

            String downloadpath = savePath;
            //File file = new File(path1);
            //如果文件不存在
			/*if(!file.exists()){
			    return false;
			}*/
            //设置响应头，控制浏览器下载该文件
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            //读取要下载的文件，保存到文件输入流
            in = new FileInputStream(downloadpath);
            //创建输出流
            out = response.getOutputStream();
            //缓存区
            byte buffer[] = new byte[1024];
            int len = 0;
            //循环将输入流中的内容读取到缓冲区中
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            success = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //删除服务器上的临时文件
        File deleteFile = new File(savePath);
        deleteFile.delete();
        return success;
    }


}
