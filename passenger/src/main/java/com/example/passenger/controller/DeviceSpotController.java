package com.example.passenger.controller;

import com.example.passenger.entity.DeviceSpot;
import com.example.passenger.entity.DeviceType;
import com.example.passenger.service.DeviceSpotService;
import com.example.passenger.service.DeviceTypeService;
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

@RequestMapping("/deviceSpot")
@Controller
public class DeviceSpotController {

    @Autowired
    DeviceSpotService deviceSpotService;

    @Autowired
    DeviceTypeService deviceTypeService;

    @RequestMapping("/deviceSpotManagement")
    public String deviceSpotManagement(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        List<DeviceType> deviceTypeList=deviceTypeService.selectAllDeviceType();
        PageUtil pageUtil=deviceSpotService.selectDeviceSpotPaging(pageNum,2);
        model.addAttribute("deviceTypeList",deviceTypeList);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/systemConfig/deviceSpotManagement";
    }

    @RequestMapping("/addDeviceSpot")
    @ResponseBody
    public Map<String,Object> addDeviceSpot(ModelAndView mv, DeviceSpot deviceSpot){
        Integer i=deviceSpotService.addDeviceSpot(deviceSpot);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    @RequestMapping("/updateDeviceSpot")
    @ResponseBody
    public Map<String,Object> updateDeviceSpot(ModelAndView mv,DeviceSpot deviceSpot){
        Integer i=deviceSpotService.updateDeviceSpot(deviceSpot);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    @RequestMapping("/deleteDeviceSpot")
    @ResponseBody
    public Map<String,Object> deleteDeviceSpot(ModelAndView mv,Integer id){
        Integer i=deviceSpotService.deleteDeviceSpot(id);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
