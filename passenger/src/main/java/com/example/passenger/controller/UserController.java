package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.EventStateVo;
import com.example.passenger.service.*;
import com.example.passenger.utils.MD5;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/user")
@EnableAutoConfiguration
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    GroupService groupService;
    @Autowired
    RightsService rightsService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/home")
    public String home(){
        return "login";
    }

    @RequestMapping("/exit")
    public String exit (HttpSession Session){
        Users users=(Users) Session.getAttribute("user");
        //日志记录
        OperationLog operationLog=new OperationLog();
        operationLog.setOperator(users.getId().toString());
        operationLog.setType("登录及注销");
        operationLog.setContent("用户("+users.getName()+") 从乘客信息系统中 注销成功!");
        operationLogService.addOperationLog(operationLog);

        Session.removeAttribute("user");
        Session.removeAttribute("token");
        Session.removeAttribute("deviceList");
        return "login";
    }

    //登录
    @RequestMapping("/login")
    public String login(Users user, RedirectAttributes attributes, HttpSession session) {
        try {
            String msg;
            Users userForBase=userService.findByUsername(user);
            if(userForBase==null){
                msg="登录失败,用户不存在";
                attributes.addFlashAttribute("msg",msg);
                return "redirect:/user/home";
            }else if(userForBase.getState()==0){
                msg="登录失败,该用户被禁用";
                attributes.addFlashAttribute("msg",msg);
                return "redirect:/user/home";
            }else {
                if (!userForBase.getPwd().equals(MD5.MD5(user.getPwd()))){
                    msg="登录失败,密码错误";
                    attributes.addFlashAttribute("msg",msg);
                    return "redirect:/user/home";
                }else {
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(userForBase.getId().toString());
                    operationLog.setType("登录及注销");
                    operationLog.setContent("用户("+userForBase.getName()+")登录到乘客信息系统成功!");
                    operationLogService.addOperationLog(operationLog);

                    String token = tokenService.getToken(userForBase);
                    List<String> deviceList=new ArrayList<>();
                    List<Rights> rightsList=new ArrayList<>();
                    List<Map<String,String>> mapList=new ArrayList<>();
                    List<EventStateVo> eventList=new ArrayList<>();
                    Group group=groupService.selectGroupByID(userForBase.getGroupID());
                    String[] rightLevel=group.getRightLevel().split(",");
                    for (int i=0;i<rightLevel.length;i++){
                        Rights rights=rightsService.selectRightsByID(Integer.parseInt(rightLevel[i]));
                        rightsList.add(rights);
                    }
                    session.setAttribute("mapList",mapList);
                    session.setAttribute("eventList",eventList);
                    session.setAttribute("rightsList",rightsList);
                    session.setAttribute("user",userForBase);
                    session.setAttribute("deviceList",deviceList);
                    session.setAttribute("token",token);
                    return "main";
                }
            }
        }catch (Exception e){
            logger.error("登陆异常",e.getMessage());
            return "redirect:/user/home";
        }
    }

    //添加用户
    @RequestMapping("addUser")
    @ResponseBody
    public Map<String,Object> addUser(ModelAndView mv,Users users,HttpSession session){
        Users userByName=userService.findByUsername(users);
        if(userByName!=null){
            mv.addObject("result","exit");
            return mv.getModel();
        }else{
            users.setPwd(MD5.MD5(users.getPwd()));
            users.setRightLevel(0);
            users.setState(1);
            Integer i=userService.addUser(users);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                Users user=(Users) session.getAttribute("user");
                operationLog.setOperator(user.getId().toString());
                operationLog.setType("系统配置管理");
                operationLog.setContent("用户("+user.getName()+") 添加用户("+users.getName()+")!");
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }
        return mv.getModel();
    }


    @RequestMapping("/userManagement")
    public String userManagement(Model model){
        List<Department> departmentList=departmentService.selectAllDepartment();
        List<Group> groupList=groupService.selectAllGroup();
        model.addAttribute("departmentList",departmentList);
        model.addAttribute("groupList",groupList);
        return "rightContent/systemConfig/userInformation";
    }

    @RequestMapping("/addUserManagement")
    public String addUserManagement(Model model){
        List<Group> groupList=groupService.selectAllGroup();
        List<Department> departmentList=departmentService.selectAllDepartment();
        model.addAttribute("departmentList",departmentList);
        model.addAttribute("groupList",groupList);
        return "rightContent/systemConfig/addUser";
    }

    @RequestMapping("/selectUserByName")
    @ResponseBody
    public Map<String,Object> selectUserByName(ModelAndView mv,String username,
                                               @RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=userService.selectAllUsersByName(username,pageNum,10);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/updateUserState")
    @ResponseBody
    public Map<String,Object> updateUserState(ModelAndView mv,Integer id,Integer state,String name,
                                              HttpSession session){
        Integer i=userService.updateUserState(id,state);
        if(i>0){
            //日志记录
            OperationLog operationLog=new OperationLog();
            Users user=(Users) session.getAttribute("user");
            operationLog.setOperator(user.getId().toString());
            operationLog.setType("系统配置管理");
            if(state==1){
                operationLog.setContent("用户("+user.getName()+") 激活用户("+name+")!");
            }else{
                operationLog.setContent("用户("+user.getName()+") 禁用用户("+name+")!");
            }
            operationLogService.addOperationLog(operationLog);
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    //修改用户
    @RequestMapping("/updateUser")
    @ResponseBody
    public Map<String,Object> updateUser(ModelAndView mv,Users users,HttpSession session){
        Integer count=userService.selectUserIsNotID(users.getId(),users.getName());
        if(count>0){
            mv.addObject("result","exit");
        }else{
            Integer i=userService.updateUser(users);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                Users user=(Users) session.getAttribute("user");
                operationLog.setOperator(user.getId().toString());
                operationLog.setType("系统配置管理");
                operationLog.setContent("用户("+user.getName()+") 修改用户("+users.getName()+")信息!");
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }
        return mv.getModel();
    }

    //删除用户
    @RequestMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(Integer userId,Model model){
        try {
            if(userId==null){
                return "未找到该用户";
            }
            Integer i=userService.deleteUser(userId);
            if(i>0){
                return "删除成功";
            }else{
                model.addAttribute("msg","");
                return "删除失败";
            }
        }catch (Exception e){
            logger.error("删除异常",e.getMessage());
            return "删除异常";
        }
    }
}
