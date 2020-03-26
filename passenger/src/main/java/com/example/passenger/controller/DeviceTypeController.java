package com.example.passenger.controller;

import com.example.passenger.entity.DeviceType;
import com.example.passenger.entity.OperationLog;
import com.example.passenger.entity.Users;
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
import java.util.Map;

@RequestMapping("/deviceType")
@Controller
public class DeviceTypeController {
    private static final Logger logger = LoggerFactory.getLogger(DeviceTypeController.class);

    @Autowired
    DeviceTypeService deviceTypeService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/deviceTypeManagement")
    public String deviceTypeManagement(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageUtil pageUtil = deviceTypeService.selectDeviceTypePaging(pageNum, 8);
        model.addAttribute("pageUtil", pageUtil);
        return "rightContent/systemConfig/deviceTypeManagement";
    }

    @RequestMapping("/addDeviceType")
    @ResponseBody
    public Map<String, Object> addDeviceType(ModelAndView mv, DeviceType device_type, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        try {
            Integer count = deviceTypeService.selectDeviceTypeByName(device_type.getName(), null);
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = deviceTypeService.addDeviceType(device_type);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + user.getName() + ") 添加设备类型(" + device_type.getName() + ")!");
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

    @RequestMapping("/updateDeviceType")
    @ResponseBody
    public Map<String, Object> updateDeviceType(ModelAndView mv, DeviceType device_type, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        try {
            Integer count = deviceTypeService.selectDeviceTypeByName(device_type.getName(), device_type.getId());
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = deviceTypeService.updateDeviceType(device_type);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + user.getName() + ") 修改设备类型(" + device_type.getName() + ")!");
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

    @RequestMapping("/deleteDeviceType")
    @ResponseBody
    public Map<String, Object> deleteDeviceType(ModelAndView mv, Integer id, HttpSession session) {
        DeviceType deviceType = deviceTypeService.selectDeviceTypeById(id);
        Users user = (Users) session.getAttribute("user");
        try {
            Integer i = deviceTypeService.deleteDeviceType(id);
            if (i > 0) {
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(user.getId().toString());
                operationLog.setType("系统配置管理");
                operationLog.setContent("用户(" + user.getName() + ") 删除设备类型(" + deviceType.getName() + ")!");
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
