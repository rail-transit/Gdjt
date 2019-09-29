package com.example.passenger.controller;

import com.example.passenger.entity.Line;
import com.example.passenger.entity.LineStandby;
import com.example.passenger.entity.StandbyEvent;
import com.example.passenger.entity.StandbyType;
import com.example.passenger.entity.vo.LineStandbyVo;
import com.example.passenger.service.LineService;
import com.example.passenger.service.LineStandbyService;
import com.example.passenger.service.StandbyEventService;
import com.example.passenger.service.StandbyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("/lineStandbyManagement")
    public String lineStandbyManagement(Model model){
        List<Line> lineList=lineService.selectAllLine();
        List<StandbyType> standbyTypeList=standbyTypeService.selectAllStandbyType();
        model.addAttribute("lineList",lineList);
        model.addAttribute("standbyTypeList",standbyTypeList);
        return "rightContent/equipmentControl/lineStandby";
    }

    @RequestMapping("/addLineStandby")
    @ResponseBody
    public Map<String,Object> addLineStandby(ModelAndView mv,LineStandby lineStandby, StandbyEvent standbyEvent){
        Integer count=lineStandbyService.selectLineStandbyExist(lineStandby.getLineID(),lineStandby.getStandbyType());
        if(count>0){
            mv.addObject("result","existing");
        }else{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            standbyEvent.setLineID(lineStandby.getLineID());
            standbyEvent.setTimestamp(sdf.format(new Date()));
            Integer i=lineStandbyService.addLineStandby(lineStandby);
            standbyEventService.addStandbyEvent(standbyEvent);
            if(i>0){
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }
        return mv.getModel();
    }

    @RequestMapping("/updateLineStandby")
    @ResponseBody
    public Map<String,Object> updateLineStandby(ModelAndView mv,LineStandby lineStandby,String event){
        StandbyEvent standbyEvent=new StandbyEvent();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        standbyEvent.setEvent(event);
        standbyEvent.setLineID(lineStandby.getLineID());
        standbyEvent.setTimestamp(sdf.format(new Date()));
        standbyEventService.addStandbyEvent(standbyEvent);
        Integer i=lineStandbyService.updateLineStandby(lineStandby);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteLineStandby")
    @ResponseBody
    public Map<String,Object> deleteLineStandby(ModelAndView mv,Integer id){
        Integer i=lineStandbyService.deleteLineStandby(id);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/selectLineStandby")
    @ResponseBody
    public Map<String,Object> selectLineStandby(ModelAndView mv,Integer lineID){
        List<LineStandbyVo> lineStandbyList=lineStandbyService.selectAllLineStandby(lineID);
        mv.addObject("lineStandbyList",lineStandbyList);
        return mv.getModel();
    }

    @RequestMapping("/selectStandbyEvent")
    @ResponseBody
    public Map<String,Object> selectStandbyEvent(ModelAndView mv,Integer lineID){
        List<StandbyEvent> standbyEventList=standbyEventService.selectAllStandbyEvent(lineID);
        mv.addObject("standbyEventList",standbyEventList);
        return mv.getModel();
    }

}
