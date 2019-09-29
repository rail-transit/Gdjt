package com.example.passenger.controller;

import com.example.passenger.entity.PlayListStyle;
import com.example.passenger.entity.StyleContent;
import com.example.passenger.entity.vo.StyleContentVo;
import com.example.passenger.service.PlayListStyleService;
import com.example.passenger.service.StyleContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RequestMapping("/styleContent")
@Controller
public class StyleContentController {

    @Autowired
    StyleContentService styleContentService;

    @Autowired
    PlayListStyleService playListStyleService;

    @RequestMapping("/getStyleContent")
    @ResponseBody
    public Map<String,Object> getStyleContent(ModelAndView mv,Integer playID){
        PlayListStyle playListStyle=playListStyleService.selectPlayListStyle(playID);
        List<StyleContentVo> styleContentVoList=styleContentService.selectStyleContentVo(playListStyle.getStyleID());
        mv.addObject("styleContentVoList",styleContentVoList);
        return mv.getModel();
    }

    @RequestMapping("/addStyleContent")
    @ResponseBody
    public Map<String,Object> addStyleContent(ModelAndView mv, StyleContent styleContent,String time){
        String[] a=time.split("\\.");
        String[] b=a[0].split(":");
        styleContent.setTimeLength(Integer.valueOf(b[0])*60*60 + Integer.valueOf(b[1])*60 + Integer.valueOf(b[2]));
        PlayListStyle playListStyle=playListStyleService.selectPlayListStyle(styleContent.getStyleID());
        styleContent.setStyleID(playListStyle.getStyleID());
        Integer i=styleContentService.addStyleContent(styleContent);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateStyleContent")
    @ResponseBody
    public Map<String,Object> updateStyleContent(ModelAndView mv, StyleContent styleContent,String time){
        String[] a=time.split("\\.");
        String[] b=a[0].split(":");
        styleContent.setTimeLength(Integer.valueOf(b[0])*60*60 + Integer.valueOf(b[1])*60 + Integer.valueOf(b[2]));
        Integer i=styleContentService.updateStyleContent(styleContent);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteStyleContent")
    @ResponseBody
    public Map<String,Object> deleteStyleContent(ModelAndView mv, Integer id){
        Integer i=styleContentService.deleteStyleContent(id);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
