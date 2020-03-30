package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.DeviceVo;
import com.example.passenger.service.*;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/Device")
@Controller
@EnableAutoConfiguration
public class DeviceController {
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    DeviceService deviceService;
    @Autowired
    DevicePosService devicePosService;
    @Autowired
    DeviceSpotService deviceSpotService;
    @Autowired
    DeviceTypeService deviceTypeService;
    @Autowired
    LineService lineService;
    @Autowired
    StationService stationService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/selectDevice")
    @ResponseBody
    public Map<String, Object> selectDevice(ModelAndView mv, Integer id) {
        Device device = deviceService.selectDevice(id);
        mv.addObject("device", device);
        return mv.getModel();
    }

    @RequestMapping("/getDevice")
    @ResponseBody
    public Map<String, Object> getDevice(ModelAndView mv, Integer lineID, Integer stationID,
                                         Integer type) {
        List<Device> deviceList = deviceService.selectDeviceById(lineID, stationID, type);
        mv.addObject("deviceList", deviceList);
        return mv.getModel();
    }

    @RequestMapping("/selectAllDevice")
    @ResponseBody
    public Map<String, Object> selectAllDevice(ModelAndView mv, Integer stationID) {
        List<Device> deviceList = deviceService.selectAllDevice(stationID);
        mv.addObject("deviceList", deviceList);
        return mv.getModel();
    }

    @RequestMapping("/getType")
    @ResponseBody
    public Map<String, Object> getType(ModelAndView mv, Integer lineID) {
        List<Line> lineList = lineService.selectAllLine();
        List<Station> stationList = stationService.selectAllStation(lineID);
        List<Station> stations = stationService.selectAllTrain(lineID);
        List<DevicePos> devicePosList = devicePosService.selectAllDevicePos();
        List<DeviceType> deviceTypeList = deviceTypeService.selectAllDeviceType();
        mv.addObject("lineList", lineList);
        mv.addObject("stations", stations);
        mv.addObject("stationList", stationList);
        mv.addObject("devicePosList", devicePosList);
        mv.addObject("deviceTypeList", deviceTypeList);
        return mv.getModel();
    }

    @RequestMapping("/addVideoList")
    @ResponseBody
    public Map<String, Object> addVideoList(ModelAndView mv, HttpSession session, String id) {
//        List<String> list=(ArrayList < String >)session.getAttribute("deviceList");
//
//        if(id!=null){
//            list.add(id.toString());
//        }
//        //去重list
//        Set set = new  HashSet();
//        List<String> newList = new  ArrayList();
//        for (String cd:list) {
//            if(set.add(cd)){
//                newList.add(cd);
//            }
//        }
//        session.setAttribute("deviceList",newList);


        //使用HashMap实现
        //设备id与前端div的映射集合 divIndex --> deviceId
        Map<Integer, Integer> divIndexMapdeviceId = (Map<Integer, Integer>) session.getAttribute("divIndexMapdeviceId");
        if (divIndexMapdeviceId == null || divIndexMapdeviceId.size() < 9) {
            divIndexMapdeviceId = new HashMap<Integer, Integer>();
            for (int i = 0; i < 9; i++) {
                divIndexMapdeviceId.put(i, -1);
            }
        }

        Boolean isExist = false;
        for (Map.Entry<Integer, Integer> entry : divIndexMapdeviceId.entrySet()) {
            if (entry.getValue() == Integer.parseInt(id)) {
                isExist = true; //当前选择的设备已在选择列表
                break;
            }
        }
        if (!isExist) { //当前选择的设备不在选择列表,设置当前的设备id与前端未存放设备信息的div映射
            for (Map.Entry<Integer, Integer> entry : divIndexMapdeviceId.entrySet()) {
                if (entry.getValue() == -1) {
                    entry.setValue(Integer.parseInt(id));
                    break;
                }
            }
        }
        session.setAttribute("divIndexMapdeviceId", divIndexMapdeviceId);

        return mv.getModel();
    }

