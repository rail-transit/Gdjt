package com.example.passenger.controller;

import com.example.passenger.entity.PlayListStyle;
import com.example.passenger.entity.PlayStyle;
import com.example.passenger.service.PlayListStyleService;
import com.example.passenger.service.PlayStyleService;
import com.example.passenger.service.SchedulesService;
import com.example.passenger.utils.FileOperation;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RequestMapping("/playStyle")
@Controller
public class PlayStyleController {
    @Autowired
    PlayStyleService playStyleService;

    @Autowired
    PlayListStyleService playListStyleService;

    @Autowired
    SchedulesService schedulesService;

    @RequestMapping("/playStyleManagement")
    public String playStyleManagement(Model model){
        return "rightContent/playStyle/playStyle";
    }

    @RequestMapping("/selectPlayStyle")
    @ResponseBody
    public Map<String,Object> selectPlayStyle(ModelAndView mv,@RequestParam(defaultValue = "1") Integer state,
                                              String name,@RequestParam(defaultValue = "1") Integer pageNum,
                                              Integer isTemplate){
        PageUtil pageUtil=playStyleService.selectPaging(state,name,isTemplate,pageNum,2);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/auditPlayStyle")
    @ResponseBody
    public Map<String,Object> auditPlayStyle(ModelAndView mv, PlayStyle playStyle){
        Integer i=playStyleService.updatePlayStyle(playStyle);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/deletePlayStyle")
    @ResponseBody
    public Map<String,Object> deletePlayStyle(ModelAndView mv, Integer id){
        Integer i=playStyleService.deletePlayStyle(id);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/selectPlayListStyle")
    @ResponseBody
    public Map<String,Object> selectPlayListStyle(ModelAndView mv,Integer playListID,
                                                  @RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=playListStyleService.selectPaging(playListID,pageNum,2);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/getWidthHeight")
    @ResponseBody
    public Map<String,Object> getWidthHeight(ModelAndView mv,Integer playID){
        PlayListStyle playListStyle=playListStyleService.selectPlayListStyle(playID);
        PlayStyle playStyle=playStyleService.selectPlayStyle(playListStyle.getStyleID());
        String width=playStyle.getContentText().substring(
                playStyle.getContentText().indexOf("<width>")+7,playStyle.getContentText().indexOf("</width>"));
        String height=playStyle.getContentText().substring(
                playStyle.getContentText().indexOf("<height>")+8,playStyle.getContentText().indexOf("</height>"));
        mv.addObject("width",width);
        mv.addObject("height",height);
        return mv.getModel();
    }

    @RequestMapping("/preview")
    @ResponseBody
    public Map<String,Object> preview(ModelAndView mv,Integer styleID){
        String playStyleContent=schedulesService.getProgramById(styleID);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String parameterOne=playStyleContent.substring(playStyleContent.indexOf("<root>")+6,
                playStyleContent.indexOf("</root>"));
        String parameterTwo="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root>\r\n" +
                "<schedule>\r\n" +
                "<id>2021</id>\r\n" +
                "<type>1</type>\r\n" +
                "<program>\r\n" +
                "<test>\r\n" +
                "<id>2067</id>\r\n" +
                "<name>测试</name>\r\n" +
                "<PlayType>11:59:59</PlayType>\r\n" +
                "<description/>\r\n" +
                "</program>\r\n" +
                "<clientid/>\r\n" +
                "<summary/>\r\n" +
                "<color>0xFF0000</color>\r\n" +
                "<description/>\r\n" +
                "<modifydt>"+sdf.format(new Date())+"</modifydt>\r\n" +
                "<startcron/>\r\n" +
                "<endcron/>\r\n" +
                "<cronexpression/>\r\n" +
                "<executeexpression/>\r\n" +
                "<allday>0</allday>\r\n" +
                "<timelength/>\r\n" +
                "<start>"+sdf.format(new Date())+"</start>\r\n" +
                "<end>2099-12-30 23:00:00</end>\r\n" +
                "<oldStart>"+sdf.format(new Date())+"</oldStart>\r\n" +
                "<oldEnd>2099-12-30 23:00:00</oldEnd>\r\n" +
                "</schedule>\r\n" +
                "<url></url>\r\n" +
                "</root>";
        Process process = null;
        try {
            String content=parameterTwo.replace("<test>",parameterOne);
            FileOperation.createTxtFile(styleID.toString(),content);
            String[] cmd = {"D:\\preview_20190923\\PreView.exe","D:\\preview_20190923\\"+styleID.toString()+".xml"};
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/addPlayListStyle")
    @ResponseBody
    public Map<String,Object> addPlayListStyle(ModelAndView mv, PlayListStyle playListStyle){
        playListStyle.setSequence(1);
        playListStyle.setPlayTimes(1);
        playListStyle.setPlayType("23:59:59");
        Integer i=playListStyleService.addPlayListStyle(playListStyle);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/deletePlayListStyle")
    @ResponseBody
    public Map<String,Object> deletePlayListStyle(ModelAndView mv,Integer id){
        Integer i=playListStyleService.deletePlayListStyle(id);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }
}
