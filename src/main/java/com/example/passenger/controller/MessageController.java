package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.service.*;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MsgSend;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/message")
@Controller
public class MessageController {

    @Autowired
    MessageService messageService;
    @Autowired
    MsgLevelService msgLevelService;
    @Autowired
    LineService lineService;
    @Autowired
    StationService stationService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    OperationLogService operationLogService;
    @Autowired
    MsgSend msgSend;

    @RequestMapping("/auditManagement")
    public String auditManagement(){
        return "rightContent/messagePlan/auditMessage";
    }

    @RequestMapping("/getMessage")
    @ResponseBody
    public Map<String,Object> getMessage(ModelAndView mv, @RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "0") Integer state,
                                         @RequestParam(defaultValue = "") String msg){
        PageUtil pageUtil=messageService.selectPaging(state,msg,0,pageNum,10);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/historyManagement")
    public String historyManagement(Model model, @RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=messageService.selectPaging(null,null,-1,pageNum,14);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/messagePlan/historyMessage";
    }

    @RequestMapping("/broadcastMessageManagement")
    public String broadcastMessageManagement(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=messageService.selectPaging(1,null,1,pageNum,14);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/messagePlan/broadcastMessage";
    }

    @RequestMapping("/waitBoradcastMessage")
    public String waitBoradcastMessage(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=messageService.selectPaging(1,null,2,pageNum,14);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/messagePlan/waitBoradcastMessage";
    }

    @RequestMapping("/releaseStatistics")
    public String releaseStatistics(Model model){
        List<Line> lineList=lineService.selectAllLine();
        model.addAttribute("lineList",lineList);
        return "rightContent/reportForm/release";
    }

    @RequestMapping("/selectStatistics")
    @ResponseBody
    public Map<String,Object> selectStatistics(@RequestParam(defaultValue = "1") Integer pageNum,
                                               ModelAndView mv,Integer lineID,Integer stationID,
                                               Integer deviceID,String startDate,String endDate){
        try {
            PageUtil pageUtil=messageService.messageStatistics(lineID,stationID,deviceID,
                    startDate,endDate,pageNum,10);
            mv.addObject("pageUtil",pageUtil);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/revokeMessage")
    @ResponseBody
    public Map<String,Object> revokeMessage(ModelAndView mv,Integer id){
        try {
            Message message=messageService.selectMessage(id);
            String content="PMSG:<MSG>" +
                    "<Type>"+message.getType()+"</Type>" +
                    "<Info>" +
                    "<ID>"+message.getId()+"</ID>" +
                    "<CtrlID>"+message.getCtrlID()+"</CtrlID>" +
                    "<Level>"+message.getLevel()+"</Level>" +
                    "<State>0</State>" +
                    "<Text>"+message.getMsg()+"</Text>" +
                    "</Info></MSG>";
            Device device=deviceService.selectDevice(message.getDeviceID());
            long ip= IPUtil.ipToLong(device.getIp());
            msgSend.sendMsg("pisplayer.occ."+ip,content);
            Integer i=messageService.updateMessage(message.getId(),null,-1);
            if(i>0){
                Integer level=messageService.getMaxLevel(message.getDeviceID());
                if(level!=null){
                    MsgLevel msgLevel=msgLevelService.selectMsgLevelByLevel(null,level);
                    Message message1=messageService.getMessageByLevel(message.getDeviceID(),msgLevel.getLevelCode());
                    messageService.updateMessage(message1.getId(),null,1);
                }
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

    @RequestMapping("/addQueueMessage")
    @ResponseBody
    public Map<String,Object> addQueueMessage(HttpSession session, ModelAndView mv, Message message){
        Users users=(Users)session.getAttribute("user");
        try {
            message.setOperator(users.getName());
            Integer i=messageService.addMessage(message);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("预案管理");
                operationLog.setContent("用户("+users.getName()+") 添加消息("+message.getMsg()+")!");
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

    @RequestMapping("/sendMessage")
    @ResponseBody
    public Map<String,Object> sendMessage(HttpSession session,ModelAndView mv,Message message){
        //获取操作用户
        Users users=(Users)session.getAttribute("user");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        try {
            //设置实体值
            message.setOperator(users.getName());
            message.setStartDate(sdfDate.format(new Date()));
            message.setStartTime(sdfTime.format(new Date()));
            message.setEndDate("");
            message.setEndTime("");
            Integer i=messageService.addMessage(message);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("预案管理");
                operationLog.setContent("用户("+users.getName()+") 立即发送消息("+message.getMsg()+")!");
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

    @RequestMapping("/auditMessage")
    @ResponseBody
    public Map<String,Object> auditMessage(ModelAndView mv,HttpSession session,Integer id,Integer state){
        Message message=messageService.selectMessage(id);
        Users user=(Users) session.getAttribute("user");
        try {
            Integer i=messageService.updateMessage(id,state,null);
            if (i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(user.getId().toString());
                operationLog.setType("预案管理");
                operationLog.setContent("用户("+user.getName()+") 审核消息("+message.getMsg()+")!");
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


    @RequestMapping("/deleteAllMessage")
    @ResponseBody
    public Map<String,Object> deleteAllMessage(ModelAndView mv,HttpSession session,@RequestBody ArrayList<Integer> list){
        Users user=(Users) session.getAttribute("user");
        try {
            for (Integer id:list){
                Message message=messageService.selectMessage(id);
                Integer i=messageService.deleteMessage(id);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("预案管理");
                    operationLog.setContent("用户("+user.getName()+") 删除历史消息("+message.getMsg()+")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result","success");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteMessage")
    @ResponseBody
    public Map<String,Object> deleteMessage(ModelAndView mv,HttpSession session,Integer id){
        Users user=(Users) session.getAttribute("user");
        Message message=messageService.selectMessage(id);
        try {
            Integer i=messageService.deleteMessage(id);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(user.getId().toString());
                operationLog.setType("预案管理");
                operationLog.setContent("用户("+user.getName()+") 删除历史消息("+message.getMsg()+")!");
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

    @RequestMapping("/getRelease")
    @ResponseBody
    public Map<String,Object> getRelease(ModelAndView mv,Integer lineID,Integer stationID,
                                         Integer deviceID,String startDate,String endDate){
        try {
            List<Map<String,String>> messageVoList=messageService.getRelease(lineID,stationID,deviceID,
                    startDate,endDate);
            mv.addObject("messageVoList",messageVoList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }
}
