package com.example.passenger.controller;

import com.example.passenger.config.AmqpConfig;
import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.DeviceVo;
import com.example.passenger.service.*;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MsgSend;
import com.example.passenger.utils.PageUtil;
import com.example.passenger.utils.VisitCount;
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
    OperationLogService operationLogService;
    @Autowired
    private MsgSend send;

    public static List<VisitCount> visitCountList=new ArrayList<>();
    public Integer count=0;

    @RequestMapping("/selectDevice")
    @ResponseBody
    public Map<String,Object> selectDevice(ModelAndView mv,Integer id){
        Device device=deviceService.selectDevice(id);
        mv.addObject("device",device);
        return mv.getModel();
    }

    @RequestMapping("/getDevice")
    @ResponseBody
    public Map<String,Object> getDevice(ModelAndView mv,Integer lineID,Integer stationID,
                                           Integer type){
        List<Device> deviceList=deviceService.selectDeviceById(lineID,stationID,type);
        mv.addObject("deviceList",deviceList);
        return mv.getModel();
    }

    @RequestMapping("/selectAllDevice")
    @ResponseBody
    public Map<String,Object> selectAllDevice(ModelAndView mv,Integer stationID){
        List<Device> deviceList=deviceService.selectAllDevice(stationID);
        mv.addObject("deviceList",deviceList);
        return mv.getModel();
    }

    @RequestMapping("/getType")
    @ResponseBody
    public Map<String,Object> getType(ModelAndView mv,Integer lineID){
        List<Line> lineList=lineService.selectAllLine();
        List<Station> stationList=stationService.selectAllStation(lineID);
        List<Station> stations=stationService.selectAllTrain(lineID);
        List<DevicePos> devicePosList=devicePosService.selectAllDevicePos();
        List<DeviceType> deviceTypeList=deviceTypeService.selectAllDeviceType();
        mv.addObject("lineList",lineList);
        mv.addObject("stations",stations);
        mv.addObject("stationList",stationList);
        mv.addObject("devicePosList",devicePosList);
        mv.addObject("deviceTypeList",deviceTypeList);
        return mv.getModel();
    }

    @RequestMapping("/videoSurveillanceManagement")
    public String videoSurveillanceManagement(){
        return "rightContent/equipmentControl/videoSurveillance";
    }

    @RequestMapping("/addVideoList")
    @ResponseBody
    public Map<String,Object> addVideoList(ModelAndView mv,HttpSession session, Integer id){
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
        return mv.getModel();
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
            String imageName=imagePath.substring(imagePath.lastIndexOf("Path/")+5);
            String path=imageName.substring(0,imageName.indexOf("/"));
            File file = new File("D://ftp/"+path);
            if(file.exists()) {
                File [] files = file.listFiles();
                if(files.length>5){
                    File file1 = new File("D://ftp/"+imageName);
                    if(file1.exists()){
                        file1.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/getImage")
    @ResponseBody
    public Map<String,Object> getImage(ModelAndView mv,String imagePath){
        List<VisitCount> visitCounts=visitCountList;
        try {
            String imgName=null;
            File file = new File("D://ftp/"+imagePath);
            if(file.exists()) {
                File [] files = file.listFiles();
                if(files.length!=0){
                    imgName=files[0].getName();
                }
                if(files.length>50){
                    for (int i = 0; i < 45; i++){
                        files[i].delete();
                    }
                }
            }

            for (int i=0;i<visitCounts.size();i++){
                if(visitCounts.get(i).getDeviceID()==Integer.parseInt(imagePath)){
                    Device device=deviceService.selectDevice(Integer.parseInt(imagePath));
                    Station station=stationService.selectStation(device.getStationID());
                    Line line=lineService.selectLine(device.getLineID());
                    String alarmName=line.getName()+"-"+station.getName()+"-"+device.getName();
                    Integer repeatCount=visitCounts.get(i).getRepeatCount();
                    Integer number=visitCounts.get(i).getAlarmNumber();
                    Integer count=visitCounts.get(i).getCount();
                    if(imgName==null){
                        number++;
                        visitCounts.get(i).setAlarmNumber(number);
                        if (number>10){
                            if(count>0){
                                count--;
                                visitCounts.get(i).setCount(count);
                            }
                            mv.addObject("alarmName",alarmName);
                            mv.addObject("result","connectError");
                            return mv.getModel();
                        }
                    }else{
                        visitCounts.get(i).setAlarmNumber(0);
                        if(visitCounts.get(i).getFileName().equals(imgName)){
                            repeatCount++;
                            visitCounts.get(i).setRepeatCount(repeatCount);
                            if(repeatCount>10){
                                if(count>0){
                                    count--;
                                    visitCounts.get(i).setCount(count);
                                }
                                mv.addObject("alarmName",alarmName);
                                mv.addObject("result","interrupt");
                                return mv.getModel();
                            }
                        }else{
                            visitCounts.get(i).setRepeatCount(0);
                        }
                        visitCounts.get(i).setFileName(imgName);
                    }
                }
            }
            mv.addObject("img",imgName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/openPlay")
    @ResponseBody
    public Map<String,Object> openPlay(ModelAndView mv,String deviceID,Integer state){
        List<VisitCount> visitCounts=visitCountList;
        try {
            if(visitCounts.size()==0){
                VisitCount visitCount=new VisitCount();
                visitCount.setCount(1);
                visitCount.setAlarmNumber(0);
                visitCount.setRepeatCount(0);
                visitCount.setFileName("");
                visitCount.setDeviceID(Integer.parseInt(deviceID));
                visitCounts.add(visitCount);
            }else{
                Boolean bool=false;
                for (int i=0;i<visitCounts.size();i++){
                    if(visitCounts.get(i).getDeviceID()==Integer.parseInt(deviceID)){
                        if (state==1){
                            Integer count=visitCounts.get(i).getCount();
                            count++;
                            visitCounts.get(i).setCount(count);
                        }else{
                            Integer count=visitCounts.get(i).getCount();
                            count--;
                            visitCounts.get(i).setCount(count);
                        }
                        bool=true;
                        break;
                    }
                }
                if(bool==false){
                    VisitCount visitCount=new VisitCount();
                    visitCount.setCount(1);
                    visitCount.setAlarmNumber(0);
                    visitCount.setRepeatCount(0);
                    visitCount.setFileName("");
                    visitCount.setDeviceID(Integer.parseInt(deviceID));
                    visitCounts.add(visitCount);
                }
            }
            String smg="CAPT:<MSG>" +
                    "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
                    "<Type>5</Type>" +
                    "<Info><ID>"+deviceID+"</ID>" +
                    "<State>1</State>" +
                    "<Fps>50</Fps>" +
                    "<FtpUserName>ftp</FtpUserName>" +
                    "<FtpUserPwd>123456</FtpUserPwd>" +
                    "<FtpPath>ftp://"+ IPUtil.ip +"</FtpPath>" +
                    "</Info></MSG>";
            send.sendMsg(deviceID,smg);
            mv.addObject("result","success");
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deletePlaylist")
    @ResponseBody
    public Map<String,Object> deletePlaylist(ModelAndView mv,HttpSession session,String deviceID){
        List<String> list=(ArrayList < String >)session.getAttribute("deviceList");
        List<VisitCount> visitCounts=visitCountList;
        try {
            for(int i = list.size() - 1; i >= 0; i--){
                String item = list.get(i);
                if(deviceID.equals(item)){
                    list.remove(item);
                }
            }
            session.setAttribute("deviceList",list);
            for (int i=0;i<visitCounts.size();i++){
                if(visitCounts.get(i).getDeviceID()==Integer.parseInt(deviceID)){
                    Integer count=visitCounts.get(i).getCount();
                    if (count>0){
                        count--;
                        visitCounts.get(i).setCount(count);
                    }
                    if(count<1){
                        String smg="CAPT:<MSG>" +
                                "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
                                "<Type>5</Type>" +
                                "<Info><ID>"+deviceID+"</ID>" +
                                "<State>0</State>" +
                                "<Fps>50</Fps>" +
                                "<FtpUserName>ftp</FtpUserName>" +
                                "<FtpUserPwd>123456</FtpUserPwd>" +
                                "<FtpPath>ftp://"+IPUtil.ip+"</FtpPath>" +
                                "</Info></MSG>";
                        send.sendMsg(deviceID,smg);
                        File file = new File("D://ftp/"+deviceID);
                        if (file.exists()) {
                            File [] files = file.listFiles();
                            for (int j = 0; j < files.length; j++){
                                files[j].delete();
                            }
                        }
                    }
                }
            }
            mv.addObject("deviceListCount",list.size());
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/closePrisonWatch")
    @ResponseBody
    public Map<String,Object> closePrisonWatch(ModelAndView mv,HttpSession session){
        List<String> list=(ArrayList < String >)session.getAttribute("deviceList");
        List<VisitCount> visitCounts=visitCountList;
        for(int i = list.size() - 1; i >= 0; i--){
            String deviceID = list.get(i);
            for (int k=0;k<visitCounts.size();k++){
                if(visitCounts.get(k).getDeviceID()==Integer.parseInt(deviceID)){
                    Integer count=visitCounts.get(k).getCount();
                    if (count>0){
                        count--;
                        visitCounts.get(k).setCount(count);
                    }
                    if(count<1){
                        String smg="CAPT:<MSG>" +
                                "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
                                "<Type>5</Type>" +
                                "<Info><ID>"+deviceID+"</ID>" +
                                "<State>0</State>" +
                                "<Fps>50</Fps>" +
                                "<FtpUserName>ftp</FtpUserName>" +
                                "<FtpUserPwd>123456</FtpUserPwd>" +
                                "<FtpPath>ftp://"+IPUtil.ip+"</FtpPath>" +
                                "</Info></MSG>";
                        send.sendMsg(deviceID,smg);
                        System.out.println("清除:"+smg);
                        File file = new File("D://ftp/"+deviceID);
                        if (file.exists()) {
                            File [] files = file.listFiles();
                            for (int j = 0;j < files.length; j++){
                                files[j].delete();
                            }
                        }
                    }
                }
            }
        }
        return mv.getModel();
    }


    @RequestMapping("/equipmentMonitorManagement")
    public String equipmentMonitorManagement(Model model,Integer id){
        Device device=deviceService.selectDevice(id);
        Station station=stationService.selectStation(device.getStationID());
        Line line=lineService.selectLine(station.getLineID());

        List<Line> lineList=lineService.selectAllLine();
        List<DeviceType> deviceTypeList=deviceTypeService.selectAllDeviceType();
        List<Station> stationList=stationService.queryAllStation();

        model.addAttribute("id",device.getStationID());
        model.addAttribute("line",line);
        model.addAttribute("lineList",lineList);
        model.addAttribute("stationList",stationList);
        model.addAttribute("deviceTypeList",deviceTypeList);
        return "rightContent/equipmentControl/equipmentMonitor";
    }

    @RequestMapping("/lineManagement")
    public String lineManagement(Model model,Integer id){
        List<Line> lineList=lineService.selectAllLine();
        List<Station> stationList=stationService.queryAllStation();
        List<DeviceType> deviceTypeList=deviceTypeService.selectAllDeviceType();

        model.addAttribute("id",id);
        model.addAttribute("line",null);
        model.addAttribute("lineList",lineList);
        model.addAttribute("stationList",stationList);
        model.addAttribute("deviceTypeList",deviceTypeList);
        return "rightContent/equipmentControl/equipmentMonitor";
    }
    @RequestMapping("/stationManagement")
    public String stationManagement(Model model,Integer id){
        List<Line> lineList=lineService.selectAllLine();
        List<Station> stationList=stationService.queryAllStation();
        List<DeviceType> deviceTypeList=deviceTypeService.selectAllDeviceType();
        Station station=stationService.selectStation(id);
        Line line=lineService.selectLine(station.getLineID());

        model.addAttribute("id",id);
        model.addAttribute("line",line);
        model.addAttribute("lineList",lineList);
        model.addAttribute("stationList",stationList);
        model.addAttribute("deviceTypeList",deviceTypeList);
        return "rightContent/equipmentControl/equipmentMonitor";
    }


    @RequestMapping("/selectMonitor")
    @ResponseBody
    public Map<String,Object> selectMonitor(ModelAndView mv,Integer lineID,Integer stationID,Integer type,Integer id,
                                            @RequestParam(defaultValue = "1") Integer pageNum){
        try {
            PageUtil pageUtil=deviceService.selectDevicePaging(lineID,stationID,type,id,pageNum,10);
            List<DeviceSpot> deviceSpotList=null;
            List<Map<String,String>> list=new ArrayList<>();
            for(int i=0;i<pageUtil.getPageData().size();i++){
                DeviceVo device= (DeviceVo) pageUtil.getPageData().get(i);
                deviceSpotList=deviceSpotService.selectDeviceSpot(device.getType());
                Map<String,String> map=new HashMap<>();
                for(int j=0;j<deviceSpotList.size();j++){
                    map.put("state","正在加载....");
                    map.put("deviceID",device.getId().toString());
                    map.put("para"+j,"正在加载....");
                }
                list.add(map);
            }
            mv.addObject("list",list);
            mv.addObject("deviceSpotList",deviceSpotList);
            mv.addObject("pageUtil",pageUtil);
        }catch (Exception e){
            e.printStackTrace();
        }
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
    public Map<String,Object> addDevice(ModelAndView mv,HttpSession session, Device device){
        Users users=(Users) session.getAttribute("user");
        try {
            Integer count=deviceService.selectDeviceByName(device.getLineID(),device.getStationID(),
                    device.getDeviceID(),device.getName(),null);
            if(count>0){
                mv.addObject("result","exist");
            }else{
                Integer i=deviceService.addDevice(device);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户("+users.getName()+") 添加设备("+device.getName()+")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    @RequestMapping("/updateDevice")
    @ResponseBody
    public Map<String,Object> updateDevice(ModelAndView mv,HttpSession session,Device device){
        Users users=(Users) session.getAttribute("user");
        try {
            Integer count=deviceService.selectDeviceByName(device.getLineID(),device.getStationID(),
                    device.getDeviceID(),device.getName(),device.getId());
            if(count>0){
                mv.addObject("result","exist");
            }else{
                Integer i=deviceService.updateDevice(device);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户("+users.getName()+") 修改设备("+device.getName()+")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    @RequestMapping("/deleteDevice")
    @ResponseBody
    public Map<String,Object> deleteDevice(ModelAndView mv,HttpSession session,Integer id){
        Users users=(Users) session.getAttribute("user");
        try {
            Integer i=deviceService.deleteDevice(id);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                Device device=deviceService.selectDevice(id);
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("系统配置管理");
                operationLog.setContent("用户("+users.getName()+") 删除设备("+device.getName()+")!");
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

    //去重
    private static ArrayList<VisitCount> removeDuplicateUser(List<VisitCount> users) {

        Set<VisitCount> set = new TreeSet<VisitCount>(new Comparator<VisitCount>() {
            public int compare(VisitCount o1, VisitCount o2) {
                return o1.getDeviceID().compareTo(o2.getDeviceID());
            }
        });
        set.addAll(users);
        return new ArrayList<VisitCount>(set);
    }
}
