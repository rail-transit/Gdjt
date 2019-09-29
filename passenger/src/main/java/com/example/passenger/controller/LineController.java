package com.example.passenger.controller;

import com.example.passenger.entity.Device;
import com.example.passenger.entity.Line;
import com.example.passenger.entity.Station;
import com.example.passenger.entity.vo.LineVo;
import com.example.passenger.entity.vo.StationVo;
import com.example.passenger.service.DeviceService;
import com.example.passenger.service.LineService;
import com.example.passenger.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/Line")
@Controller
@EnableAutoConfiguration
public class LineController {
    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    @Autowired
    DeviceService deviceService;

    @RequestMapping("/jumpToDevice")
    public String jumpToDevice(){
        return "rightContent/systemConfig/equipmentInformation";
    }

    @RequestMapping("selectAllLine")
    @ResponseBody
    public Map<String,Object> selectAllLine(ModelAndView mv){
        List<Line> lineList=lineService.selectAllLine();
        mv.addObject("lineList",lineList);
        return mv.getModel();
    }

    @RequestMapping("/addLine")
    @ResponseBody
    public Map<String,Object> addLine(ModelAndView mv, Line line){
        Integer i=lineService.addLine(line);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/getMenu")
    @ResponseBody
    public Map<String,Object> getMenu(ModelAndView mv){
        List<Line> lineList=lineService.selectAllLine();
        List<LineVo> lineVoList=new ArrayList<>();
        for (Line line :lineList){
            LineVo vo=lineToVO(line);
            List<Station> stationList=stationService.selectAllStation(line.getId());
            List<StationVo> stationVoList=new ArrayList<>();
            for (Station station :stationList){
                StationVo stationVo=stationToVO(station);
                List<Device> deviceList=deviceService.selectAllDevice(station.getId());
                stationVo.setDeviceList(deviceList);
                stationVoList.add(stationVo);
            }
            vo.setStationVoList(stationVoList);
            lineVoList.add(vo);
        }
        mv.addObject("lineVoList",lineVoList);
        return mv.getModel();
    }

    @RequestMapping("/selectLine")
    @ResponseBody
    public Map<String,Object> selectLine(ModelAndView mv,Integer id){
        Line line=lineService.selectLine(id);
        mv.addObject("line",line);
        return mv.getModel();
    }

    @RequestMapping("/deleteLine")
    @ResponseBody
    public Map<String,Object> deleteLine(ModelAndView mv,Integer id){
        Integer i=lineService.deleteLine(id);
        if(i>0){
            stationService.deleteStationByLineId(id);
            deviceService.deleteDeviceByLineId(id);
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    @RequestMapping("/updateLine")
    @ResponseBody
    public Map<String,Object> updateLine(ModelAndView mv,Line line){
        Integer i=lineService.updateLine(line);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    //转型
    private LineVo lineToVO(Line line){
        LineVo lineVo=new LineVo();
        lineVo.setId(line.getId());
        lineVo.setLineID(line.getLineID());
        lineVo.setName(line.getName());
        lineVo.setEnName(line.getName());
        lineVo.setDes(line.getDes());
        lineVo.setServerIP(line.getServerIP());
        return lineVo;
    }
    //转型
    private StationVo stationToVO(Station station){
        StationVo vo=new StationVo();
        vo.setId(station.getId());
        vo.setStationID(station.getStationID());
        vo.setName(station.getName());
        vo.setEnName(station.getName());
        vo.setDes(station.getDes());
        vo.setDownStartTime(station.getDownStartTime());
        vo.setDownStopTime(station.getDownStopTime());
        vo.setIsCenter(station.getIsCenter());
        vo.setIsTrain(station.getIsTrain());
        vo.setServerIP(station.getServerIP());
        vo.setLineID(station.getLineID());
        vo.setUpStartTime(station.getUpStartTime());
        vo.setUpStopTime(station.getUpStopTime());
        return vo;
    }
}
