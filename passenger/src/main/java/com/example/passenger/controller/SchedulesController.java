package com.example.passenger.controller;

import com.example.passenger.service.SchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/schedules")
@RestController
public class SchedulesController {
    @Autowired
    SchedulesService schedulesService;

    @GetMapping(value = "/getScheduleById",produces = MediaType.APPLICATION_XML_VALUE)
    public String getScheduleById(Integer clientid, Integer idxs){
        /*PlayList playList=playListService.selectPlayList(idxs);
        PlayListStyle playListStyles=playListStyleService.selectPlayListStyle(playList.getId());
        String listContent=playList.getContentText();
        String styleContent="</type>\n" +
                "<program>\n" +
                "<id>"+playListStyles.getStyleID()+"</id>\n" +
                "<name>"+playListStyles.getStyleName()+"</name>\n" +
                "<PlayType>"+playListStyles.getPlayType()+"</PlayType>\n" +
                "<description/>\n" +
                "</program>\n"+
                "<clientid/>\n";
        String contentNumber=playList.getContentText().substring( listContent.indexOf("</type>"),
                playList.getContentText().indexOf("<summary/>"));
        String contentIndex=listContent.replace(contentNumber,styleContent);

        String indexOne=contentIndex.substring(contentIndex.indexOf("</schedule>"),
                contentIndex.indexOf("</root>"));
        String indexTwo="</schedule>\n" +
                "<url>http://10.1.9.168:8080/schedules/getProgramById?idxs=</url>\n";
        String content=contentIndex.replace(indexOne,indexTwo);*/
        String content=schedulesService.getScheduleById(idxs);
        return content;
    }

    @GetMapping(value = "/getProgramById",produces = MediaType.APPLICATION_XML_VALUE)
    public String getProgramById(Integer idxs){
        /*PlayStyle playStyle=playStyleService.selectPlayStyle(idxs);
        List<StyleContent> styleContentList=styleContentService.selectStyleContent(playStyle.getId());

        String layContent=playStyle.getContentText().substring(playStyle.getContentText().indexOf("<layout>"),
                playStyle.getContentText().indexOf("<layoutname>"));

        String contentNumber=playStyle.getContentText();

        String contentOne=contentNumber.substring(contentNumber.indexOf("<layout>"),
                contentNumber.indexOf("<layoutname>"));

        StringBuffer contentBuffer=new StringBuffer(layContent);
        for (StyleContent styleContent:styleContentList){
            Fodder fodder=fodderService.selectFodderByID(styleContent.getMaterialID());
            String contentTwo="<contents>\n" +
                    "<id>"+styleContent.getContentID()+"</id>\n" +
                    "<materialid>"+styleContent.getMaterialID()+"</materialid>\n" +
                    "<fileproterty>"+styleContent.getFileproterty()+"</fileproterty>\n" +
                    "<fsize>"+fodder.getSize()+"</fsize>\n" +
                    "<timelength>"+styleContent.getTimeLength()+"</timelength>\n" +
                    "<playtimes>"+styleContent.getPlaytimes()+"</playtimes>\n" +
                    "<contentsname>"+fodder.getName()+"</contentsname>\n" +
                    "<getcontents>\n" +
                    ""+fodder.getPath()+"" +
                    "</getcontents>\n" +
                    "</contents>\n";
            contentBuffer.append(contentTwo);
        }
        String content=contentNumber.replace(contentOne,contentBuffer);*/
        String content=schedulesService.getProgramById(idxs);
        return content;
    }

}
