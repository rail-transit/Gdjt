package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.service.*;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MsgSend;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/playListClient")
@Controller
public class PlayListClientController {
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
    public String videoReleaseStatistics(Model model){
        List<Device> deviceList=deviceService.queryAllDevice();
        List<Station> stationList=stationService.queryAllStation();
        List<Line> lineList=lineService.selectAllLine();
        model.addAttribute("deviceList",deviceList);
        model.addAttribute("stationList",stationList);
        model.addAttribute("lineList",lineList);
        return "rightContent/reportForm/videoRelease";
    }

    @RequestMapping("/getDownloadSpeed")
    @ResponseBody
    public Map<String,Object> getDownloadSpeed(ModelAndView mv, @RequestParam(defaultValue = "1")
            Integer pageNum, Integer playListID){
        try {
            PageUtil pageUtil=playListClientService.getDownloadSpeed(playListID,pageNum,10);
            List<PlayListDownloadStatus> playListDownloadStatusList=playListDownloadStatusService.
                    selectDownloadStatus(playListID,null);
            mv.addObject("playListDownloadStatusList",playListDownloadStatusList);
            mv.addObject("pageUtil",pageUtil);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/selectStatistics")
    @ResponseBody
    public Map<String,Object> selectStatistics(@RequestParam(defaultValue = "1") Integer pageNum,
                                               ModelAndView mv,Integer lineID,Integer stationID,
                                               Integer deviceID,String startDate,String endDate){
        try {
            PageUtil pageUtil=playListClientService.releasePaging(lineID,stationID,deviceID,
                    startDate,endDate,pageNum,10);

            mv.addObject("pageUtil",pageUtil);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/getVideoRelease")
    @ResponseBody
    public Map<String,Object> getVideoRelease(ModelAndView mv,Integer lineID,Integer stationID,
                                              Integer deviceID,String startDate,String endDate){
        try {
            List<Map<String,String>> playListClientVoList=playListClientService.getVideoRelease(lineID,
                    stationID,deviceID,startDate,endDate);
            mv.addObject("playListClientVoList",playListClientVoList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv.getModel();
    }

    @RequestMapping("/addClient")
    @ResponseBody
    public Map<String,Object> addClient(HttpSession session, ModelAndView mv, PlayListClient playListClient,
                                        @RequestParam(defaultValue = "0") Integer type){
        Integer count=playListClientService.selectClientByPlayListID(playListClient.getPlaylistID(),
                playListClient.getClientID());
        Users users=(Users)session.getAttribute("user");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PlayList playList=playListService.selectPlayList(playListClient.getPlaylistID());
        if(type==1){
            if(playList!=null){
                //日志记录
                OperationLog operationLog=new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("版式管理");
                operationLog.setContent("用户("+users.getName()+") 取消播表发布("+playList.getName()+")!");
                operationLogService.addOperationLog(operationLog);
                playListService.updatePlayListByID(playListClient.getPlaylistID(),null,playList.getStartDate());
            }
        }
        if(count>0){
            Integer id=playListClientService.selectClientByPlayID(
                    playListClient.getClientID(),playListClient.getPlaylistID());
            PlayListClient playListClient1=new PlayListClient();
            playListClient1.setSequence(0);
            playListClient1.setEditor(users.getName());
            playListClient1.setAuditTime(sdf.format(new Date()));
            playListClient1.setId(id);
            Integer i=playListClientService.updatePlayListClient(playListClient1);
            if(i>0){
                List<PlayListDownloadStatus> statusList=playListDownloadStatusService.selectDownloadStatus(
                        playListClient.getPlaylistID(),playListClient.getClientID());
                for (PlayListDownloadStatus status:statusList){
                    playListDownloadStatusService.deleteDownload(status.getId());
                }
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }else{
            playListClient.setAuditTime(sdf.format(new Date()));
            playListClient.setSequence(0);
            playListClient.setEditor(users.getName());
            Integer i=playListClientService.addPlayListClient(playListClient);
            if(i>0){
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }
        //日志记录
        OperationLog operationLog=new OperationLog();
        operationLog.setOperator(users.getId().toString());
        operationLog.setType("版式管理");
        operationLog.setContent("用户("+users.getName()+") 发布播表或者预案("+playList.getName()+")!");
        operationLogService.addOperationLog(operationLog);
        return mv.getModel();
    }

    @RequestMapping("/cancelPlayList")
    @ResponseBody
    public Map<String,Object> cancelPlayList(ModelAndView mv,HttpSession session,Integer playlistID,
                                             Integer deviceID){
        try {
            PlayList playList=playListService.selectPlayList(playlistID);
            Device device=deviceService.selectDevice(deviceID);
            String content="PLAN:<MSG>\n" +
                    "<Type>13</Type>\n" +
                    "<Info>\n" +
                    "<Level>"+playList.getLevel()+"</Level>\n" +
                    "<State>0</State>\n" +
                    "<ID>"+playlistID+"</ID>\n" +
                    "</Info>\n" +
                    "</MSG>";
            long ip= IPUtil.ipToLong(device.getIp());
            msgSend.sendMsg("pisplayer.*."+ip,content);

            //日志记录
            OperationLog operationLog=new OperationLog();
            Users users=(Users) session.getAttribute("user");
            operationLog.setOperator(users.getId().toString());
            operationLog.setType("版式管理");
            operationLog.setContent("用户("+users.getName()+") 预案取消("+playList.getName()+")!");
            operationLogService.addOperationLog(operationLog);
            mv.addObject("result","success");
        }catch (Exception e){
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/implPlayList")
    @ResponseBody
    public Map<String,Object> implPlayList(ModelAndView mv,HttpSession session,Integer playlistID,
                                           Integer deviceID){
        try {
            PlayList playList=playListService.selectPlayList(playlistID);
            Device device=deviceService.selectDevice(deviceID);
            String content="PLAN:<MSG>\n" +
                    "<Type>13</Type>\n" +
                    "<Info>\n" +
                    "<Level>"+playList.getLevel()+"</Level>\n" +
                    "<State>1</State>\n" +
                    "<ID>"+playlistID+"</ID>\n" +
                    "</Info>\n" +
                    "</MSG>";
            long ip= IPUtil.ipToLong(device.getIp());
            msgSend.sendMsg("pisplayer.*."+ip,content);

            //日志记录
            OperationLog operationLog=new OperationLog();
            Users users=(Users) session.getAttribute("user");
            operationLog.setOperator(users.getId().toString());
            operationLog.setType("版式管理");
            operationLog.setContent("用户("+users.getName()+") 预案执行("+playList.getName()+")!");
            operationLogService.addOperationLog(operationLog);
            mv.addObject("result","success");
        }catch (Exception e){
            mv.addObject("result","error");
        }
        return mv.getModel();
    }
}
