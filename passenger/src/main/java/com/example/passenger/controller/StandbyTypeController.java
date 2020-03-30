package com.example.passenger.controller;

import com.example.passenger.entity.OperationLog;
import com.example.passenger.entity.StandbyType;
import com.example.passenger.entity.Users;
import com.example.passenger.service.LineStandbyService;
import com.example.passenger.service.OperationLogService;
import com.example.passenger.service.StandbyTypeService;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RequestMapping("/standbyType")
@Controller
public class StandbyTypeController {
    private static final Logger logger = LoggerFactory.getLogger(StandbyTypeController.class);

    @Autowired
    StandbyTypeService standbyTypeService;
    @Autowired
    LineStandbyService lineStandbyService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/standbyType")
    public String standbyType(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageUtil pageUtil = standbyTypeService.selectStandbyTypePaging(pageNum, 9);
        model.addAttribute("pageUtil", pageUtil);
        return "rightContent/monitor/standbyType";
    }

    @RequestMapping("/addStandbyType")
    @ResponseBody
    public Map<String, Object> addStandbyType(ModelAndView mv, StandbyType standbyType, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = standbyTypeService.selectStandbyTypeByName(standbyType.getStandbyName());
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = standbyTypeService.addStandbyType(standbyType);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("设备监控");
                    operationLog.setContent("用户(" + users.getName() + ") 添加备件类型(" + standbyType.getStandbyName() + ")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/updateStandbyType")
    @ResponseBody
    public Map<String, Object> updateStandbyType(ModelAndView mv, StandbyType standbyType, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer i = standbyTypeService.updateStandbyType(standbyType);
            if (i > 0) {
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("设备监控");
                operationLog.setContent("用户(" + users.getName() + ") 修改备件类型(" + standbyType.getStandbyName() + ")!");
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result", "success");
            } else {
                mv.addObject("result", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteStandbyType")
    @ResponseBody
    public Map<String, Object> deleteStandbyType(ModelAndView mv, Integer id, String name, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = lineStandbyService.getStandbyExistByType(id);
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = standbyTypeService.deleteStandbyType(id);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("设备监控");
                    operationLog.setContent("用户(" + users.getName() + ") 删除备件类型(" + name + ")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }
}
