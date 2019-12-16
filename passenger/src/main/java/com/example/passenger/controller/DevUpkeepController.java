package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.DevUpkeepVo;
import com.example.passenger.service.*;
import com.example.passenger.utils.PageUtil;
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

@RequestMapping("/devUpkeep")
@Controller
public class DevUpkeepController {

    @Autowired
    DevUpkeepService devUpkeepService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    StationService stationService;
    @Autowired
    LineService lineService;
    @Autowired
    DeviceTypeService deviceTypeService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/devUpkeepManagement")
    public String devUpkeepManagement(Model model,Integer id){
        List<DevUpkeep> devUpkeepList=devUpkeepService.selectDevUpkeepByEndTimeIsNull(id);
        Device device=deviceService.selectDevice(id);
        model.addAttribute("device",device);
        model.addAttribute("devUpkeepList",devUpkeepList);
        return "rightContent/equipmentControl/equipmentMaintain";
    }

    @RequestMapping("/maintainStatistics")
    public String maintainStatistics(Model model){
        List<Line> lineList=lineService.selectAllLine();
        List<Station> stationList=stationService.queryAllStation();
        List<Device> deviceList=deviceService.queryAllDevice();
        List<DeviceType> deviceTypeList=deviceTypeService.selectAllDeviceType();

        model.addAttribute("lineList",lineList);
        model.addAttribute("stationList",stationList);
        model.addAttribute("deviceList",deviceList);
        model.addAttribute("deviceTypeList",deviceTypeList);
        return "rightContent/reportForm/maintain";
    }

    @RequestMapping("/selectStatistics")
    @ResponseBody
    public Map<String,Object> selectStatistics(@RequestParam(defaultValue = "1") Integer pageNum,
                                               ModelAndView mv, DevUpkeepVo devUpkeep){
        try {
            PageUtil pageUtil=devUpkeepService.maintainPaging(devUpkeep,pageNum,10);
            mv.addObject("pageUtil",pageUtil);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }


    @RequestMapping("/selectDevUpkeep")
    @ResponseBody
    public Map<String,Object> selectDevUpkeep(ModelAndView mv,Integer id,
                                              @RequestParam(defaultValue = "1") Integer pageNum){
        Device device=deviceService.selectDevice(id);
        PageUtil pageUtil=devUpkeepService.selectDevUpkeepByEndTimeNotNull(id,pageNum,10);
        mv.addObject("device",device);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/getDevUpKeep")
    @ResponseBody
    public Map<String,Object> getDevUpKeep(ModelAndView mv,DevUpkeepVo devUpkeep){
        try {
            List<Map<String,String>> devUpkeepVoList=devUpkeepService.getDevUpKeep(devUpkeep);
            mv.addObject("devUpkeepVoList",devUpkeepVoList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/addDevUpkeep")
    @ResponseBody
    public Map<String,Object> addDevUpkeep(ModelAndView mv, DevUpkeep devUpkeep, HttpSession session){
        Users users=(Users) session.getAttribute("user");
        try {
            Integer i=devUpkeepService.addDevUpkeep(devUpkeep);
            if(i>0){
                //日志记录
                Device device=deviceService.selectDevice(devUpkeep.getDeviceID());
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("设备监控");
                operationLog.setContent("用户("+users.getName()+") 添加设备保养("+device.getName()+")!");
                operationLogService.addOperationLog(operationLog);
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

    @RequestMapping("/updateDevUpkeep")
    @ResponseBody
    public Map<String,Object> updateDevUpkeep(ModelAndView mv,DevUpkeep devUpkeep,HttpSession session){
        Users users=(Users) session.getAttribute("user");
        try {
            Integer i=devUpkeepService.updateDevUpkeep(devUpkeep);
            if(i>0){
                //日志记录
                Device device=deviceService.selectDevice(devUpkeep.getDeviceID());
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("设备监控");
                operationLog.setContent("用户("+users.getName()+") 结束设备保养("+device.getName()+")!");
                operationLogService.addOperationLog(operationLog);
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
