package com.example.passenger.controller;

import com.example.passenger.entity.MsgFodder;
import com.example.passenger.entity.MsgLevel;
import com.example.passenger.entity.OperationLog;
import com.example.passenger.entity.Users;
import com.example.passenger.service.MsgFodderService;
import com.example.passenger.service.MsgLevelService;
import com.example.passenger.service.OperationLogService;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/msgFodder")
public class MsgFodderController {

    @Autowired
    MsgFodderService msgFodderService;
    @Autowired
    MsgLevelService msgLevelService;
    @Autowired
    OperationLogService operationLogService;


    @RequestMapping("/msgFodderManagement")
    public String msgFodderManagement(){
        return "rightContent/messagePlan/addMessage";
    }

    @RequestMapping("/realTimeMsgManagement")
    public String realTimeMsgManagement(Model model){
        return "rightContent/messagePlan/realTimeMsg";
    }

    @RequestMapping("/timingMsgManagement")
    public String timingMsgManagement(Model model){
        return "rightContent/messagePlan/timingMsg";
    }

    @RequestMapping("/getMgsLevel")
    @ResponseBody
    public Map<String,Object> getMgsLevel(ModelAndView mv,@RequestParam(defaultValue = "11")String levelCode){
        List<MsgLevel> levelList=msgLevelService.selectMsgLevelByCode(levelCode);
        mv.addObject("levelList",levelList);
        return mv.getModel();
    }


    @RequestMapping("/getFodderMsgByType")
    @ResponseBody
    public Map<String,Object> getFodderMsgByType(ModelAndView mv,@RequestParam(defaultValue = "1")
            Integer pageNum,@RequestParam(defaultValue = "0") Integer type){
        PageUtil pageUtil=msgFodderService.selectPaging(type,null,pageNum,9);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/getFodderMsgByState")
    @ResponseBody
    public Map<String,Object> getFodderMsgByState(ModelAndView mv,@RequestParam(defaultValue = "1")
            Integer pageNum,@RequestParam(defaultValue = "0") Integer type){
        PageUtil pageUtil=msgFodderService.selectPaging(type,1,pageNum,10);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/selectMsgByType")
    @ResponseBody
    public Map<String,Object> selectMsgByType(ModelAndView mv,Integer type){
        List<MsgFodder> msgFodderList=msgFodderService.selectMsgFodderByType(type);
        mv.addObject("msgFodderList",msgFodderList);
        return mv.getModel();
    }

    @RequestMapping("/updateStateFodder")
    @ResponseBody
    public Map<String,Object> updateStateFodder(ModelAndView mv,HttpSession session,
                                                Integer id,Integer state,String note){
        MsgFodder msgFodder=msgFodderService.selectMsgFodder(id);
        Users users=(Users) session.getAttribute("user");
        try {
            Integer i=msgFodderService.updateState(id,state,note);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("预案管理");
                operationLog.setContent("用户("+users.getName()+") 审核消息素材("+msgFodder.getTitle()+")!");
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/addMsgFodder")
    @ResponseBody
    public Map<String,Object> addMsgFodder(ModelAndView mv, MsgFodder msgFodder, HttpSession session){
        Users users=(Users) session.getAttribute("user");
        try {
            Integer count=msgFodderService.selectMsgFodderByTitle(msgFodder.getTitle(),
                    msgFodder.getType(),null);
            if(count>0){
                mv.addObject("result","exists");
            }else {
                //录入消息
                Integer i=msgFodderService.addMsgFodder(msgFodder);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("预案管理");
                    operationLog.setContent("用户("+users.getName()+") 添加消息素材("+msgFodder.getTitle()+")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateFodder")
    @ResponseBody
    public Map<String,Object> updateFodder(ModelAndView mv,MsgFodder msgFodder,HttpSession session){
        Users users=(Users) session.getAttribute("user");
        try {
            MsgFodder msgFodder1=msgFodderService.selectMsgFodder(msgFodder.getId());
            Integer count=msgFodderService.selectMsgFodderByTitle(msgFodder.getTitle(),
                    msgFodder1.getType(),msgFodder.getId());
            if(count>0){
                mv.addObject("result","exists");
            }else{
                Integer i=msgFodderService.updateMsgFodder(msgFodder);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("预案管理");
                    operationLog.setContent("用户("+users.getName()+") 修改消息素材("+msgFodder.getTitle()+")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }

        return mv.getModel();
    }

    @RequestMapping("/deleteFodder")
    @ResponseBody
    public Map<String,Object> deleteFodder(ModelAndView mv,Integer id,HttpSession session){
        MsgFodder msgFodder=msgFodderService.selectMsgFodder(id);
        Users users=(Users) session.getAttribute("user");
        try {
            Integer i=msgFodderService.deleteMsgFodder(id);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("预案管理");
                operationLog.setContent("用户("+users.getName()+") 删除消息素材("+msgFodder.getTitle()+")!");
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
