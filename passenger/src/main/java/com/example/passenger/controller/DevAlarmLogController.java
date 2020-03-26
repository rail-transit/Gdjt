package com.example.passenger.controller;

import com.example.passenger.entity.Device;
import com.example.passenger.entity.DeviceType;
import com.example.passenger.entity.Line;
import com.example.passenger.entity.Station;
import com.example.passenger.service.*;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/devAlarmLog")
public class DevAlarmLogController {
    private static final Logger logger = LoggerFactory.getLogger(DevAlarmLogController.class);

    @Autowired
    DevAlarmLogService devAlarmLogService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    StationService stationService;
    @Autowired
    LineService lineService;
    @Autowired
    DeviceTypeService deviceTypeService;

    @RequestMapping("/warningStatistics")
    public String warningStatistics(Model model) {
        List<Line> lineList = lineService.selectAllLine();
        List<Station> stationList = stationService.queryAllStation();
        List<Device> deviceList = deviceService.queryAllDevice();
        List<DeviceType> deviceTypeList = deviceTypeService.selectAllDeviceType();

        model.addAttribute("lineList", lineList);
        model.addAttribute("stationList", stationList);
        model.addAttribute("deviceList", deviceList);
        model.addAttribute("deviceTypeList", deviceTypeList);
        return "rightContent/reportForm/warning";
    }

    @RequestMapping("/faultStatistics")
    public String faultStatistics(Model model) {
        List<Line> lineList = lineService.selectAllLine();
        List<Station> stationList = stationService.queryAllStation();
        List<Device> deviceList = deviceService.queryAllDevice();
        List<DeviceType> deviceTypeList = deviceTypeService.selectAllDeviceType();

        model.addAttribute("lineList", lineList);
        model.addAttribute("stationList", stationList);
        model.addAttribute("deviceList", deviceList);
        model.addAttribute("deviceTypeList", deviceTypeList);
        return "rightContent/reportForm/fault";
    }

    @RequestMapping("/selectStatistics")
    @ResponseBody
    public Map<String, Object> selectStatistics(@RequestParam(defaultValue = "1") Integer pageNum,
                                                ModelAndView mv, Integer alarmType, Integer lineID,
                                                Integer stationID, Integer deviceID, Integer type,
                                                String startDate, String endDate) {
        try {
            PageUtil pageUtil = devAlarmLogService.warningPaging(alarmType, lineID, stationID, deviceID,
                    type, startDate, endDate, pageNum, 10);
            mv.addObject("pageUtil", pageUtil);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/getWarning")
    @ResponseBody
    public Map<String, Object> getWarning(ModelAndView mv, Integer alarmType, Integer lineID,
                                          Integer stationID, Integer deviceID, Integer type,
                                          String startDate, String endDate) {
        try {
            List<Map<String, String>> devAlarmLogVoList = devAlarmLogService.getWarning(alarmType, lineID,
                    type, stationID, deviceID, startDate, endDate);
            mv.addObject("devAlarmLogVoList", devAlarmLogVoList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }
}
