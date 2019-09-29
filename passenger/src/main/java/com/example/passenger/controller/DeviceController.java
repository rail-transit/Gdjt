package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.DeviceVo;
import com.example.passenger.service.*;
import com.example.passenger.utils.MsgSend;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RequestMapping("/Device")
@Controller
@EnableAutoConfiguration
public class DeviceController{
    @Autowired
    DeviceService deviceService;

    @Autowired
    DevicePosService devicePosService;

    @Autowired
    DeviceSpotService deviceSpotService ;

    @Autowired
    DeviceTypeService deviceTypeService;

    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;


    @Autowired
    private MsgSend send;

    @RequestMapping("/selectDevice")
    @ResponseBody
    public Map<String,Object> selectDevice(ModelAndView mv,Integer id){
        Device device=deviceService.selectDevice(id);
        mv.addObject("device",device);
        return mv.getModel();
    }

    @RequestMapping("/getType")
    @ResponseBody
    public Map<String,Object> getType(ModelAndView mv,Integer stationID){
        List<Line> lineList=lineService.selectAllLine();
        Station station=stationService.selectStation(stationID);
        List<DevicePos> devicePosList=devicePosService.selectAllDevicePos();
        List<DeviceType> deviceTypeList=deviceTypeService.selectAllDeviceType();
        mv.addObject("station",station);
        mv.addObject("lineList",lineList);
        mv.addObject("devicePosList",devicePosList);
        mv.addObject("deviceTypeList",deviceTypeList);
        return mv.getModel();
    }

    @RequestMapping("/videoSurveillanceManagement")
    public String videoSurveillanceManagement(HttpSession session,Model model, Integer id){
        List<String> list=(ArrayList < String >)session.getAttribute("deviceList");
        if(id!=null){
            list.add(id.toString());
        }
        //去重list
        Set set = new  HashSet();
        List<String> newList = new  ArrayList();
        for (String cd:list) {
            if(set.add(cd)){
                newList.add(cd);
            }
        }
        session.setAttribute("deviceList",newList);
        return "rightContent/equipmentControl/videoSurveillance";
    }

    @RequestMapping("/getVideo")
    @ResponseBody
    public Map<String,Object> getVideo(ModelAndView mv,HttpSession session){
        List<String> list=(ArrayList < String >)session.getAttribute("deviceList");
        List<DeviceVo> deviceList=new ArrayList<>();
        for (String s:list){
            //获取设备信息
            Device device=deviceService.selectDevice(Integer.parseInt(s));
            DeviceVo deviceVo=deviceVo(device);
            //获取车站名称
            Station station=stationService.selectStation(device.getStationID());
            deviceVo.setStationName(station.getName());
            //获取线路名称
            Line line=lineService.selectLine(device.getLineID());
            deviceVo.setLineName(line.getName());
            deviceList.add(deviceVo);
        }
        List<Line> line=lineService.selectAllLine();
        mv.addObject("deviceList",deviceList);
        mv.addObject("line",line);
        return mv.getModel();
    }

