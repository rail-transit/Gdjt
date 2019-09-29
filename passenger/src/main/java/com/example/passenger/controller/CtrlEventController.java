package com.example.passenger.controller;

import com.example.passenger.entity.CtrlEvent;
import com.example.passenger.entity.Device;
import com.example.passenger.entity.DevicePos;
import com.example.passenger.entity.SnmpModel;
import com.example.passenger.service.CtrlEventService;
import com.example.passenger.service.DevicePosService;
import com.example.passenger.service.DeviceService;
import com.example.passenger.service.SnmpService;
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
import java.util.Map;

@RequestMapping("/ctrlEvent")
@Controller
public class CtrlEventController {

    @Autowired
    CtrlEventService ctrlEventService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    DevicePosService devicePosService;

    @RequestMapping("/equipmentControl")
    public String equipmentControl(Model model,Integer id){
        Device device=deviceService.selectDevice(id);
        SnmpService snmpService = new SnmpService();
        SnmpModel snmpModel = new SnmpModel();
        snmpModel.setCommunityName("public");
        snmpModel.setHostIp(device.getIp());
        snmpModel.setPort(161);
        snmpModel.setVersion(1);
        Integer volume=snmpService.setVolume(snmpModel,".1.3.6.1.4.1.15.0.0.1");
        model.addAttribute("device",device);
        model.addAttribute("volume",volume);
        return "rightContent/equipmentControl/upperPlayer";
    }

    @RequestMapping("/selectUpperPlayer")
    @ResponseBody
    public Map<String,Object> selectUpperPlayer(ModelAndView mv,Integer id,@RequestParam(defaultValue = "1")
            Integer pageNum){
        Device device=deviceService.selectDevice(id);
        PageUtil pageUtil =ctrlEventService.selectAllCtrlEvent(id,pageNum,2);
        DevicePos devicePos=devicePosService.selectDevicePosById(device.getDevicePosID());
        mv.addObject("device",device);
        mv.addObject("pageUtil",pageUtil);
        mv.addObject("devicePos",devicePos);
        return mv.getModel();
    }

    @RequestMapping("/addEquipmentControl")
    @ResponseBody
    public Map<String,Object> addEquipmentControl(ModelAndView mv,String type,Integer deviceID,String volume){
        try {
            Device device=deviceService.selectDevice(deviceID);
            //设置音量
            SnmpService snmpService = new SnmpService();
            SnmpModel snmpModel = new SnmpModel();
            snmpModel.setCommunityName("public");
            snmpModel.setHostIp(device.getIp());
            snmpModel.setPort(161);
            snmpModel.setVersion(1);
            snmpModel.setAsync(1);
            snmpModel.setParameter(volume);
            Integer volumes=snmpService.setVolume(snmpModel,".1.3.6.1.4.1.15.0.0.1");

            //添加记录
            CtrlEvent ctrlEvent=new CtrlEvent();
            ctrlEvent.setDeviceID(deviceID);
            ctrlEvent.setCtrlType(type);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ctrlEvent.setCtrlTime(sdf.format(new Date()));
            if(volumes>0){
                ctrlEvent.setCtrlResult("成功");
            }else{
                ctrlEvent.setCtrlResult("失败");
            }
            ctrlEvent.setStationID(device.getStationID());
            ctrlEvent.setLineID(device.getLineID());
            Integer i=ctrlEventService.insertCtrlEvent(ctrlEvent);
            if(i>0){
                mv.addObject("para",volumes);
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }catch (Exception e){
            mv.addObject("result","error");
        }

        return mv.getModel();
    }


    @RequestMapping("/openPlayer")
    @ResponseBody
    public Map<String,Object> openPlayer(ModelAndView mv,String type,Integer deviceID){
        try {
            Device device=deviceService.selectDevice(deviceID);
            //开启播放器
            SnmpService snmpService = new SnmpService();
            SnmpModel snmpModel = new SnmpModel();
            snmpModel.setCommunityName("public");
            snmpModel.setHostIp(device.getIp());
            snmpModel.setPort(161);
            snmpModel.setVersion(1);
            snmpModel.setAsync(1);
            snmpModel.setParameter("1");
            Integer i=snmpService.setVolume(snmpModel,".1.3.6.1.4.1.15.0.0.1");
            //创建对象
            CtrlEvent ctrlEvent=new CtrlEvent();
            if(i>0){
                ctrlEvent.setCtrlResult("成功");
            }else{
                ctrlEvent.setCtrlResult("失败");
            }
            //添加记录
            ctrlEvent.setDeviceID(deviceID);
            ctrlEvent.setCtrlType(type);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ctrlEvent.setCtrlTime(sdf.format(new Date()));
            ctrlEvent.setStationID(device.getStationID());
            ctrlEvent.setLineID(device.getLineID());
            Integer t=ctrlEventService.insertCtrlEvent(ctrlEvent);
            if(t>0){
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }catch (Exception e){
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    @RequestMapping("/closePlayer")
    @ResponseBody
    public Map<String,Object> closePlayer(ModelAndView mv,String type,Integer deviceID){
        try {
            Device device=deviceService.selectDevice(deviceID);
            //关闭播放器
            SnmpService snmpService = new SnmpService();
            SnmpModel snmpModel = new SnmpModel();
            snmpModel.setCommunityName("public");
            snmpModel.setHostIp(device.getIp());
            snmpModel.setPort(161);
            snmpModel.setVersion(1);
            snmpModel.setAsync(1);
            snmpModel.setParameter("1");
            Integer i=snmpService.setVolume(snmpModel,".1.3.6.1.4.1.15.0.0.3");
            //创建对象
            CtrlEvent ctrlEvent=new CtrlEvent();

            if(i>0){
                ctrlEvent.setCtrlResult("成功");
            }else{
                ctrlEvent.setCtrlResult("失败");
            }
            //添加记录
            ctrlEvent.setDeviceID(deviceID);
            ctrlEvent.setCtrlType(type);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ctrlEvent.setCtrlTime(sdf.format(new Date()));
            ctrlEvent.setStationID(device.getStationID());
            ctrlEvent.setLineID(device.getLineID());
            Integer t=ctrlEventService.insertCtrlEvent(ctrlEvent);
            if(t>0){
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }catch (Exception e){
            mv.addObject("result","error");
        }

        return mv.getModel();
    }


    @RequestMapping("/restartPlayer")
    @ResponseBody
    public Map<String,Object> restartPlayer(ModelAndView mv,String type,Integer deviceID){
        try {
            Device device=deviceService.selectDevice(deviceID);
            //重启播放器
            SnmpService snmpService = new SnmpService();
            SnmpModel snmpModel = new SnmpModel();
            snmpModel.setCommunityName("public");
            snmpModel.setHostIp(device.getIp());
            snmpModel.setPort(161);
            snmpModel.setVersion(1);
            snmpModel.setAsync(1);
            snmpModel.setParameter("1");
            Integer i=snmpService.setVolume(snmpModel,".1.3.6.1.4.1.15.0.0.2");
            CtrlEvent ctrlEvent=new CtrlEvent();
            if(i>0){
                ctrlEvent.setCtrlResult("成功");
            }else{
                ctrlEvent.setCtrlResult("失败");
            }
            //添加记录
            ctrlEvent.setDeviceID(deviceID);
            ctrlEvent.setCtrlType(type);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ctrlEvent.setCtrlTime(sdf.format(new Date()));
            ctrlEvent.setStationID(device.getStationID());
            ctrlEvent.setLineID(device.getLineID());
            Integer t=ctrlEventService.insertCtrlEvent(ctrlEvent);
            if(t>0){
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }catch (Exception e){
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
