package com.example.passenger.service;

import com.example.passenger.entity.*;
import com.example.passenger.mapper.*;
import com.example.passenger.utils.IPUtil;
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
    PlayListClientService playListClientService;
    @Autowired
    PlayStyleMapper playStyleMapper;
    @Autowired
    StyleContentMapper styleContentMapper;
    @Autowired
    FodderMapper fodderMapper;
    @Autowired
    DeviceService deviceService;

    public String getScheduleById(Integer clientid, Integer idxs) {
        StringBuffer stringBuffer = new StringBuffer();
        Device device = deviceService.selectDevice(clientid);
        PlayList playList = playListMapper.selectPlayList(idxs);
        if (playList != null) {
            List<PlayListStyle> playListStyles = playListStyleMapper.getPlayListStyle(playList.getId());
            StringBuffer styleBuffer = new StringBuffer();
            for (PlayListStyle playListStyle : playListStyles) {
                String styleContent = "<program>" +
                        "<id>" + playListStyle.getStyleID() + "</id>" +
                        "<name>" + playListStyle.getStyleName() + "</name>" +
                        "<PlayType>" + playListStyle.getPlayType() + "</PlayType>" +
                        "<description/>" +
                        "</program>";
                styleBuffer.append(styleContent);
            }
            String path = "<url>http://" + IPUtil.IP + ":8080/schedules/getProgramById?idxs=</url>";
            stringBuffer.append(playList.getContentText());
            stringBuffer.replace(stringBuffer.indexOf("<start>") + 7,
                    stringBuffer.indexOf("</start>"), playList.getStartDate());
            stringBuffer.replace(stringBuffer.indexOf("<end>") + 5,
                    stringBuffer.indexOf("</end>"), playList.getEndDate());
            stringBuffer.replace(stringBuffer.indexOf("<type>") + 6,
                    stringBuffer.indexOf("</type>"), playList.getType().toString());
            if (device.getIsBackups().equals("1")) {
                PlayListClient playListClient = playListClientService.selectClientByPlayListID(idxs, clientid);
                if (playListClient != null) {
                    Integer clientID = null;
                    List<PlayListClient> playListClientList = playListClientService.previousRelease(idxs, clientid, playListClient.getAuditTime());
                    for (PlayListClient playListClient1 : playListClientList) {
                        clientID = playListClient1.getClientID();
                    }
                    stringBuffer.insert(stringBuffer.indexOf("</type>") + 7, "<clientid>" + clientID + "</clientid>");
                }
            } else {
                stringBuffer.insert(stringBuffer.indexOf("</type>") + 7, "<clientid/>");
            }
            stringBuffer.insert(stringBuffer.indexOf("</oldEnd>") + 9, styleBuffer);
            stringBuffer.insert(stringBuffer.indexOf("</schedule>") + 11, path);
        }
        return stringBuffer.toString();
    }

    public String getProgramById(Integer idxs) {
        //根据id获取版式信息
        PlayStyle playStyle = playStyleMapper.selectPlayStyle(idxs);
        String content = "";
        try {
            //根据版式xml拆分layout存入数组
            String[] array = playStyle.getContentText().split("</layout>");
            //初始化结果集合
            List<String> contentList = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                if (i != array.length - 1) {
                    //截取layout头部信息
                    String layout = array[i].substring(array[i].indexOf("<layout>"),
                            array[i].indexOf("<layoutname>"));
                    //获取截取layoutID
                    String layoutID = layout.substring(layout.indexOf("<id>") + 4, layout.indexOf("</id>"));
                    //根据版式id以及layoutID查询控件类容信息
                    List<StyleContent> styleContentList =
                            styleContentMapper.selectStyleContent(playStyle.getId(), layoutID);

                    //初始化StringBuffer用于存储类容
                    StringBuffer contentBuffer = new StringBuffer(layout);
                    //循环类容list
                    for (StyleContent styleContent : styleContentList) {
                        if (styleContent.getMaterialID() != -1) {
                            //根据类容信息的素材id查询对象的素材信息
                            Fodder fodder = fodderMapper.selectFodderByID(styleContent.getMaterialID());
                            if (fodder != null) {
                                //用字符串拼接为xml格式数据块
                                String contentTwo = "<contents>" +
                                        "<id>" + styleContent.getContentID() + "</id>" +
                                        "<materialid>" + styleContent.getMaterialID() + "</materialid>" +
                                        "<fileproterty>" + styleContent.getFileproterty() + "</fileproterty>" +
                                        "<fsize>" + fodder.getSize() + "</fsize>" +
                                        "<timelength>" + styleContent.getTimeLength() + "</timelength>" +
                                        "<playtimes>" + styleContent.getPlaytimes() + "</playtimes>" +
                                        "<contentsname>" + fodder.getName() + "</contentsname>" +
                                        "<getcontents>" + fodder.getPath() + "</getcontents>" +
                                        "</contents>";
                                contentBuffer.append(contentTwo);
                            }
                        } else {
                            contentBuffer.append(styleContent.getGetcontents());
                        }
                    }
                    contentList.add(array[i].replace(layout, contentBuffer) + "</layout>");
                } else {
                    contentList.add(array[i]);
                }
            }
            content = String.join("", contentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /*public String getPlayerFile(Integer idxs){
        //根据id获取版式信息
        PlayStyle format=playStyleMapper.selectPlayStyle(idxs);
        String content="";
        try {
            //根据版式xml拆分layout存入数组
            String[] array=format.getContentText().split("</layout>");
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
                            styleContentMapper.selectStyleContent(format.getId(),layoutID);

                    //初始化StringBuffer用于存储类容
                    StringBuffer contentBuffer=new StringBuffer(layout);
                    //循环类容list
                    for (StyleContent styleContent:styleContentList){
                        if (styleContent.getMaterialID()!=-1){
                            //根据类容信息的素材id查询对象的素材信息
                            Fodder fodder=fodderMapper.selectFodderByID(styleContent.getMaterialID());
                            //用字符串拼接为xml格式数据块
                            String contentTwo="<contents>" +
                                    "<id>"+styleContent.getContentID()+"</id>" +
                                    "<materialid>"+styleContent.getMaterialID()+"</materialid>" +
                                    "<fileproterty>"+styleContent.getFileproterty()+"</fileproterty>" +
                                    "<fsize>"+fodder.getSize()+"</fsize>" +
                                    "<timelength>"+styleContent.getTimeLength()+"</timelength>" +
                                    "<playtimes>"+styleContent.getPlaytimes()+"</playtimes>" +
                                    "<contentsname>"+fodder.getName()+"</contentsname>" +
                                    "<getcontents>"+fodder.getPath()+"</getcontents>" +
                                    "</contents>";
                            //获取文件名
                            String fileName=fodder.getPath().substring(fodder.getPath().lastIndexOf("/")+1);
                            FtpFileUtil.downloadFile(fileName);
                            contentBuffer.append(contentTwo);
                        }else{
                            contentBuffer.append(styleContent.getGetcontents());
                        }
                    }
                    contentList.add(array[i].replace(layout,contentBuffer)+"</layout>");
                }else{
                    contentList.add(array[i]);
                }
            }
            content=String.join("",contentList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return content;
    }*/
}