    @RequestMapping("/deleteImage")
    @ResponseBody
    public Map<String,Object> deleteImage(ModelAndView mv,String imagePath){
        try {
            String path=imagePath.substring(imagePath.lastIndexOf("Path/")+5);
            File file = new File("D://ftp/"+path);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/openPlay")
    @ResponseBody
    public Map<String,Object> openPlay(ModelAndView mv,String deviceID){
        /*String smg="CAPT:<MSG>" +
                "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
                "<Type>5</Type>" +
                "<Info><ID>1025</ID>" +
                "<State>0</State>" +
                "<Fps>50</Fps>" +
                "<FtpUserName>ftp</FtpUserName>" +
                "<FtpUserPwd>123456</FtpUserPwd>" +
                "<FtpPath>ftp://10.1.9.168</FtpPath>" +
                "</Info></MSG>";
        send.sendMsg(deviceID,smg);*/
        return mv.getModel();
    }

    @RequestMapping("/deletePlaylist")
    public String deletePlaylist(HttpSession session,String deviceID){
        List<String> list=(ArrayList < String >)session.getAttribute("deviceList");
        for(int i = list.size() - 1; i >= 0; i--){
            String item = list.get(i);
            if(deviceID.equals(item)){
                list.remove(item);
             }
        }
        session.setAttribute("deviceList",list);
        return "redirect:/Device/videoSurveillanceManagement";
    }


    @RequestMapping("/equipmentMonitorManagement")
    public String equipmentMonitorManagement(Model model,Integer id){
        List<Station> stationList=stationService.queryAllStation();
        List<DeviceType> deviceTypeList=deviceTypeService.selectAllDeviceType();
        //List<Device> deviceList=deviceService.selectAllDevice();
        model.addAttribute("stationList",stationList);
        model.addAttribute("deviceTypeList",deviceTypeList);
        return "rightContent/equipmentControl/equipmentMonitor";
    }

    @RequestMapping("/selectMonitor")
    @ResponseBody
    public Map<String,Object> selectMonitor(ModelAndView mv,Integer stationID,Integer type,
                                            @RequestParam(defaultValue = "1") Integer pageNum){
        PageUtil pageUtil=deviceService.selectDevicePaging(stationID,type,pageNum,2);
        List<Line> lineList=lineService.selectAllLine();
        List<DevicePos> devicePosList=devicePosService.selectAllDevicePos();
        List<Station> stationList=stationService.queryAllStation();
        List<DeviceSpot> deviceSpotList=deviceSpotService.selectDeviceSpot(null);
        List<Map<String,String>> list=new ArrayList<>();
        for(int i=0;i<pageUtil.getPageData().size();i++){
            Device device= (Device) pageUtil.getPageData().get(i);
            List<DeviceSpot> deviceSpotList1=deviceSpotService.selectDeviceSpot(device.getType());
            Map<String,String> map=new HashMap<>();
            for(int j=0;j<deviceSpotList1.size();j++){
                map.put("state","正在加载....");
                map.put("deviceID",device.getId().toString());
                map.put("para"+j,"正在加载....");
            }
            list.add(map);
        }
        mv.addObject("list",list);
        mv.addObject("deviceSpotList",deviceSpotList);
        mv.addObject("stationList",stationList);
        mv.addObject("lineList",lineList);
        mv.addObject("devicePosList",devicePosList);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }


    @RequestMapping("/selectMonitorType")
    @ResponseBody
    public Map<String,Object> selectMonitorType(ModelAndView mv,Integer id){
        Device device=deviceService.selectDevice(id);
        List<DeviceSpot> deviceSpotList1=deviceSpotService.selectDeviceSpot(device.getType());
        //snmp参数
        SnmpService snmpService = new SnmpService();
        SnmpModel snmpModel = new SnmpModel();
        snmpModel.setCommunityName("public");
        snmpModel.setHostIp(device.getIp());
        snmpModel.setPort(161);
        snmpModel.setVersion(1);
        Map<String,String> map=new HashMap<>();
        try {
            //是否连接
            if (snmpService.isEthernetConnection(snmpModel)==true){
                map.put("state","1");
            }else{
                map.put("state","0");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int j=0;j<deviceSpotList1.size();j++){
            DeviceSpot deviceSpot=deviceSpotList1.get(j);
            Integer para=snmpService.work(snmpModel,deviceSpot.getParameter());
            map.put("para"+j,para.toString());
        }
        mv.addObject("map",map);
        return mv.getModel();
    }

    @RequestMapping("/addDevice")
    @ResponseBody
    public Map<String,Object> addDevice(ModelAndView mv, Device device){
        device.setIsBackups("1111");
        Integer i=deviceService.addDevice(device);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    @RequestMapping("/updateDevice")
    @ResponseBody
    public Map<String,Object> updateDevice(ModelAndView mv,Device device){
        device.setIsBackups("1111");
        Integer i=deviceService.updateDevice(device);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    @RequestMapping("/deleteDevice")
    @ResponseBody
    public Map<String,Object> deleteDevice(ModelAndView mv,Integer id){
        Integer i=deviceService.deleteDevice(id);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    //转型
    private DeviceVo deviceVo(Device device){
        DeviceVo deviceVo=new DeviceVo();
        deviceVo.setId(device.getId());
        deviceVo.setName(device.getName());
        deviceVo.setStationID(device.getStationID());
        deviceVo.setLineID(device.getLineID());
        deviceVo.setDeviceID(device.getDeviceID());
        return deviceVo;
    }
}