    @RequestMapping("/getVideo")
    @ResponseBody
    public Map<String, Object> getVideo(ModelAndView mv, HttpSession session) {
//        List<String> list=(ArrayList < String >)session.getAttribute("deviceList");
//        List<DeviceVo> deviceList=new ArrayList<>();
//        for (String s:list){
//            //获取设备信息
//            Device device=deviceService.selectDevice(Integer.parseInt(s));
//            DeviceVo deviceVo=deviceVo(device);
//            //获取车站名称
//            Station station=stationService.selectStation(device.getStationID());
//            deviceVo.setStationName(station.getName());
//            //获取线路名称
//            Line line=lineService.selectLine(device.getLineID());
//            deviceVo.setLineName(line.getName());
//            deviceList.add(deviceVo);
//        }
//        List<Line> line=lineService.selectAllLine();
//        mv.addObject("deviceList",deviceList);
//        mv.addObject("line",line);


        //使用HashMap实现
        //设备id与前端div的映射集合 divIndex --> deviceId
        Map<Integer, Integer> divIndexMapdeviceId = (Map<Integer, Integer>) session.getAttribute("divIndexMapdeviceId");

        if (divIndexMapdeviceId != null) {
            List<DeviceVo> deviceList = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : divIndexMapdeviceId.entrySet()) {
                if (entry.getValue() != -1) {
                    //获取设备信息
                    Device device = deviceService.selectDevice(entry.getValue());
                    DeviceVo deviceVo = deviceVo(device);
                    //获取车站名称
                    Station station = stationService.selectStation(device.getStationID());
                    deviceVo.setStationName(station.getName());
                    //获取线路名称
                    Line line = lineService.selectLine(device.getLineID());
                    deviceVo.setLineName(line.getName());
                    deviceList.add(deviceVo);
                }
            }
            List<Line> line = lineService.selectAllLine();
            mv.addObject("deviceList", deviceList);
            mv.addObject("line", line);
        }
        mv.addObject("divIndexMapdeviceId", divIndexMapdeviceId);
        return mv.getModel();
    }


    @RequestMapping("/deviceMonitor")
    public String deviceMonitor(Model model, Integer id) {
        Device device = deviceService.selectDevice(id);
        Station station = stationService.selectStation(device.getStationID());
        Line line = lineService.selectLine(station.getLineID());

        List<Line> lineList = lineService.selectAllLine();
        List<DeviceType> deviceTypeList = deviceTypeService.selectAllDeviceType();
        List<Station> stationList = stationService.queryAllStation();

        model.addAttribute("id", device.getStationID());
        model.addAttribute("line", line);
        model.addAttribute("lineList", lineList);
        model.addAttribute("stationList", stationList);
        model.addAttribute("deviceTypeList", deviceTypeList);
        return "rightContent/monitor/devMonitor";
    }

    @RequestMapping("/lineMonitor")
    public String lineMonitor(Model model, Integer id) {
        List<Line> lineList = lineService.selectAllLine();
        List<Station> stationList = stationService.queryAllStation();
        List<DeviceType> deviceTypeList = deviceTypeService.selectAllDeviceType();

        model.addAttribute("id", id);
        model.addAttribute("line", null);
        model.addAttribute("lineList", lineList);
        model.addAttribute("stationList", stationList);
        model.addAttribute("deviceTypeList", deviceTypeList);
        return "rightContent/monitor/devMonitor";
    }

    @RequestMapping("/stationMonitor")
    public String stationMonitor(Model model, Integer id) {
        List<Line> lineList = lineService.selectAllLine();
        List<Station> stationList = stationService.queryAllStation();
        List<DeviceType> deviceTypeList = deviceTypeService.selectAllDeviceType();
        Station station = stationService.selectStation(id);
        Line line = lineService.selectLine(station.getLineID());

        model.addAttribute("id", id);
        model.addAttribute("line", line);
        model.addAttribute("lineList", lineList);
        model.addAttribute("stationList", stationList);
        model.addAttribute("deviceTypeList", deviceTypeList);
        return "rightContent/monitor/devMonitor";
    }


    @RequestMapping("/selectMonitor")
    @ResponseBody
    public Map<String, Object> selectMonitor(ModelAndView mv, Integer lineID, Integer stationID, Integer type, Integer id,
                                             @RequestParam(defaultValue = "1") Integer pageNum) {
        try {
            PageUtil pageUtil = deviceService.selectDevicePaging(lineID, stationID, type, id, pageNum, 10);
            List<DeviceSpot> deviceSpotList = null;
            List<Map<String, String>> list = new ArrayList<>();
            for (int i = 0; i < pageUtil.getPageData().size(); i++) {
                DeviceVo device = (DeviceVo) pageUtil.getPageData().get(i);
                deviceSpotList = deviceSpotService.selectDeviceSpot(device.getType());
                Map<String, String> map = new HashMap<>();
                for (int j = 0; j < deviceSpotList.size(); j++) {
                    map.put("state", "正在加载....");
                    map.put("deviceID", device.getId().toString());
                    map.put("para" + j, "正在加载....");
                }
                list.add(map);
            }
            mv.addObject("list", list);
            mv.addObject("deviceSpotList", deviceSpotList);
            mv.addObject("pageUtil", pageUtil);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }


    @RequestMapping("/selectMonitorType")
    @ResponseBody
    public Map<String, Object> selectMonitorType(ModelAndView mv, Integer id) {
        Device device = deviceService.selectDevice(id);
        //snmp参数
        SnmpService snmpService = new SnmpService();
        SnmpModel snmpModel = new SnmpModel();
        snmpModel.setCommunityName("public");
        snmpModel.setHostIp(device.getIp());
        snmpModel.setPort(161);
        snmpModel.setVersion(1);
        Map<String, String> map = new HashMap<>();
        try {
            //是否连接
            if (snmpService.isEthernetConnection(snmpModel) == true) {
                map.put("state", "1");
            } else {
                map.put("state", "0");
            }
            //获取设备测点信息
            List<DeviceSpot> deviceSpotList = deviceSpotService.selectDeviceSpot(device.getType());
            for (int j = 0; j < deviceSpotList.size(); j++) {
                DeviceSpot deviceSpot = deviceSpotList.get(j);
                Integer para = snmpService.work(snmpModel, deviceSpot.getParameter());
                if (null != para && para > 0) {
                    map.put("para" + j, para.toString());
                } else {
                    map.put("para" + j, "0");
                }
            }
            mv.addObject("map", map);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/addDevice")
    @ResponseBody
    public Map<String, Object> addDevice(ModelAndView mv, HttpSession session, Device device) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = deviceService.selectDeviceByName(device.getLineID(), device.getStationID(),
                    device.getDeviceID(), device.getName(), null);
            if (count > 0) {
                mv.addObject("result", "exist");
            } else {
                Integer i = deviceService.addDevice(device);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + users.getName() + ") 添加设备(" + device.getName() + ")!");
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


    @RequestMapping("/updateDevice")
    @ResponseBody
    public Map<String, Object> updateDevice(ModelAndView mv, HttpSession session, Device device) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = deviceService.selectDeviceByName(device.getLineID(), device.getStationID(),
                    device.getDeviceID(), device.getName(), device.getId());
            if (count > 0) {
                mv.addObject("result", "exist");
            } else {
                Integer i = deviceService.updateDevice(device);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + users.getName() + ") 修改设备(" + device.getName() + ")!");
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


    @RequestMapping("/deleteDevice")
    @ResponseBody
    public Map<String, Object> deleteDevice(ModelAndView mv, HttpSession session, Integer id) {
        Users users = (Users) session.getAttribute("user");
        Device device = deviceService.selectDevice(id);
        try {
            Integer i = deviceService.deleteDevice(id);
            if (i > 0) {
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("系统配置管理");
                operationLog.setContent("用户(" + users.getName() + ") 删除设备(" + device.getName() + ")!");
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

    //转型
    private DeviceVo deviceVo(Device device) {
        DeviceVo deviceVo = new DeviceVo();
        deviceVo.setId(device.getId());
        deviceVo.setName(device.getName());
        deviceVo.setStationID(device.getStationID());
        deviceVo.setLineID(device.getLineID());
        deviceVo.setDeviceID(device.getDeviceID());
        return deviceVo;
    }
}
