package com.example.passenger.controller;

import com.example.passenger.entity.DevicePos;
import com.example.passenger.service.DevicePosService;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequestMapping("/devicePos")
@Controller
public class DevicePosController {

    @Autowired
    DevicePosService devicePosService;

    @RequestMapping("/devicePosManagement")
    public String devicePosManagement(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=devicePosService.selectDevicePosPaging(pageNum,2);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/systemConfig/devicePosManagement";
    }

    @RequestMapping("/addDevicePos")
    @ResponseBody
    public Map<String,Object> addDevicePos(ModelAndView mv, DevicePos device_pos){
        Integer i=devicePosService.addDevicePos(device_pos);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateDevicePos")
    @ResponseBody
    public Map<String,Object> updateDevicePos(ModelAndView mv, DevicePos device_pos){
        Integer i=devicePosService.updateDevicePos(device_pos);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteDevicePos")
    @ResponseBody
    public Map<String,Object> deleteDevicePos(ModelAndView mv,Integer id){
        Integer i=devicePosService.deleteDevicePos(id);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
