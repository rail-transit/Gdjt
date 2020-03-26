package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.service.*;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MD5;
import com.example.passenger.utils.MsgSend;
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

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/playListClient")
@Controller
public class PlayListClientController {
    private static final Logger logger = LoggerFactory.getLogger(PlayListClientController.class);

    @Autowired
    PlayListClientService playListClientService;
    @Autowired
    PlayListDownloadStatusService playListDownloadStatusService;
    @Autowired
    PlayListService playListService;
    @Autowired
    LineService lineService;
    @Autowired
    StationService stationService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    OperationLogService operationLogService;
    @Autowired
    MsgSend msgSend;

    @RequestMapping("/videoReleaseStatistics")
    public String videoReleaseStatistics(Model model) {
        List<Device> deviceList = deviceService.queryAllDevice();
        List<Station> stationList = stationService.queryAllStation();
        List<Line> lineList = lineService.selectAllLine();
        model.addAttribute("deviceList", deviceList);
        model.addAttribute("stationList", stationList);
        model.addAttribute("lineList", lineList);
        return "rightContent/reportForm/videoRelease";
    }

    @RequestMapping("/getDownloadSpeed")
    @ResponseBody
    public Map<String, Object> getDownloadSpeed(ModelAndView mv, @RequestParam(defaultValue = "1")
            Integer pageNum, Integer playListID) {
        try {
            PageUtil pageUtil = playListClientService.getDownloadSpeed(playListID, pageNum, 10);
            List<PlayListDownloadStatus> playListDownloadStatusList = playListDownloadStatusService.
                    selectDownloadStatus(playListID, null);
            mv.addObject("playListDownloadStatusList", playListDownloadStatusList);
            mv.addObject("pageUtil", pageUtil);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/selectStatistics")
    @ResponseBody
    public Map<String, Object> selectStatistics(@RequestParam(defaultValue = "1") Integer pageNum,
                                                ModelAndView mv, Integer lineID, Integer stationID,
                                                Integer deviceID, String startDate, String endDate) {
        try {
            PageUtil pageUtil = playListClientService.releasePaging(lineID, stationID, deviceID,
                    startDate, endDate, pageNum, 10);

            mv.addObject("pageUtil", pageUtil);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/getVideoRelease")
    @ResponseBody
    public Map<String, Object> getVideoRelease(ModelAndView mv, Integer lineID, Integer stationID,
                                               Integer deviceID, String startDate, String endDate) {
        try {
            List<Map<String, String>> playListClientVoList = playListClientService.getVideoRelease(lineID,
                    stationID, deviceID, startDate, endDate);
            mv.addObject("playListClientVoList", playListClientVoList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/addClient")
    @ResponseBody
    public Map<String, Object> addClient(HttpSession session, ModelAndView mv, PlayListClient playListClient,
                                         String pwd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Users users = (Users) session.getAttribute("user");
        try {
            if (!users.getPwd().equals(MD5.MD5(pwd))) {
                mv.addObject("result", "passwordError");
                return mv.getModel();
            }
            PlayListClient playListClient1 = playListClientService.selectClientByPlayListID(playListClient.getPlaylistID(),
                    playListClient.getClientID());
            //获取播表,线路,车站,设备信息
            PlayList playList = playListService.selectPlayList(playListClient.getPlaylistID());
            Device device = deviceService.selectDevice(playListClient.getClientID());
            Station station = stationService.selectStation(device.getStationID());
            Line line = lineService.selectLine(device.getLineID());
            //设置初始数据
            playListClient.setAuditTime(sdf.format(new Date()));
            playListClient.setSequence(0);
            playListClient.setEditor(users.getName());
            if (playListClient1 != null) {
                playListClient.setId(playListClient1.getId());
                Integer i = playListClientService.updatePlayListClient(playListClient);
                if (i > 0) {
                    List<PlayListDownloadStatus> statusList = playListDownloadStatusService.selectDownloadStatus(
                            playListClient.getPlaylistID(), playListClient.getClientID());
                    for (PlayListDownloadStatus status : statusList) {
                        playListDownloadStatusService.deleteDownload(status.getId());
                    }
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            } else {
                Integer i = playListClientService.addPlayListClient(playListClient);
                if (i > 0) {
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
            //日志记录
            OperationLog operationLog = new OperationLog();
            operationLog.setOperator(users.getId().toString());
            operationLog.setType("预案管理");
            operationLog.setContent("用户(" + users.getName() + ") 发布预案(" + playList.getName() + ")至(" +
                    line.getName() + "-" + station.getName() + "-" + device.getName() + ")!");
            operationLogService.addOperationLog(operationLog);
        } catch (Exception e) {
            mv.addObject("result", "error");
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/cancelPlayList")
    @ResponseBody
    public Map<String, Object> cancelPlayList(ModelAndView mv, HttpSession session, Integer playlistID,
                                              Integer deviceID, String pwd) {
        PlayList playList = playListService.selectPlayList(playlistID);
        Users users = (Users) session.getAttribute("user");
        try {
            if (!users.getPwd().equals(MD5.MD5(pwd))) {
                mv.addObject("result", "passwordError");
                return mv.getModel();
            }
            //获取播表,线路,车站,设备信息
            Device device = deviceService.selectDevice(deviceID);
            Station station = stationService.selectStation(device.getStationID());
            Line line = lineService.selectLine(device.getLineID());
            //编辑消息体,执行下发操作
            String content = "PLAN:<MSG>\n" +
                    "<Type>13</Type>\n" +
                    "<Info>\n" +
                    "<Level>" + playList.getLevel() + "</Level>\n" +
                    "<State>0</State>\n" +
                    "<ID>" + playlistID + "</ID>\n" +
                    "</Info>\n" +
                    "</MSG>";
            long ip = IPUtil.ipToLong(device.getIp());
            msgSend.sendMsg("pisplayer.*." + ip, content);

            //日志记录
            OperationLog operationLog = new OperationLog();
            operationLog.setOperator(users.getId().toString());
            operationLog.setType("版式管理");
            operationLog.setContent("用户(" + users.getName() + ") 预案取消(" + playList.getName() + ")至(" +
                    line.getName() + "-" + station.getName() + "-" + device.getName() + ")!");
            operationLogService.addOperationLog(operationLog);
            mv.addObject("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            mv.addObject("result", "error");
        }
        return mv.getModel();
    }

    @RequestMapping("/implPlayList")
    @ResponseBody
    public Map<String, Object> implPlayList(ModelAndView mv, HttpSession session, Integer playlistID,
                                            Integer deviceID, String pwd) {
        PlayList playList = playListService.selectPlayList(playlistID);
        Users users = (Users) session.getAttribute("user");
        try {
            if (!users.getPwd().equals(MD5.MD5(pwd))) {
                mv.addObject("result", "passwordError");
                return mv.getModel();
            }
            //获取播表,线路,车站,设备信息
            Device device = deviceService.selectDevice(deviceID);
            Station station = stationService.selectStation(device.getStationID());
            Line line = lineService.selectLine(device.getLineID());
            //编辑消息体,执行下发操作
            String content = "PLAN:<MSG>\n" +
                    "<Type>13</Type>\n" +
                    "<Info>\n" +
                    "<Level>" + playList.getLevel() + "</Level>\n" +
                    "<State>1</State>\n" +
                    "<ID>" + playlistID + "</ID>\n" +
                    "</Info>\n" +
                    "</MSG>";
            long ip = IPUtil.ipToLong(device.getIp());
            msgSend.sendMsg("pisplayer.*." + ip, content);

            //日志记录
            OperationLog operationLog = new OperationLog();
            operationLog.setOperator(users.getId().toString());
            operationLog.setType("版式管理");
            operationLog.setContent("用户(" + users.getName() + ") 预案执行(" + playList.getName() + ")至(" +
                    line.getName() + "-" + station.getName() + "-" + device.getName() + ")!");
            operationLogService.addOperationLog(operationLog);
            mv.addObject("result", "success");
        } catch (Exception e) {
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }
}
