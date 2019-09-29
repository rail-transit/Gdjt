package com.example.passenger.controller;

import com.example.passenger.entity.DevRepair;
import com.example.passenger.entity.Device;
import com.example.passenger.service.DevRepairService;
import com.example.passenger.service.DeviceService;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/devRepair")
@Controller
public class DevRepairController {

    @Autowired
    DevRepairService devRepairService;

    @Autowired
    DeviceService deviceService;

    @RequestMapping("/devRepairManagement")
    public String devRepairManagement(Model model,Integer id){
        List<DevRepair> devRepairList=devRepairService.selectDevRepairByEndTimeIsNull();
        Device device=deviceService.selectDevice(id);
        model.addAttribute("devRepairList",devRepairList);
        model.addAttribute("device",device);
        return "rightContent/equipmentControl/equipmentRepair";
    }

    @RequestMapping("/selectDevRepair")
    @ResponseBody
    public Map<String,Object> selectDevRepair(ModelAndView mv,Integer id,
                                              @RequestParam(defaultValue = "1") Integer pageNum){
        Device device=deviceService.selectDevice(id);
        PageUtil pageUtil=devRepairService.selectDevRepairByEndTimeNotNull(pageNum,2);
        mv.addObject("device",device);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/addDevRepair")
    public String addDevRepair(DevRepair devRepair,Integer deviceID){
        Device device=deviceService.selectDevice(deviceID);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        devRepair.setStartTime(sdf.format(new Date()));
        devRepair.setDeviceID(device.getId());
        devRepair.setStationID(device.getStationID());
        devRepair.setLineID(device.getLineID());
        devRepairService.addDevRepair(devRepair);
        return "redirect:/devRepair/devRepairManagement?id="+ deviceID;
    }

    @RequestMapping("/updateDevRepair")
    public String updateDevRepair(DevRepair devRepair,Integer deviceID){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        devRepair.setEndTime(sdf.format(new Date()));
        devRepairService.updateDevRepair(devRepair);
        return "redirect:/devRepair/devRepairManagement?id="+ deviceID;
    }

}
