package com.example.passenger.controller;

import com.example.passenger.entity.Line;
import com.example.passenger.entity.Station;
import com.example.passenger.service.BroadcastService;
import com.example.passenger.service.LineService;
import com.example.passenger.service.StationService;
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

@RequestMapping("/broadcast")
@Controller
public class BroadcastController {
    @Autowired
    BroadcastService broadcastService;
    @Autowired
    LineService lineService;
    @Autowired
    StationService stationService;


    @RequestMapping("/videoPlayStatistics")
    public String videoPlayStatistics(Model model){
        List<Line> lineList=lineService.selectAllLine();
        List<Station> stationList=stationService.queryAllStation();

        model.addAttribute("lineList",lineList);
        model.addAttribute("stationList",stationList);
        return "rightContent/reportForm/videoPlay";
    }

    @RequestMapping("/textPlayStatistics")
    public String textPlayStatistics(Model model){
        List<Line> lineList=lineService.selectAllLine();
        List<Station> stationList=stationService.queryAllStation();

        model.addAttribute("lineList",lineList);
        model.addAttribute("stationList",stationList);
        return "rightContent/reportForm/textPlay";
    }

    @RequestMapping("/getBroadcast")
    @ResponseBody
    public Map<String,Object> getBroadcast(ModelAndView mv,Integer lineID,Integer stationID,
                                           Integer deviceID,Integer type,String startDate,
                                           String endDate){
        try {
            List<Map<String,String>> broadcastVoList=broadcastService.getBroadcast(lineID,stationID,
                    deviceID,type,startDate,endDate);
            mv.addObject("broadcastVoList",broadcastVoList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }


    @RequestMapping("/selectStatistics")
    @ResponseBody
    public Map<String,Object> selectStatistics(ModelAndView mv, Integer lineID, Integer stationID, Integer deviceID,
                                               Integer type, String startDate, String endDate,
                                               @RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=broadcastService.broadcastPaging(lineID,stationID,deviceID,type,startDate,
                endDate,pageNum,10);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

}
