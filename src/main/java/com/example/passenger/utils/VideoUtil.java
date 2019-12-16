package com.example.passenger.utils;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public class VideoUtil {
    public static String width;
    public static String height;
    /**
     * 获取视频时长(时分秒)
     *
     * @param ms
     * @return
     */
    public static String ReadVideoTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
        /*if (strHour.equals("00")) {
            return strMinute + ":" + strSecond;
        } else {*/
            return strHour + ":" + strMinute + ":" + strSecond;
        //}
    }


    /**
     * 获取视频时长(毫秒)
     *
     * @param file
     * @return
     */
    public static String ReadVideoTimeMs(MultipartFile file) {
        Encoder encoder = new Encoder();
        String length = "";
        try {
            // 获取文件类型
            String fileName = file.getContentType();
            // 获取文件后缀
            String pref = fileName.indexOf("/") != -1 ? fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length()) : null;
            String prefix = "." + pref;
            // 用uuid作为文件名，防止生成的临时文件重复
            final File excelFile = File.createTempFile(UUID.randomUUID().toString(), prefix);
            // MultipartFile to File
            file.transferTo(excelFile);
            MultimediaInfo m = encoder.getInfo(excelFile);
            long ls = m.getDuration();
            length = ReadVideoTime(ls);
            if (pref.equals("vnd.dlna.mpeg-tts")){
                getVedioInfo(excelFile.getPath());
            }else{
                width=String.valueOf(m.getVideo().getSize().getWidth());
                height=String.valueOf(m.getVideo().getSize().getHeight());
            }
            //程序结束时，删除临时文件
            VideoUtil.deleteFile(excelFile);
        } catch (Exception e) {
            e.printStackTrace();
            length="-1";
        }
        return length;
    }


    public static void getVedioInfo(String filename) {
        // first we create a Xuggler container object
        IContainer container = IContainer.make();

        // we attempt to open up the container
        int result = container.open(filename, IContainer.Type.READ, null);

        // check if the operation was successful
        if (result < 0)
            return;
        // query how many streams the call to open found
        int numStreams = container.getNumStreams();
        // query for the total duration
        long duration = container.getDuration();
        // query for the file size
        //long fileSize = container.getFileSize();
        //long secondDuration = duration / 1000000;
        //System.out.println("时长：" + secondDuration + "秒");
        //System.out.println("文件大小:" + fileSize + "M");
        for (int i = 0; i < numStreams; i++) {
            IStream stream = container.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();
            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                width=String.valueOf(coder.getWidth());
                height=String.valueOf(coder.getHeight());
            }
        }
    }


    /**
     * 删除
     *
     * @param files
     */
    private static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
