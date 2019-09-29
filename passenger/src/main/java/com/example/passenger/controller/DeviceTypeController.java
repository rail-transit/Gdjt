package com.example.passenger.controller;

import com.example.passenger.entity.DeviceType;
import com.example.passenger.service.DeviceTypeService;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequestMapping("/deviceType")
@Controller
public class DeviceTypeController {

    @Autowired
    DeviceTypeService deviceTypeService;

    @RequestMapping("/deviceTypeManagement")
    public String deviceTypeManagement(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=deviceTypeService.selectDeviceTypePaging(pageNum,2);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/systemConfig/deviceTypeManagement";
    }

    @RequestMapping("/addDeviceType")
    @ResponseBody
    public Map<String,Object> addDeviceType(ModelAndView mv, DeviceType device_type){
        Integer i=deviceTypeService.addDeviceType(device_type);
        if (i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateDeviceType")
    @ResponseBody
    public Map<String,Object> updateDeviceType(ModelAndView mv,DeviceType device_type){
        Integer i=deviceTypeService.updateDeviceType(device_type);
        if (i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteDeviceType")
    @ResponseBody
    public Map<String,Object> deleteDeviceType(ModelAndView mv,Integer id){
        Integer i=deviceTypeService.deleteDeviceType(id);
        if (i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
