package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.DeviceVo;
import com.example.passenger.entity.vo.EventStateVo;
import com.example.passenger.entity.vo.LineStandbyVo;
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
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/ctrlEvent")
@Controller
public class CtrlEventController {

    @Autowired
    CtrlEventService ctrlEventService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    DevicePosService devicePosService;
    @Autowired
    DeviceTypeService deviceTypeService;
    @Autowired
    DeviceSpotService deviceSpotService ;
    @Autowired
    LineService lineService;
    @Autowired
    StationService stationService;
    @Autowired
    OperationLogService operationLogService;
    @Autowired
    LineStandbyService lineStandbyService;
    @Autowired
    DevAlarmLogService devAlarmLogService;

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

    @RequestMapping("/controlStatistics")
    public String controlStatistics(Model model){
        List<Line> lineList=lineService.selectAllLine();
        List<Station> stationList=stationService.queryAllStation();
        List<Device> deviceList=deviceService.queryAllDevice();
        List<DeviceType> deviceTypeList=deviceTypeService.selectAllDeviceType();

        model.addAttribute("lineList",lineList);
        model.addAttribute("stationList",stationList);
        model.addAttribute("deviceList",deviceList);
        model.addAttribute("deviceTypeList",deviceTypeList);
        return "rightContent/reportForm/control";
    }



