package com.example.passenger.controller;

import com.example.passenger.entity.OperationLog;
import com.example.passenger.entity.Station;
import com.example.passenger.entity.Users;
import com.example.passenger.service.DeviceService;
import com.example.passenger.service.OperationLogService;
import com.example.passenger.service.StationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Station")
@EnableAutoConfiguration
public class StationController {
    private static final Logger logger = LoggerFactory.getLogger(StationController.class);

    @Autowired
    StationService stationService;
    @Autowired
    OperationLogService operationLogService;
    @Autowired
    DeviceService deviceService;

    @RequestMapping("/trainInfoManage")
    public String trainInfoManage() {
        return "rightContent/system/trainInfoManage";
    }

    @RequestMapping("selectStation")
    @ResponseBody
    public Map<String, Object> selectStation(ModelAndView mv, Integer id) {
        Station station = stationService.selectStation(id);
        mv.addObject("station", station);
        return mv.getModel();
    }

    @RequestMapping("/selectAllStation")
    @ResponseBody
    public Map<String, Object> selectAllStation(ModelAndView mv, Integer lineID) {
        try {
            List<Station> stationList = stationService.selectAllStation(lineID);
            mv.addObject("stationList", stationList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/selectAllTrain")
    @ResponseBody
    public Map<String, Object> selectAllTrain(ModelAndView mv, Integer lineID) {
        try {
            List<Station> stationList = stationService.selectAllTrain(lineID);
            mv.addObject("stationList", stationList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("addStation")
    @ResponseBody
    public Map<String, Object> addStation(ModelAndView mv, HttpSession session, Station station) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = stationService.selectStationByName(station.getLineID(),
                    station.getStationID(), station.getName(), null);
            if (count > 0) {
                mv.addObject("result", "exist");
            } else {
                Integer i = stationService.addStation(station);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("系统配置管理");
                    if (station.getIsTrain() == 1) {
                        operationLog.setContent("用户(" + users.getName() + ") 添加列车(" + station.getName() + ")!");
                    } else {
                        operationLog.setContent("用户(" + users.getName() + ") 添加车站(" + station.getName() + ")!");
                    }
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

    @RequestMapping("/updateStation")
    @ResponseBody
    public Map<String, Object> updateStation(ModelAndView mv, HttpSession session, Station station) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = stationService.selectStationByName(station.getLineID(),
                    station.getStationID(), station.getName(), station.getId());
            if (count > 0) {
                mv.addObject("result", "exist");
            } else {
                Integer i = stationService.updateStation(station);
                if (i > 0) {
                    Station station1 = stationService.selectStation(station.getId());
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("系统配置管理");
                    if (station1.getIsTrain() == 1) {
                        operationLog.setContent("用户(" + users.getName() + ") 修改列车(" + station.getName() + ")!");
                    } else {
                        operationLog.setContent("用户(" + users.getName() + ") 修改车站(" + station.getName() + ")!");
                    }
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

    @RequestMapping("/deleteStation")
    @ResponseBody
    public Map<String, Object> deleteStation(ModelAndView mv, HttpSession session, Integer id) {
        Users users = (Users) session.getAttribute("user");
        Station station = stationService.selectStation(id);
        try {
            Integer i = stationService.deleteStation(id);
            if (i > 0) {
                deviceService.deleteDeviceByStationId(id);
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("系统配置管理");
                if (station.getIsTrain() == 1) {
                    operationLog.setContent("用户(" + users.getName() + ") 删除列车(" + station.getName() + ")!");
                } else {
                    operationLog.setContent("用户(" + users.getName() + ") 删除车站(" + station.getName() + ")!");
                }
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
}
