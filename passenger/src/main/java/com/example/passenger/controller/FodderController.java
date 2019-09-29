package com.example.passenger.controller;

import com.example.passenger.entity.Fodder;
import com.example.passenger.entity.FodderType;
import com.example.passenger.entity.Line;
import com.example.passenger.entity.Users;
import com.example.passenger.service.FodderService;
import com.example.passenger.service.FodderTypeService;
import com.example.passenger.service.LineService;
import com.example.passenger.utils.FtpFileUtil;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/fodder")
@Controller
public class FodderController {

    @Autowired
    FodderService fodderService;


    @Autowired
    LineService lineService;

    @Autowired
    FodderTypeService fodderTypeService;

    @RequestMapping("/fodderManagement")
    public String fodderManagement(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=fodderService.selectAllFodder(pageNum,2);
        List<Line> lineList=lineService.selectAllLine();
        model.addAttribute("lineList",lineList);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/systemConfig/fodderManagement";
    }

    @RequestMapping("/materialManagement")
    public String materialManagement(Model model,Integer type,Integer state,String name,
                                     @RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=fodderService.selectAllFodderMaterial(type,state,name,pageNum,3);
        List<FodderType> fodderTypeList=fodderTypeService.selectAllFodderType();
        model.addAttribute("fodderTypeList",fodderTypeList);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/systemConfig/materialManagement";
    }

    @RequestMapping("/addFodder")
    @ResponseBody
    public Map<String,Object> addFodder(ModelAndView mv, HttpSession session, Fodder fodder){
        try {
            Users users=(Users) session.getAttribute("user");
            fodder.setType(4);
            fodder.setSize("0");
            fodder.setState(1);
            fodder.setEditorID(users.getId().toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fodder.setEditTime(sdf.parse(sdf.format(new Date())));
            fodder.setGuid(UUID.randomUUID().toString());
            fodder.setMd5("abc");
            Integer i=fodderService.addFodder(fodder);
            if(i>0){
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return mv.getModel();
    }


    @RequestMapping("/addMaterial")
    public String addMaterial(@RequestParam(value="file",required=false)MultipartFile[] multipartFile,
                              HttpSession session) throws IOException {
        for (int i =0;i<multipartFile.length;i++) {
            if(multipartFile[i] != null) {
                Fodder fodder1=new Fodder();

                //生成新文件名，防止文件名重复而导致文件覆盖
                //1、获取原文件后缀名 .img .jpg ....
                String originalFileName = multipartFile[i].getOriginalFilename();
                InputStream inputStream=multipartFile[i].getInputStream();
                String suffix = originalFileName.substring(originalFileName.lastIndexOf('.'));
                //2、使用UUID生成新文件名
                String newFileName = UUID.randomUUID() + suffix;
                //上传
                Boolean flag= FtpFileUtil.uploadFile(newFileName,inputStream);
                if(flag==true){
                    //传输内容
                    try {
                        //判断文件类型类型
                        List<FodderType> fodderTypeList=fodderTypeService.selectAllFodderType();
                        for (FodderType fodderType:fodderTypeList) {
                            if(suffix.equalsIgnoreCase("."+fodderType.getSuffixName())){
                                fodder1.setType(fodderType.getType());
                            }
                        }
                        //上传路径
                        String Path= FtpFileUtil.getPath()+newFileName;

                        Users users=(Users) session.getAttribute("user");
                        fodder1.setName(originalFileName);
                        fodder1.setPath(Path);
                        fodder1.setSize(multipartFile[i].getSize()+"");
                        fodder1.setState(0);
                        fodder1.setEditorID(users.getId().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                        fodder1.setEditTime(sdf.parse(sdf.format(new Date())));
                        fodder1.setGuid(UUID.randomUUID().toString());
                        fodder1.setMd5("ccc");
                        fodderService.addFodder(fodder1);
                    }catch (Exception e){
                        System.out.println("时间错误");
                    }
                }
            }
        }
        return "redirect:/fodder/materialManagement";
    }


    @RequestMapping("/updateFodder")
    @ResponseBody
    public Map<String,Object> updateFodder(ModelAndView mv,Fodder fodder){
        Integer i=fodderService.updateFodder(fodder);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateFodderState")
    @ResponseBody
    public Map<String,Object> updateFodderState(ModelAndView mv,Integer id,Integer state){
        Integer i=fodderService.updateFodderState(id,state);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteFodder")
    @ResponseBody
    public Map<String,Object> deleteFodder(ModelAndView mv,Integer id){
        Integer i=fodderService.deleteFodder(id);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteMaterial")
    @ResponseBody
    public Map<String,Object> deleteMaterial(ModelAndView mv,Integer id){
        Integer i=fodderService.deleteFodder(id);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/selectFodder")
    @ResponseBody
    public Map<String,Object> selectFodder(ModelAndView mv,Integer id){
        Fodder fodder=fodderService.selectFodderByID(id);
        mv.addObject("fodder",fodder);
        return mv.getModel();
    }

    @RequestMapping("/selectFodderList")
    @ResponseBody
    public Map<String,Object> selectFodderList(ModelAndView mv,@RequestParam(defaultValue = "0") Integer type){
        List<Fodder> fodderList=fodderService.selectFodderByType(type);
        mv.addObject("fodderList",fodderList);
        return mv.getModel();
    }
}
