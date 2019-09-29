package com.example.passenger.controller;

import com.example.passenger.entity.Group;
import com.example.passenger.entity.Rights;
import com.example.passenger.service.GroupService;
import com.example.passenger.service.RightsService;
import com.example.passenger.service.UserService;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/group")
@Controller
public class GroupController {

    @Autowired
    GroupService groupService;
    @Autowired
    UserService userService;
    @Autowired
    RightsService rightsService;


    @RequestMapping("/groupManagement")
    public String groupManagement(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=groupService.selectGroupPaging(pageNum,2);
        model.addAttribute("pageUtil",pageUtil);
        return "rightContent/systemConfig/groupManagement";
    }

    @RequestMapping("/getRights")
    @ResponseBody
    public Map<String,Object> getRights(ModelAndView mv,Integer groupID){
        try {
            Group group=groupService.selectGroupByID(groupID);
            List<Rights> rightsList=new ArrayList<>();
            String[] rightLevel=group.getRightLevel().split(",");
            for (int i=0;i<rightLevel.length;i++){
                Rights rights=rightsService.selectRightsByID(Integer.parseInt(rightLevel[i]));
                rightsList.add(rights);
            }
            mv.addObject("rightsList",rightsList);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/addGroup")
    @ResponseBody
    public Map<String,Object> addGroup(ModelAndView mv, Group group){
        Integer i=groupService.addGroup(group);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateGroup")
    @ResponseBody
    public Map<String,Object> updateGroup(ModelAndView mv,Group group){
        Integer i=groupService.updateGroup(group);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteGroup")
    @ResponseBody
    public Map<String,Object> deleteGroup(ModelAndView mv,Integer id){
        Integer i=groupService.deleteGroup(id);
        if(i>0){
            userService.deleteUserByGroupId(id);
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
