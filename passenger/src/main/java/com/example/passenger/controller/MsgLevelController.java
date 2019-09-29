package com.example.passenger.controller;

import com.example.passenger.entity.MsgLevel;
import com.example.passenger.service.MsgLevelService;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/msgLevel")
public class MsgLevelController {
    @Autowired
    MsgLevelService msgLevelService;

    @RequestMapping("/msgLevelManagement")
    public String msgLevelManagement(){
        return "rightContent/messagePlan/priority";
    }

    @RequestMapping("/getMsgLevel")
    @ResponseBody
    public Map<String,Object> getMsgLevel(ModelAndView mv,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=msgLevelService.selectPaging(pageNum,2);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/addMsgLevel")
    @ResponseBody
    public Map<String,Object> addMsgLevel(ModelAndView mv){
        MsgLevel msgLevel=new MsgLevel();
        msgLevel.setLevel(0);
        msgLevel.setLevelCode("test");
        msgLevel.setTitle("test");
        msgLevel.setNote("test");
        msgLevelService.addMsgLevel(msgLevel);
        return mv.getModel();
    }

    @RequestMapping("/updateMsgLevel")
    @ResponseBody
    public Map<String,Object> updateMsgLevel(ModelAndView mv,MsgLevel msgLevel){
        Integer i=msgLevelService.updateMsgLevel(msgLevel);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteMsgLevel")
    @ResponseBody
    public Map<String,Object> deleteMsgLevel(ModelAndView mv,Integer id){
        Integer i=msgLevelService.deleteMsgLevel(id);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

}
