package com.example.passenger.controller;

import com.example.passenger.entity.MsgFodder;
import com.example.passenger.entity.MsgLevel;
import com.example.passenger.service.MsgFodderService;
import com.example.passenger.service.MsgLevelService;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/msgFodder")
public class MsgFodderController {

    @Autowired
    MsgFodderService msgFodderService;

    @Autowired
    MsgLevelService msgLevelService;

    @RequestMapping("/msgFodderManagement")
    public String msgFodderManagement(){
        return "rightContent/messagePlan/addMessage";
    }

    @RequestMapping("/realTimeMsgManagement")
    public String realTimeMsgManagement(Model model){
        List<MsgLevel> msgLevelList=msgLevelService.selectMsgLevelAll();
        model.addAttribute("msgLevelList",msgLevelList);
        return "rightContent/messagePlan/realTimeMsg";
    }

    @RequestMapping("/getFodderMsgByType")
    @ResponseBody
    public Map<String,Object> getFodderMsgByType(ModelAndView mv,@RequestParam(defaultValue = "1")
            Integer pageNum,@RequestParam(defaultValue = "0") Integer type){
        PageUtil pageUtil=msgFodderService.selectPaging(type,null,pageNum,2);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/getFodderMsgByState")
    @ResponseBody
    public Map<String,Object> getFodderMsgByState(ModelAndView mv,@RequestParam(defaultValue = "1")
            Integer pageNum,@RequestParam(defaultValue = "0") Integer type){
        PageUtil pageUtil=msgFodderService.selectPaging(type,1,pageNum,2);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/updateStateFodder")
    @ResponseBody
    public Map<String,Object> updateStateFodder(ModelAndView mv,Integer id,Integer state){
        Integer i=msgFodderService.updateState(id,state);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/addMsgFodder")
    @ResponseBody
    public Map<String,Object> addMsgFodder(ModelAndView mv,MsgFodder msgFodder){
        //设置初始状态为0
        msgFodder.setState(0);
        //录入消息
        Integer i=msgFodderService.addMsgFodder(msgFodder);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateFodder")
    @ResponseBody
    public Map<String,Object> updateFodder(ModelAndView mv,MsgFodder msgFodder){
        Integer i=msgFodderService.updateMsgFodder(msgFodder);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteFodder")
    @ResponseBody
    public Map<String,Object> deleteFodder(ModelAndView mv,Integer id){
        Integer i=msgFodderService.deleteMsgFodder(id);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }
}
