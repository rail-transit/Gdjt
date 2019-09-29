package com.example.passenger.controller;

import com.example.passenger.entity.DevUpkeep;
import com.example.passenger.entity.Device;
import com.example.passenger.service.DevUpkeepService;
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

@RequestMapping("/devUpkeep")
@Controller
public class DevUpkeepController {

    @Autowired
    DevUpkeepService devUpkeepService;

    @Autowired
    DeviceService deviceService;

    @RequestMapping("/devUpkeepManagement")
    public String devUpkeepManagement(Model model,Integer id){
        List<DevUpkeep> devUpkeepList=devUpkeepService.selectDevUpkeepByEndTimeIsNull();
        Device device=deviceService.selectDevice(id);
        model.addAttribute("device",device);
        model.addAttribute("devUpkeepList",devUpkeepList);
        return "rightContent/equipmentControl/equipmentMaintain";
    }

    @RequestMapping("/selectDevUpkeep")
    @ResponseBody
    public Map<String,Object> selectDevUpkeep(ModelAndView mv,Integer id,
                                              @RequestParam(defaultValue = "1") Integer pageNum){
        Device device=deviceService.selectDevice(id);
        PageUtil pageUtil=devUpkeepService.selectDevUpkeepByEndTimeNotNull(pageNum,2);
        mv.addObject("device",device);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/addDevUpkeep")
    public String addDevUpkeep(DevUpkeep devUpkeep,Integer deviceID){
        Device device=deviceService.selectDevice(deviceID);
        devUpkeep.setDeviceID(device.getId());
        devUpkeep.setStationID(device.getStationID());
        devUpkeep.setLineID(device.getLineID());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        devUpkeep.setStartTime(sdf.format(new Date()));
        devUpkeepService.addDevUpkeep(devUpkeep);
        return "redirect:/devUpkeep/devUpkeepManagement?id="+deviceID;
    }

    @RequestMapping("/updateDevUpkeep")
    public String updateDevUpkeep(DevUpkeep devUpkeep,Integer deviceID){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        devUpkeep.setEndTime(sdf.format(new Date()));
        devUpkeepService.updateDevUpkeep(devUpkeep);
        return "redirect:/devUpkeep/devUpkeepManagement?id="+deviceID;
    }
}
