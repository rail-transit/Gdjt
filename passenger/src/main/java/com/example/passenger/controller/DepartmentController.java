package com.example.passenger.controller;

import com.example.passenger.entity.Department;
import com.example.passenger.service.DepartmentService;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequestMapping("/department")
@Controller
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @RequestMapping("/departmentManagement")
    public  String departmentManagement(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=departmentService.selectDepartmentPaging(pageNum,2);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/systemConfig/departmentManagement";
    }

    @RequestMapping("/addDepartment")
    @ResponseBody
    public Map<String,Object> addDepartment(ModelAndView mv, Department department){
        Integer i=departmentService.addDepartment(department);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateDepartment")
    @ResponseBody
    public Map<String,Object> updateDepartment(ModelAndView mv, Department department){
        Integer i=departmentService.updateDepartment(department);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteDepartment")
    @ResponseBody
    public Map<String,Object> deleteDepartment(ModelAndView mv,Integer id){
        Integer i=departmentService.deleteDepartment(id);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
