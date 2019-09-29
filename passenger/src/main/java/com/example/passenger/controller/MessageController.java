package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.service.DeviceService;
import com.example.passenger.service.LineService;
import com.example.passenger.service.MessageService;
import com.example.passenger.service.StationService;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MsgSend;
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

@RequestMapping("/message")
@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    MsgSend msgSend;

    @RequestMapping("/auditManagement")
    public String auditManagement(Model model, @RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=messageService.selectPaging(null,pageNum,2);
        List<Line> lineList=lineService.selectAllLine();
        List<Station> stationList=stationService.queryAllStation();
        List<Device> deviceList=deviceService.queryAllDevice();
        model.addAttribute("deviceList",deviceList);
        model.addAttribute("lineList",lineList);
        model.addAttribute("stationList",stationList);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/messagePlan/auditMessage";
    }


    @RequestMapping("/historyManagement")
    public String historyManagement(Model model, @RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=messageService.selectPaging(-1,pageNum,2);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/messagePlan/historyMessage";
    }

    @RequestMapping("/broadcastMessageManagement")
    public String broadcastMessageManagement(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=messageService.selectPaging(1,pageNum,2);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/messagePlan/broadcastMessage";
    }

    @RequestMapping("/revokeMessage")
    @ResponseBody
    public Map<String,Object> revokeMessage(ModelAndView mv,Integer id){
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
        msgSend.sendMsg("pisplayer.*."+ip,content);
        messageService.updateMessage(message.getId(),null,-1);
        return mv.getModel();
    }

    @RequestMapping("/addQueueMessage")
    @ResponseBody
    public Map<String,Object> addQueueMessage(HttpSession session, ModelAndView mv, Message message){
        //获取操作用户
        Users users=(Users)session.getAttribute("user");

        //设置实体值
        message.setOperator(users.getName());
        Integer i=messageService.addMessage(message);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/sendMessage")
    @ResponseBody
    public Map<String,Object> sendMessage(HttpSession session,ModelAndView mv,Message message){
        //获取操作用户
        Users users=(Users)session.getAttribute("user");
        //设置实体值
        message.setOperator(users.getName());
        message.setStartDate("");
        message.setEndDate("");
        message.setStartTime("");
        message.setEndTime("");
        Integer i=messageService.addMessage(message);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/auditMessage")
    @ResponseBody
    public Map<String,Object> auditMessage(ModelAndView mv,Integer id,Integer state){
        Integer i=messageService.updateMessage(id,state,null);
        if(i>0){
            Message message=messageService.selectMessage(id);
            if(message.getStartDate().length()==0 && message.getState()==1){
                String content="PMSG:<MSG>" +
                        "<Type>"+message.getType()+"</Type>" +
                        "<Info>" +
                        "<ID>"+message.getId()+"</ID>" +
                        "<CtrlID>"+message.getCtrlID()+"</CtrlID>" +
                        "<Level>"+message.getLevel()+"</Level>" +
                        "<State>1</State>" +
                        "<Text>"+message.getMsg()+"</Text>" +
                        "</Info></MSG>";
                Device device=deviceService.selectDevice(message.getDeviceID());
                long ip= IPUtil.ipToLong(device.getIp());
                msgSend.sendMsg("pisplayer.*."+ip,content);
                messageService.updateMessage(message.getId(),null,1);
            }
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteMessage")
    @ResponseBody
    public Map<String,Object> deleteMessage(ModelAndView mv,Integer id){
        Integer i=messageService.deleteMessage(id);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }
}
