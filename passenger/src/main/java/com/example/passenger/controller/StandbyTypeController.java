package com.example.passenger.controller;

import com.example.passenger.entity.StandbyType;
import com.example.passenger.service.StandbyTypeService;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequestMapping("/standbyType")
@Controller
public class StandbyTypeController {

    @Autowired
    StandbyTypeService standbyTypeService;

    @RequestMapping("/standbyTypeManagement")
    public String standbyTypeManagement(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=standbyTypeService.selectStandbyTypePaging(pageNum,2);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/equipmentControl/spareTypeManagement";
    }

    @RequestMapping("/addStandbyType")
    @ResponseBody
    public Map<String,Object> addStandbyType(ModelAndView mv, StandbyType standbyType){
        Integer i=standbyTypeService.addStandbyType(standbyType);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateStandbyType")
    @ResponseBody
    public Map<String,Object> updateStandbyType(ModelAndView mv,StandbyType standbyType){
        Integer i=standbyTypeService.updateStandbyType(standbyType);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteStandbyType")
    @ResponseBody
    public Map<String,Object> deleteStandbyType(ModelAndView mv,Integer id){
        Integer i=standbyTypeService.deleteStandbyType(id);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
