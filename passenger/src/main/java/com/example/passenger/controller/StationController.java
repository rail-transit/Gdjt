package com.example.passenger.controller;

import com.example.passenger.entity.Station;
import com.example.passenger.service.DeviceService;
import com.example.passenger.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Station")
@EnableAutoConfiguration
public class StationController {
    @Autowired
    StationService stationService;

    @Autowired
    DeviceService deviceService;

    @RequestMapping("selectStation")
    @ResponseBody
    public Map<String,Object> selectStation(ModelAndView mv,Integer id){
        Station station=stationService.selectStation(id);
        mv.addObject("station",station);
        return mv.getModel();
    }

    @RequestMapping("/selectAllStation")
    @ResponseBody
    public Map<String,Object> selectAllStation(ModelAndView mv,Integer lineID){
        List<Station> stationList=stationService.selectAllStation(lineID);
        mv.addObject("stationList",stationList);
        return mv.getModel();
    }

    @RequestMapping("addStation")
    @ResponseBody
    public Map<String,Object> addStation(ModelAndView mv,Station station){
        Integer i=stationService.addStation(station);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateStation")
    @ResponseBody
    public Map<String,Object> updateStation(ModelAndView mv,Station station){
        station.setIsCenter(1);
        station.setIsTrain(1);
        Integer i=stationService.updateStation(station);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteStation")
    @ResponseBody
    public Map<String,Object> deleteStation(ModelAndView mv,Integer id){
        Integer i=stationService.deleteStation(id);
        if(i>0){
            deviceService.deleteDeviceByStationId(id);
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