    @RequestMapping("getEventState")
    @ResponseBody
    public Map<String,Object> getEventState(ModelAndView mv, HttpSession session){
        List<Map<String,String>> eventStateVoList1=(ArrayList<Map<String,String>>)session.getAttribute("mapList");
        List<DeviceVo> deviceVos=deviceService.queryAllDeviceVo();
        List<LineStandbyVo> lineStandbyVos=lineStandbyService.queryAllStandbyVo();

        List<EventStateVo> eventStateVos=(ArrayList<EventStateVo>)session.getAttribute("eventList");
        List<EventStateVo> eventStateVoList=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        List<Map<String,String>> mapList=new ArrayList<>();
        try {
            for (DeviceVo deviceVo:deviceVos){
                SnmpService snmpService = new SnmpService();
                SnmpModel snmpModel = new SnmpModel();
                snmpModel.setCommunityName("public");
                snmpModel.setHostIp(deviceVo.getIp());
                snmpModel.setPort(161);
                snmpModel.setVersion(1);
                //初始化状态类
                EventStateVo eventStateVo=new EventStateVo();
                eventStateVo.setDeviceID(deviceVo.getId());
                eventStateVo.setStationID(deviceVo.getStationID());
                eventStateVo.setLineID(deviceVo.getLineID());
                eventStateVo.setLineName(deviceVo.getLineName());
                eventStateVo.setStationName(deviceVo.getStationName());
                eventStateVo.setDeviceName(deviceVo.getName());
                eventStateVo.setEventTime(sdf.format(new Date()));

                //初始化告警对象
                DevAlarmLog devAlarmLog=new DevAlarmLog();
                Map<String,String> map=new HashMap<>();
                if (snmpService.isEthernetConnection(snmpModel)==false){
                    eventStateVo.setState(1);
                    eventStateVo.setEventContent("设备通信中断!");
                    //添加故障记录
                    devAlarmLog.setDeviceID(deviceVo.getId().toString());
                    devAlarmLog.setDeviceName(deviceVo.getName());
                    devAlarmLog.setAlarmType("0");
                    devAlarmLog.setSpotID("0");
                    devAlarmLog.setSpotName("0");
                    devAlarmLog.setValue("0");
                    devAlarmLog.setEvent("设备连接超时");
                    devAlarmLog.setTimeStamp(sdf.format(new Date()));
                    devAlarmLog.setLineID(deviceVo.getLineID().toString());
                    devAlarmLog.setStationID(deviceVo.getStationID().toString());
                    devAlarmLogService.addDevAlarmLog(devAlarmLog);
                    map.put(deviceVo.getId().toString(),"1");
                    if(eventStateVoList1.size()==0){
                        eventStateVos.add(eventStateVo);
                        eventStateVoList.add(eventStateVo);
                    }else{
                        for (Map<String,String> map1:eventStateVoList1){
                            if(map1.get(deviceVo.getId().toString())=="0"){
                                eventStateVos.add(eventStateVo);
                                eventStateVoList.add(eventStateVo);
                            }
                        }
                    }
                }else{
                    eventStateVo.setState(0);
                    eventStateVo.setEventContent("设备通讯正常");
                    map.put(deviceVo.getId().toString(),"0");
                    if(eventStateVoList1.size()==0){
                        eventStateVos.add(eventStateVo);
                        eventStateVoList.add(eventStateVo);
                    }else{
                        for (Map<String,String> map1:eventStateVoList1){
                            if(map1.get(deviceVo.getId().toString())=="1"){
                                eventStateVos.add(eventStateVo);
                                eventStateVoList.add(eventStateVo);
                            }
                        }
                    }
                }
                mapList.add(map);
                session.setAttribute("mapList",mapList);
            }


            for (DeviceVo deviceVo:deviceVos){
                SnmpService snmpService = new SnmpService();
                SnmpModel snmpModel = new SnmpModel();
                snmpModel.setCommunityName("public");
                snmpModel.setHostIp(deviceVo.getIp());
                snmpModel.setPort(161);
                snmpModel.setVersion(1);
                //初始化告警对象
                DevAlarmLog devAlarmLog=new DevAlarmLog();
                if (snmpService.isEthernetConnection(snmpModel)==true){
                    List<DeviceSpot> deviceSpotList=deviceSpotService.selectDeviceSpot(deviceVo.getType());
                    for(int j=0;j<deviceSpotList.size();j++){
                        DeviceSpot deviceSpot=deviceSpotList.get(j);
                        Integer para=snmpService.work(snmpModel,deviceSpot.getParameter());
                        //添加告警记录
                        devAlarmLog.setDeviceID(deviceVo.getId().toString());
                        devAlarmLog.setDeviceName(deviceVo.getName());
                        devAlarmLog.setAlarmType("1");
                        devAlarmLog.setSpotID(deviceSpot.getId().toString());
                        devAlarmLog.setSpotName(deviceSpot.getName());
                        devAlarmLog.setValue(para.toString());
                        devAlarmLog.setTimeStamp(sdf.format(new Date()));
                        devAlarmLog.setLineID(deviceVo.getLineID().toString());
                        devAlarmLog.setStationID(deviceVo.getStationID().toString());

                        //初始化状态类
                        EventStateVo eventStateVo=new EventStateVo();
                        eventStateVo.setDeviceID(deviceVo.getId());
                        eventStateVo.setStationID(deviceVo.getStationID());
                        eventStateVo.setLineID(deviceVo.getLineID());
                        eventStateVo.setLineName(deviceVo.getLineName());
                        eventStateVo.setStationName(deviceVo.getStationName());
                        eventStateVo.setDeviceName(deviceVo.getName());
                        eventStateVo.setEventTime(sdf.format(new Date()));

                        if(para<deviceSpot.getMin()){
                            eventStateVo.setState(2);
                            eventStateVo.setEventContent("当前"+deviceSpot.getName()+"小于预警值"+"当前值为"+para);

                            devAlarmLog.setEvent("当前"+deviceSpot.getName()+"小于预警值"+"当前值为"+para);
                            devAlarmLogService.addDevAlarmLog(devAlarmLog);
                            eventStateVos.add(eventStateVo);
                            eventStateVoList.add(eventStateVo);
                        }else if(para>deviceSpot.getMax()){
                            eventStateVo.setState(2);
                            eventStateVo.setEventContent("当前"+deviceSpot.getName()+"大于预警值"+"当前值为"+para);

                            devAlarmLog.setEvent("当前"+deviceSpot.getName()+"小于预警值"+"当前值为"+para);
                            devAlarmLogService.addDevAlarmLog(devAlarmLog);
                            eventStateVos.add(eventStateVo);
                            eventStateVoList.add(eventStateVo);
                        }
                    }
                }
            }

            List<EventStateVo> lineStandbyList=new ArrayList<>();
            for (LineStandbyVo lineStandbyVo:lineStandbyVos){
                EventStateVo eventStateVo=new EventStateVo();
                eventStateVo.setLineName(lineStandbyVo.getLineName());
                eventStateVo.setStationName("备件("+lineStandbyVo.getStandbyName()+")");
                eventStateVo.setDeviceName("备件("+lineStandbyVo.getStandbyName()+")");
                eventStateVo.setEventContent("可用数量少于预警数量,当前可用数量为"+lineStandbyVo.getAvailableNumber()+",请备货!");
                eventStateVo.setEventTime(sdf.format(new Date()));
                lineStandbyList.add(eventStateVo);
            }
            session.setAttribute("deviceState",eventStateVoList);
            session.setAttribute("eventList",eventStateVos);

            mv.addObject("eventStateVos",eventStateVos);
            mv.addObject("lineStandbyList",lineStandbyList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/selectStatistics")
    @ResponseBody
    public Map<String,Object> selectStatistics(@RequestParam(defaultValue = "1") Integer pageNum,
                                               ModelAndView mv,Integer lineID,Integer stationID,
                                               Integer deviceID,Integer type, String startDate,String endDate){
        try {
            PageUtil pageUtil=ctrlEventService.controlPaging(lineID,stationID,deviceID,type,
                    startDate,endDate,pageNum,10);
            mv.addObject("pageUtil",pageUtil);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/selectUpperPlayer")
    @ResponseBody
    public Map<String,Object> selectUpperPlayer(ModelAndView mv,Integer id,@RequestParam(defaultValue = "1")
            Integer pageNum){
        PageUtil pageUtil =ctrlEventService.selectAllCtrlEvent(id,pageNum,9);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/addEquipmentControl")
    @ResponseBody
    public Map<String,Object> addEquipmentControl(ModelAndView mv,String type,Integer deviceID,
                                                  String volume,HttpSession session){
        try {
            //日志记录
            Users users=(Users) session.getAttribute("user");
            OperationLog operationLog=new OperationLog();
            operationLog.setOperator(users.getId().toString());
            operationLog.setType("设备监控");

            Device device=deviceService.selectDevice(deviceID);
            CtrlEvent ctrlEvent=new CtrlEvent();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ctrlEvent.setCtrlTime(sdf.format(new Date())); ctrlEvent.setDeviceID(deviceID);
            ctrlEvent.setStationID(device.getStationID());
            ctrlEvent.setLineID(device.getLineID());

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
            if (volumes==-1){
                mv.addObject("result","interrupt");
                ctrlEvent.setCtrlType(type);
                ctrlEvent.setCtrlResult("失败");
                ctrlEventService.insertCtrlEvent(ctrlEvent);
                operationLog.setContent("用户("+users.getName()+")操作"+device.getName()+"设置音量("+volumes+")失败!");
                return mv.getModel();
            }else{
                //添加记录
                ctrlEvent.setCtrlType(type+"("+volumes+")");
                if(volumes==Integer.valueOf(volume)){
                    ctrlEvent.setCtrlResult("成功");
                    operationLog.setContent("用户("+users.getName()+")操作"+device.getName()+"设置音量("+volumes+")成功!");
                }else{
                    ctrlEvent.setCtrlResult("失败");
                }
                ctrlEventService.insertCtrlEvent(ctrlEvent);
                mv.addObject("para",volumes);
                mv.addObject("result","success");
            }
            operationLogService.addOperationLog(operationLog);
        }catch (Exception e){
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/operation")
    @ResponseBody
    public Map<String,Object> operation(ModelAndView mv,String type,Integer deviceID,HttpSession session){
        try {
            //日志记录
            Users users=(Users) session.getAttribute("user");
            OperationLog operationLog=new OperationLog();
            operationLog.setOperator(users.getId().toString());
            operationLog.setType("设备监控");

            Device device=deviceService.selectDevice(deviceID);
            //创建对象
            CtrlEvent ctrlEvent=new CtrlEvent();
            //关闭播放器
            SnmpService snmpService = new SnmpService();
            SnmpModel snmpModel = new SnmpModel();
            snmpModel.setCommunityName("public");
            snmpModel.setHostIp(device.getIp());
            snmpModel.setPort(161);
            snmpModel.setVersion(1);
            snmpModel.setAsync(1);
            snmpModel.setParameter("1");
            String oid=null;
            if (type.equals("关闭播放器")){
                oid=".1.3.6.1.4.1.15.0.0.3";
            }else if(type.equals("重启播放器")){
                oid=".1.3.6.1.4.1.15.0.0.2";
            }else if(type.equals("静音")){
                snmpModel.setParameter("-1");
                oid=".1.3.6.1.4.1.15.0.0.1";
            }else if(type.equals("取消静音")){
                snmpModel.setParameter("-2");
                oid=".1.3.6.1.4.1.15.0.0.1";
            }
            else if (type.equals("打开播放器"))
            {
                //根据端口和MAC地址唤醒(开机)对应设备
                SnmpService.wake(90,device.getMac());

                //添加记录
                ctrlEvent.setCtrlResult("成功");
                ctrlEvent.setDeviceID(deviceID);
                ctrlEvent.setCtrlType(type);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ctrlEvent.setCtrlTime(sdf.format(new Date()));
                ctrlEvent.setStationID(device.getStationID());
                ctrlEvent.setLineID(device.getLineID());
                ctrlEventService.insertCtrlEvent(ctrlEvent);
                mv.addObject("result","success");
                return mv.getModel();
            }
            Integer i=snmpService.setVolume(snmpModel,oid);
            if (i==-1){
                //添加记录
                ctrlEvent.setCtrlResult("失败");
                ctrlEvent.setDeviceID(deviceID);
                ctrlEvent.setCtrlType(type);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ctrlEvent.setCtrlTime(sdf.format(new Date()));
                ctrlEvent.setStationID(device.getStationID());
                ctrlEvent.setLineID(device.getLineID());
                ctrlEventService.insertCtrlEvent(ctrlEvent);
                mv.addObject("result","interrupt");
                operationLog.setContent("用户("+users.getName()+")操作"+device.getName()+""+type+"失败!");
            }else{
                //添加记录
                ctrlEvent.setCtrlResult("成功");
                ctrlEvent.setDeviceID(deviceID);
                ctrlEvent.setCtrlType(type);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ctrlEvent.setCtrlTime(sdf.format(new Date()));
                ctrlEvent.setStationID(device.getStationID());
                ctrlEvent.setLineID(device.getLineID());
                ctrlEventService.insertCtrlEvent(ctrlEvent);
                operationLog.setContent("用户("+users.getName()+")操作"+device.getName()+""+type+"成功!");
                mv.addObject("result","success");
            }
            operationLogService.addOperationLog(operationLog);
        }catch (Exception e){
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/getControl")
    @ResponseBody
    public Map<String,Object> getControl(ModelAndView mv,Integer lineID,Integer stationID,
                                         Integer deviceID,Integer type,String startDate,String endDate){
        try {
            List<Map<String,String>> ctrlEventVoList=ctrlEventService.getControl(lineID,stationID,deviceID,type,startDate,endDate);
            mv.addObject("ctrlEventVoList",ctrlEventVoList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/getEventTime")
    @ResponseBody
    public Map<String,Object> getEventTime(ModelAndView mv){
        String time=ctrlEventService.getEventTime();
        mv.addObject("time",time);
        return mv.getModel();
    }
}
