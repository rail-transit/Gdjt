package com.example.passenger.controller;

import com.example.passenger.entity.Group;
import com.example.passenger.entity.OperationLog;
import com.example.passenger.entity.Rights;
import com.example.passenger.entity.Users;
import com.example.passenger.service.GroupService;
import com.example.passenger.service.OperationLogService;
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

import javax.servlet.http.HttpSession;
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
    @Autowired
    OperationLogService operationLogService;


    @RequestMapping("/groupManagement")
    public String groupManagement(Model model,@RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=groupService.selectGroupPaging(pageNum,10);
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
    public Map<String,Object> addGroup(ModelAndView mv, Group group, HttpSession session){
        Users user=(Users) session.getAttribute("user");
        try {
            Integer count=groupService.selectGroupByName(group.getName(),null);
            if(count>0){
                mv.addObject("result","exit");
            }else{
                Integer i=groupService.addGroup(group);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户("+user.getName()+") 添加角色("+group.getName()+")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updateGroup")
    @ResponseBody
    public Map<String,Object> updateGroup(ModelAndView mv,Group group,HttpSession session){
        Users user=(Users) session.getAttribute("user");
        try {
            Integer count=groupService.selectGroupByName(group.getName(),group.getId());
            if(count>0){
                mv.addObject("result","exit");
            }else{
                Integer i=groupService.updateGroup(group);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户("+user.getName()+") 修改角色("+group.getName()+")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteGroup")
    @ResponseBody
    public Map<String,Object> deleteGroup(ModelAndView mv,HttpSession session,Integer id){
        Group group=groupService.selectGroupByID(id);
        Users user=(Users) session.getAttribute("user");
        try {
            Integer i=groupService.deleteGroup(id);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(user.getId().toString());
                operationLog.setType("系统配置管理");
                operationLog.setContent("用户("+user.getName()+") 删除角色("+group.getName()+")成功!");
                operationLogService.addOperationLog(operationLog);
                userService.deleteUserByGroupId(id);
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
