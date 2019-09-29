package com.example.passenger.service;

import com.example.passenger.entity.*;
import com.example.passenger.mapper.*;
import com.example.passenger.utils.FtpFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulesService {
    @Autowired
    PlayListMapper playListMapper;

    @Autowired
    PlayListStyleMapper playListStyleMapper;

    @Autowired
    PlayStyleMapper playStyleMapper;

    @Autowired
    StyleContentMapper styleContentMapper;

    @Autowired
    FodderMapper fodderMapper;

    public String getScheduleById(Integer idxs){
        PlayList playList=playListMapper.selectPlayList(idxs);
        PlayListStyle playListStyles=playListStyleMapper.selectPlayListStyle(playList.getId());
        String listContent=playList.getContentText();
        String styleContent="</type>\r\n" +
                "<program>\r\n" +
                "<id>"+playListStyles.getStyleID()+"</id>\r\n" +
                "<name>"+playListStyles.getStyleName()+"</name>\r\n" +
                "<PlayType>"+playListStyles.getPlayType()+"</PlayType>\r\n" +
                "<description/>\r\n" +
                "</program>\r\n"+
                "<clientid/>\r\n";
        String contentNumber=playList.getContentText().substring( listContent.indexOf("</type>"),
                playList.getContentText().indexOf("<summary/>"));
        String contentIndex=listContent.replace(contentNumber,styleContent);

        String indexOne=contentIndex.substring(contentIndex.indexOf("</schedule>"),
                contentIndex.indexOf("</root>"));
        String indexTwo="</schedule>\r\n" +
                "<url>http://10.1.9.168:8080/schedules/getProgramById?idxs=</url>\r\n";
        String content=contentIndex.replace(indexOne,indexTwo);
        return content;
    }

    public String getProgramById(Integer idxs){
        //根据id获取版式信息
        PlayStyle playStyle=playStyleMapper.selectPlayStyle(idxs);

        //根据版式xml拆分layout存入数组
        String[] array=playStyle.getContentText().split("</layout>");
        //初始化结果集合
        List<String> contentList=new ArrayList<>();
        for (int i=0;i<array.length;i++){
            if (i!=array.length-1){
                //截取layout头部信息
                String layout=array[i].substring(array[i].indexOf("<layout>"),
                        array[i].indexOf("<layoutname>"));
                //获取截取layoutID
                String layoutID=layout.substring(layout.indexOf("<id>")+4,layout.indexOf("</id>"));
                //根据版式id以及layoutID查询控件类容信息
                List<StyleContent> styleContentList=
                        styleContentMapper.selectStyleContent(playStyle.getId(),layoutID);

                //初始化StringBuffer用于存储类容
                StringBuffer contentBuffer=new StringBuffer(layout);
                //循环类容list
                for (StyleContent styleContent:styleContentList){
                    //根据类容信息的素材id查询对象的素材信息
                    Fodder fodder=fodderMapper.selectFodderByID(styleContent.getMaterialID());
                    //用字符串拼接为xml格式数据块
                    String contentTwo="<contents>\r\n" +
                            "<id>"+styleContent.getContentID()+"</id>\r\n" +
                            "<materialid>"+styleContent.getMaterialID()+"</materialid>\r\n" +
                            "<fileproterty>"+styleContent.getFileproterty()+"</fileproterty>\r\n" +
                            "<fsize>"+fodder.getSize()+"</fsize>\r\n" +
                            "<timelength>"+styleContent.getTimeLength()+"</timelength>\r\n" +
                            "<playtimes>"+styleContent.getPlaytimes()+"</playtimes>\r\n" +
                            "<contentsname>"+fodder.getName()+"</contentsname>\r\n" +
                            "<getcontents>\r\n" +
                            ""+fodder.getPath()+"" +
                            "</getcontents>\r\n" +
                            "</contents>\r\n";
                    //获取文件名
                    String fileName=fodder.getPath().substring(fodder.getPath().lastIndexOf("/")+1);
                    FtpFileUtil.downloadFile(fileName);
                    contentBuffer.append(contentTwo);
                }
                contentList.add(array[i].replace(layout,contentBuffer)+"</layout>");
            }else{
                contentList.add(array[i]);
            }
        }
        String content=String.join("",contentList);
        return content;
    }
}
