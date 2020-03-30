package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.LineVo;
import com.example.passenger.entity.vo.StationVo;
import com.example.passenger.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/Line")
@Controller
@EnableAutoConfiguration
public class LineController {
    private static final Logger logger = LoggerFactory.getLogger(LineController.class);

    @Autowired
    LineService lineService;
    @Autowired
    StationService stationService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceTypeService deviceTypeService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/lineInfoManage")
    public String lineInfoManage() {
        return "rightContent/system/lineInfoManage";
    }

    @RequestMapping("selectAllLine")
    @ResponseBody
    public Map<String, Object> selectAllLine(ModelAndView mv) {
        List<Line> lineList = lineService.selectAllLine();
        mv.addObject("lineList", lineList);
        return mv.getModel();
    }

    @RequestMapping("/selectLine")
    @ResponseBody
    public Map<String, Object> selectLine(ModelAndView mv, Integer id) {
        Line line = lineService.selectLine(id);
        mv.addObject("line", line);
        return mv.getModel();
    }

    @RequestMapping("/getMenu")
    @ResponseBody
    public Map<String, Object> getMenu(ModelAndView mv, @RequestParam(defaultValue = "0") Integer type) {
        List<Line> lineList = lineService.selectAllLine();
        List<LineVo> lineVoList = new ArrayList<>();
        for (Line line : lineList) {
            LineVo vo = lineToVO(line);
            List<Station> stationList = stationService.selectAllStation(line.getId());
            List<StationVo> stationVoList = new ArrayList<>();
            for (Station station : stationList) {
                StationVo stationVo = stationToVO(station);
                if (type == 0) {
                    List<Device> deviceList = deviceService.selectAllDevice(station.getId());
                    stationVo.setDeviceList(deviceList);
                } else {
                    DeviceType deviceType = deviceTypeService.selectTypeByName();
                    List<Device> deviceList = deviceService.selectDeviceByType(station.getId(), deviceType.getId());
                    stationVo.setDeviceList(deviceList);
                }
                stationVoList.add(stationVo);
            }
            vo.setStationVoList(stationVoList);
            lineVoList.add(vo);
        }
        mv.addObject("lineVoList", lineVoList);
        return mv.getModel();
    }

    @RequestMapping("/getTrain")
    @ResponseBody
    public Map<String, Object> getTrain(ModelAndView mv) {
        List<Line> lineList = lineService.selectAllLine();
        List<LineVo> lineVoList = new ArrayList<>();
        for (Line line : lineList) {
            LineVo vo = lineToVO(line);
            List<Station> stationList = stationService.selectAllTrain(line.getId());
            List<StationVo> stationVoList = new ArrayList<>();
            for (Station station : stationList) {
                StationVo stationVo = stationToVO(station);
                List<Device> deviceList = deviceService.selectAllDevice(station.getId());
                stationVo.setDeviceList(deviceList);
                stationVoList.add(stationVo);
            }
            vo.setStationVoList(stationVoList);
            lineVoList.add(vo);
        }
        mv.addObject("lineVoList", lineVoList);
        return mv.getModel();
    }

    @RequestMapping("/addLine")
    @ResponseBody
    public Map<String, Object> addLine(ModelAndView mv, HttpSession session, Line line) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = lineService.selectLineByName(line.getLineID(), line.getName(), null);
            if (count > 0) {
                mv.addObject("result", "exist");
            } else {
                Integer i = lineService.addLine(line);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + users.getName() + ") 添加线路(" + line.getName() + ")!");
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

    @RequestMapping("/updateLine")
    @ResponseBody
    public Map<String, Object> updateLine(ModelAndView mv, HttpSession session, Line line) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = lineService.selectLineByName(line.getLineID(), line.getName(), line.getId());
            if (count > 0) {
                mv.addObject("result", "exist");
            } else {
                Integer i = lineService.updateLine(line);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + users.getName() + ") 修改线路(" + line.getName() + ")!");
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

    @RequestMapping("/deleteLine")
    @ResponseBody
    public Map<String, Object> deleteLine(ModelAndView mv, HttpSession session, Integer id) {
        Users users = (Users) session.getAttribute("user");
        Line line = lineService.selectLine(id);
        try {
            Integer i = lineService.deleteLine(id);
            if (i > 0) {
                stationService.deleteStationByLineId(id);
                deviceService.deleteDeviceByLineId(id);
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("系统配置管理");
                operationLog.setContent("用户(" + users.getName() + ") 删除线路(" + line.getName() + ")!");
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
    private LineVo lineToVO(Line line) {
        LineVo lineVo = new LineVo();
        lineVo.setId(line.getId());
        lineVo.setLineID(line.getLineID());
        lineVo.setName(line.getName());
        lineVo.setEnName(line.getName());
        lineVo.setDes(line.getDes());
        lineVo.setServerIP(line.getServerIP());
        return lineVo;
    }

    //转型
    private StationVo stationToVO(Station station) {
        StationVo vo = new StationVo();
        vo.setId(station.getId());
        vo.setStationID(station.getStationID());
        vo.setName(station.getName());
        vo.setEnName(station.getName());
        vo.setDes(station.getDes());
        vo.setDownStartTime(station.getDownStartTime());
        vo.setDownStopTime(station.getDownStopTime());
        vo.setIsCenter(station.getIsCenter());
        vo.setIsTrain(station.getIsTrain());
        vo.setServerIP(station.getServerIP());
        vo.setLineID(station.getLineID());
        vo.setUpStartTime(station.getUpStartTime());
        vo.setUpStopTime(station.getUpStopTime());
        return vo;
    }
}
