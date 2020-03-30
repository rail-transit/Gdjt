package com.example.passenger.controller;

import com.example.passenger.entity.DevicePos;
import com.example.passenger.entity.OperationLog;
import com.example.passenger.entity.Users;
import com.example.passenger.service.DevicePosService;
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

@RequestMapping("/devicePos")
@Controller
public class DevicePosController {
    private static final Logger logger = LoggerFactory.getLogger(DevicePosController.class);

    @Autowired
    DevicePosService devicePosService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/devPositionManage")
    public String devPositionManage(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageUtil pageUtil = devicePosService.selectDevicePosPaging(pageNum, 10);
        model.addAttribute("pageUtil", pageUtil);
        return "rightContent/system/devPositionManage";
    }

    @RequestMapping("/addDevicePos")
    @ResponseBody
    public Map<String, Object> addDevicePos(ModelAndView mv, DevicePos device_pos, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        try {
            Integer count = devicePosService.selectDevicePosByName(device_pos.getName(), null);
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = devicePosService.addDevicePos(device_pos);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + user.getName() + ") 添加设备位置(" + device_pos.getName() + ")!");
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

    @RequestMapping("/updateDevicePos")
    @ResponseBody
    public Map<String, Object> updateDevicePos(ModelAndView mv, DevicePos device_pos, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        try {
            Integer count = devicePosService.selectDevicePosByName(device_pos.getName(), device_pos.getId());
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = devicePosService.updateDevicePos(device_pos);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + user.getName() + ") 修改设备位置(" + device_pos.getName() + ")!");
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

    @RequestMapping("/deleteDevicePos")
    @ResponseBody
    public Map<String, Object> deleteDevicePos(ModelAndView mv, Integer id, HttpSession session) {
        DevicePos devicePos = devicePosService.selectDevicePosById(id);
        Users user = (Users) session.getAttribute("user");
        try {
            Integer i = devicePosService.deleteDevicePos(id);
            if (i > 0) {
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(user.getId().toString());
                operationLog.setType("系统配置管理");
                operationLog.setContent("用户(" + user.getName() + ") 删除设备位置(" + devicePos.getName() + ")!");
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
