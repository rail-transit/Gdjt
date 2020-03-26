package com.example.passenger.controller;

import com.example.passenger.entity.DeviceSpot;
import com.example.passenger.entity.DeviceType;
import com.example.passenger.entity.OperationLog;
import com.example.passenger.entity.Users;
import com.example.passenger.service.DeviceSpotService;
import com.example.passenger.service.DeviceTypeService;
import com.example.passenger.service.OperationLogService;
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
import java.util.List;
import java.util.Map;

@RequestMapping("/deviceSpot")
@Controller
public class DeviceSpotController {
    private static final Logger logger = LoggerFactory.getLogger(DeviceSpotController.class);

    @Autowired
    DeviceSpotService deviceSpotService;
    @Autowired
    DeviceTypeService deviceTypeService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/deviceSpotManagement")
    public String deviceSpotManagement(Model model) {
        List<DeviceType> deviceTypeList = deviceTypeService.selectAllDeviceType();
        model.addAttribute("deviceTypeList", deviceTypeList);
        return "rightContent/systemConfig/deviceSpotManagement";
    }

    @RequestMapping("/getDeviceSpot")
    @ResponseBody
    public Map<String, Object> getDeviceSpot(ModelAndView mv, @RequestParam(defaultValue = "1") Integer pageNum,
                                             Integer deviceType) {
        PageUtil pageUtil = deviceSpotService.selectDeviceSpotPaging(deviceType, pageNum, 6);
        mv.addObject("pageUtil", pageUtil);
        return mv.getModel();
    }


    @RequestMapping("/addDeviceSpot")
    @ResponseBody
    public Map<String, Object> addDeviceSpot(ModelAndView mv, DeviceSpot deviceSpot, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        try {
            Integer count = deviceSpotService.selectDeviceSpotByName(deviceSpot.getDeviceType(),
                    deviceSpot.getName(), null);
            DeviceSpot deviceSpot1 = deviceSpotService.selectDeviceSpotByCtrlType(null, 0,
                    deviceSpot.getCtrlType());
            if (count > 0 || deviceSpot1 == null) {
                mv.addObject("result", "exist");
            } else {
                Integer i = deviceSpotService.addDeviceSpot(deviceSpot);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + user.getName() + ") 添加测点(" + deviceSpot.getName() + ")!");
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


    @RequestMapping("/updateDeviceSpot")
    @ResponseBody
    public Map<String, Object> updateDeviceSpot(ModelAndView mv, DeviceSpot deviceSpot, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        try {
            Integer count = deviceSpotService.selectDeviceSpotByName(deviceSpot.getDeviceType(),
                    deviceSpot.getName(), deviceSpot.getId());
            DeviceSpot deviceSpot1 = deviceSpotService.selectDeviceSpotByCtrlType(deviceSpot.getId(),
                    0, deviceSpot.getCtrlType());
            if (count > 0 || deviceSpot1 == null) {
                mv.addObject("result", "exit");
            } else {
                Integer i = deviceSpotService.updateDeviceSpot(deviceSpot);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + user.getName() + ") 修改测点(" + deviceSpot.getName() + ")!");
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


    @RequestMapping("/deleteDeviceSpot")
    @ResponseBody
    public Map<String, Object> deleteDeviceSpot(ModelAndView mv, Integer id, HttpSession session) {
        DeviceSpot deviceSpot = deviceSpotService.selectDeviceSpotById(id);
        Users user = (Users) session.getAttribute("user");
        try {
            Integer i = deviceSpotService.deleteDeviceSpot(id);
            if (i > 0) {
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(user.getId().toString());
                operationLog.setType("系统配置管理");
                operationLog.setContent("用户(" + user.getName() + ") 删除测点(" + deviceSpot.getName() + ")!");
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
