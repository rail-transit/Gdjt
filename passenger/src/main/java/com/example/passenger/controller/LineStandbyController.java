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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RequestMapping("/lineStandby")
@Controller
public class LineStandbyController {

    @Autowired
    LineStandbyService lineStandbyService;
    @Autowired
    StandbyTypeService standbyTypeService;
    @Autowired
    LineService lineService;
    @Autowired
    StandbyEventService standbyEventService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/lineStandbyManagement")
    public String lineStandbyManagement(Model model){
        List<Line> lineList=lineService.selectAllLine();
        List<StandbyType> standbyTypeList=standbyTypeService.selectAllStandbyType();
        model.addAttribute("lineList",lineList);
        model.addAttribute("standbyTypeList",standbyTypeList);
        return "rightContent/equipmentControl/lineStandby";
    }

    @RequestMapping("/sparePartStatistics")
    public String sparePartStatistics(Model model){
        List<Line> lineList=lineService.selectAllLine();
        model.addAttribute("lineList",lineList);
        return "rightContent/reportForm/sparePart";
    }

    @RequestMapping("/selectStatistics")
    @ResponseBody
    public Map<String,Object> selectStatistics(@RequestParam(defaultValue = "1") Integer pageNum,
                                               ModelAndView mv,Integer lineID){
        try {
            PageUtil pageUtil=lineStandbyService.selectPaging(lineID,pageNum,10);
            mv.addObject("pageUtil",pageUtil);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/addLineStandby")
    @ResponseBody
    public Map<String,Object> addLineStandby(ModelAndView mv, LineStandby lineStandby,StandbyEvent standbyEvent, HttpSession session){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Users users=(Users) session.getAttribute("user");
        try {
            Integer count=lineStandbyService.selectLineStandbyExist(lineStandby.getLineID(),lineStandby.getStandbyType());
            if(count>0){
                mv.addObject("result","existing");
            }else{
                standbyEvent.setLineID(lineStandby.getLineID());
                standbyEvent.setTimestamp(sdf.format(new Date()));

                Integer i=lineStandbyService.addLineStandby(lineStandby);
                standbyEventService.addStandbyEvent(standbyEvent);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    Line line=lineService.selectLine(lineStandby.getLineID());
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("设备监控");
                    operationLog.setContent("用户("+users.getName()+") 向("+line.getName()+")添加备件!");
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

    @RequestMapping("/updateLineStandby")
    @ResponseBody
    public Map<String,Object> updateLineStandby(ModelAndView mv,LineStandby lineStandby,String event,HttpSession session){
        Users users=(Users) session.getAttribute("user");
        try {
            StandbyEvent standbyEvent=new StandbyEvent();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            standbyEvent.setEvent(event);
            standbyEvent.setLineID(lineStandby.getLineID());
            standbyEvent.setTimestamp(sdf.format(new Date()));
            standbyEventService.addStandbyEvent(standbyEvent);

            Integer i=lineStandbyService.updateLineStandby(lineStandby);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                Line line=lineService.selectLine(lineStandby.getLineID());
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("设备监控");
                operationLog.setContent("用户("+users.getName()+") 修改线路("+line.getName()+")备件!");
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

    @RequestMapping("/deleteLineStandby")
    @ResponseBody
    public Map<String,Object> deleteLineStandby(ModelAndView mv,Integer id,Integer lineID,HttpSession session){
        Users users=(Users) session.getAttribute("user");
        try {
            Integer i=lineStandbyService.deleteLineStandby(id);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                Line line=lineService.selectLine(lineID);
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("设备监控");
                operationLog.setContent("用户("+users.getName()+") 删除线路("+line.getName()+")备件!");
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

    @RequestMapping("/selectLineStandby")
    @ResponseBody
    public Map<String,Object> selectLineStandby(@RequestParam(defaultValue = "1") Integer pageNum,
                                                ModelAndView mv,Integer lineID){
        PageUtil pageUtil=lineStandbyService.selectPaging(lineID,pageNum,10);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/selectStandbyEvent")
    @ResponseBody
    public Map<String,Object> selectStandbyEvent(ModelAndView mv,Integer lineID){
        List<StandbyEvent> standbyEventList=standbyEventService.selectAllStandbyEvent(lineID);
        mv.addObject("standbyEventList",standbyEventList);
        return mv.getModel();
    }

    @RequestMapping("/getSparePart")
    @ResponseBody
    public Map<String,Object> getSparePart(ModelAndView mv,Integer lineID){
        try {
            List<Map<String,String>> lineStandbyVoList=lineStandbyService.getSparePart(lineID);
            mv.addObject("lineStandbyVoList",lineStandbyVoList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

}
