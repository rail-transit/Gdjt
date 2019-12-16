package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.DevRepairVo;
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

@RequestMapping("/devRepair")
@Controller
public class DevRepairController {

    @Autowired
    DevRepairService devRepairService;
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

    @RequestMapping("/devRepairManagement")
    public String devRepairManagement(Model model,Integer id){
        List<DevRepair> devRepairList=devRepairService.selectDevRepairByEndTimeIsNull(id);
        Device device=deviceService.selectDevice(id);
        model.addAttribute("devRepairList",devRepairList);
        model.addAttribute("device",device);
        return "rightContent/equipmentControl/equipmentRepair";
    }

    @RequestMapping("/repairStatistics")
    public String repairStatistics(Model model){
        List<Line> lineList=lineService.selectAllLine();
        List<Station> stationList=stationService.queryAllStation();
        List<Device> deviceList=deviceService.queryAllDevice();
        List<DeviceType> deviceTypeList=deviceTypeService.selectAllDeviceType();

        model.addAttribute("lineList",lineList);
        model.addAttribute("stationList",stationList);
        model.addAttribute("deviceList",deviceList);
        model.addAttribute("deviceTypeList",deviceTypeList);
        return "rightContent/reportForm/repair";
    }

    @RequestMapping("/selectStatistics")
    @ResponseBody
    public Map<String,Object> selectStatistics(@RequestParam(defaultValue = "1") Integer pageNum,
                                               ModelAndView mv, DevRepairVo devRepair){
        try {
            PageUtil pageUtil=devRepairService.repairPaging(devRepair,pageNum,10);
            mv.addObject("pageUtil",pageUtil);
        }catch (Exception e){
            e.printStackTrace();
        }

        return mv.getModel();
    }

    @RequestMapping("/selectDevRepair")
    @ResponseBody
    public Map<String,Object> selectDevRepair(ModelAndView mv,Integer id,
                                              @RequestParam(defaultValue = "1") Integer pageNum){
        Device device=deviceService.selectDevice(id);
        PageUtil pageUtil=devRepairService.selectDevRepairByEndTimeNotNull(id,pageNum,6);
        mv.addObject("device",device);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/getDevRepair")
    @ResponseBody
    public Map<String,Object> getDevRepair(ModelAndView mv,DevRepairVo devRepair){
        try {
            List<Map<String,String>> devRepairVoList=devRepairService.getRepair(devRepair);
            mv.addObject("devRepairVoList",devRepairVoList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/addDevRepair")
    @ResponseBody
    public Map<String,Object> addDevRepair(ModelAndView mv, DevRepair devRepair, HttpSession session){
        Users users=(Users) session.getAttribute("user");
        try {
            Integer i=devRepairService.addDevRepair(devRepair);
            if(i>0){
                //日志记录
                Device device=deviceService.selectDevice(devRepair.getDeviceID());
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("设备监控");
                operationLog.setContent("用户("+users.getName()+") 添加设备维护("+device.getName()+")!");
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

    @RequestMapping("/updateDevRepair")
    @ResponseBody
    public Map<String,Object> updateDevRepair(ModelAndView mv,DevRepair devRepair,HttpSession session){
        Users users=(Users) session.getAttribute("user");
        try {
            Integer i=devRepairService.updateDevRepair(devRepair);
            if(i>0){
                //日志记录
                Device device=deviceService.selectDevice(devRepair.getDeviceID());
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("设备监控");
                operationLog.setContent("用户("+users.getName()+") 结束设备维护("+device.getName()+")!");
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
