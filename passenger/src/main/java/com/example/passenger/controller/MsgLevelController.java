package com.example.passenger.controller;

import com.example.passenger.entity.MsgLevel;
import com.example.passenger.entity.OperationLog;
import com.example.passenger.entity.Users;
import com.example.passenger.service.MsgLevelService;
import com.example.passenger.service.OperationLogService;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/msgLevel")
public class MsgLevelController {
    private static final Logger logger = LoggerFactory.getLogger(MsgLevelController.class);

    @Autowired
    MsgLevelService msgLevelService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/msgLevelManagement")
    public String msgLevelManagement() {
        return "rightContent/messagePlan/priority";
    }

    @RequestMapping("/getMsgLevel")
    @ResponseBody
    public Map<String, Object> getMsgLevel(ModelAndView mv, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageUtil pageUtil = msgLevelService.selectPaging(pageNum, 10);
        mv.addObject("pageUtil", pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/addMsgLevel")
    @ResponseBody
    public Map<String, Object> addMsgLevel(ModelAndView mv, MsgLevel msgLevel, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = msgLevelService.selectLevelByID(msgLevel.getLevel(), msgLevel.getId());
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = msgLevelService.addMsgLevel(msgLevel);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("预案管理");
                    operationLog.setContent("用户(" + users.getName() + ") 添加优先级(" + msgLevel.getTitle() + ")!");
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

    @RequestMapping("/updateMsgLevel")
    @ResponseBody
    public Map<String, Object> updateMsgLevel(ModelAndView mv, MsgLevel msgLevel, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = msgLevelService.selectLevelByID(msgLevel.getLevel(), msgLevel.getId());
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = msgLevelService.updateMsgLevel(msgLevel);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("预案管理");
                    operationLog.setContent("用户(" + users.getName() + ") 修改优先级(" + msgLevel.getTitle() + ")!");
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

    @RequestMapping("/deleteMsgLevel")
    @ResponseBody
    public Map<String, Object> deleteMsgLevel(ModelAndView mv, Integer id, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        try {
            MsgLevel msgLevel = msgLevelService.selectMsgLevel(id);
            Integer i = msgLevelService.deleteMsgLevel(id);
            if (i > 0) {
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("预案管理");
                operationLog.setContent("用户(" + users.getName() + ") 删除优先级(" + msgLevel.getTitle() + ")!");
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

}
