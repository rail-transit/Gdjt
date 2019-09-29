package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.service.*;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RequestMapping("/playList")
@Controller
public class PlayListController {

    @Autowired
    PlayListService playListService;

    @Autowired
    MsgLevelService msgLevelService;

    @Autowired
    FodderService fodderService;

    @Autowired
    PlayStyleService playStyleService;

    @Autowired
    PlayListClientService playListClientService;

    @Autowired
    PlayListStyleService playListStyleService;

    @Autowired
    PlayListDownloadStatusService playListDownloadStatusService;

    @RequestMapping("/planManagement")
    public String planManagement(Model model){
        List<MsgLevel> msgLevelList=msgLevelService.selectMsgLevelAll();
        model.addAttribute("msgLevelList",msgLevelList);
        return "rightContent/messagePlan/plan";
    }


    @RequestMapping("/playReleaseManagement")
    public String playReleaseManagement(Model model){
        List<MsgLevel> msgLevelList=msgLevelService.selectMsgLevelAll();
        model.addAttribute("msgLevelList",msgLevelList);
        return "rightContent/messagePlan/playRelease";
    }

    @RequestMapping("/playState")
    public String playState(){
        return "rightContent/messagePlan/playState";
    }

    @RequestMapping("/playListManagement")
    public String playListManagement(){
        return "rightContent/playStyle/playList";
    }


    @RequestMapping("/releasePlayList")
    public String releasePlayList(Model model){
        return "rightContent/playStyle/releasePlayList";
    }

    @RequestMapping("/selectPlayList")
    @ResponseBody
    public Map<String,Object> selectPlayList(ModelAndView mv,String timeLength,
                                             @RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=playListService.selectPaging(null,timeLength,pageNum,2);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }


    @RequestMapping("/getPlayList")
    @ResponseBody
    public Map<String,Object> getPlayList(ModelAndView mv,Integer id){
        PlayList playList=playListService.selectPlayList(id);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(playList.getStartDate(), df);
        LocalDateTime endDateTime = LocalDateTime.parse(playList.getStartDate(), df);
        playList.setStartDate(startDateTime.toString());
        playList.setEndDate(endDateTime.toString());
        mv.addObject("playList",playList);
        return mv.getModel();
    }

    @RequestMapping("/getReleasePlayList")
    @ResponseBody
    public Map<String,Object> getReleasePlayList(ModelAndView mv,@RequestParam(defaultValue = "1")
            Integer pageNum){
        PageUtil pageUtil=playListService.selectPaging(1,"",pageNum,2);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/addPlayList")
    @ResponseBody
    public Map<String,Object> addPlayList(HttpSession session,ModelAndView mv,PlayList playList){
        //获取用户信息
        Users users=(Users)session.getAttribute("user");
        Integer i=playListService.addPlayList(playList,users);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/addPlan")
    @ResponseBody
    public Map<String,Object> addPlan(HttpSession session, ModelAndView mv, PlayList playList,
                                      String width,String height){
        //获取用户信息
        Users users=(Users)session.getAttribute("user");

        //执行操作
        Integer i=playListService.addMultimedia(playList,users,width,height);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }


    @RequestMapping("/updatePlay")
    @ResponseBody
    public Map<String,Object> updatePlay(ModelAndView mv,PlayList playList,Integer width,Integer height){
        Integer i=playListService.updatePlayList(playList);
        PlayListStyle playListStyle=playListStyleService.selectPlayListStyle(playList.getId());
        PlayStyle playStyle=playStyleService.selectPlayStyle(playListStyle.getStyleID());
        String contentOne=playStyle.getContentText().substring(
                playStyle.getContentText().indexOf("<width>"),playStyle.getContentText().indexOf("<fullimage"));
        String contentTwo="<width>"+width+"</width>\n" +
                "<height>"+height+"</height>";
        String contentNum=playStyle.getContentText().replace(contentOne,contentTwo);

        String contentThree=contentNum.substring( contentNum.indexOf("</layoutname>")+13,
                contentNum.indexOf("<x"));
        String content=contentNum.replace(contentThree,contentTwo);
        if(i>0){
            Integer j=playStyleService.updatePlayStyleContent(playStyle.getId(),content);
            if(j>0){
                mv.addObject("result","成功");
            }else{
                mv.addObject("result","失败");
            }
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }



    @RequestMapping("/deletePlay")
    @ResponseBody
    public Map<String,Object> deletePlay(ModelAndView mv,Integer id){
        Integer i=playListService.deletePlayList(id);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/auditPlayList")
    @ResponseBody
    public Map<String,Object> auditPlayList(ModelAndView mv,PlayList playList){
        Integer i=playListService.auditPlayList(playList);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

}
