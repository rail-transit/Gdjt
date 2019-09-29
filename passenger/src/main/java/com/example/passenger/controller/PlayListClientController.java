package com.example.passenger.controller;

import com.example.passenger.entity.Device;
import com.example.passenger.entity.PlayList;
import com.example.passenger.entity.PlayListClient;
import com.example.passenger.entity.Users;
import com.example.passenger.service.DeviceService;
import com.example.passenger.service.PlayListClientService;
import com.example.passenger.service.PlayListService;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MsgSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RequestMapping("/playListClient")
@Controller
public class PlayListClientController {
    @Autowired
    PlayListClientService playListClientService;

    @Autowired
    PlayListService playListService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    MsgSend msgSend;

    @RequestMapping("/addClient")
    @ResponseBody
    public Map<String,Object> addClient(HttpSession session, ModelAndView mv, PlayListClient playListClient){
        Integer count=playListClientService.selectClientByPlayListID(playListClient.getPlaylistID(),
                playListClient.getClientID());
        Users users=(Users)session.getAttribute("user");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(count>0){
            Integer id=playListClientService.selectClientByPlayID(
                    playListClient.getClientID(),playListClient.getPlaylistID());
            PlayListClient playListClient1=new PlayListClient();
            playListClient1.setSequence(0);
            playListClient1.setEditor(users.getName());
            playListClient1.setAuditTime(sdf.format(new Date()));
            playListClient1.setId(id);
            playListClientService.updatePlayListClient(playListClient1);
            mv.addObject("result","成功");
        }else{
            playListClient.setAuditTime(sdf.format(new Date()));
            playListClient.setSequence(0);
            playListClient.setEditor(users.getName());
            Integer i=playListClientService.addPlayListClient(playListClient);
            if(i>0){
                mv.addObject("result","成功");
            }else{
                mv.addObject("result","失败");
            }
        }
        return mv.getModel();
    }

    @RequestMapping("/cancelPlayList")
    @ResponseBody
    public Map<String,Object> cancelPlayList(ModelAndView mv,Integer playlistID,Integer deviceID){
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
            mv.addObject("result","成功");
        }catch (Exception e){
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/implPlayList")
    @ResponseBody
    public Map<String,Object> implPlayList(ModelAndView mv,Integer playlistID,Integer deviceID){
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
            mv.addObject("result","成功");
        }catch (Exception e){
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

   /* @RequestMapping("/updateClient")
    @ResponseBody
    public Map<String,Object> updateClient(ModelAndView mv, PlayListClient playListClient){
        Integer i=playListClientService.updatePlayListClient(playListClient);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteClient")
    @ResponseBody
    public Map<String,Object> deleteClient(ModelAndView mv,Integer id){
        Integer i=playListClientService.deletePlayListClient(id);
        if(i>0){
            mv.addObject("result","成功");
        }else{
            mv.addObject("result","失败");
        }
        return mv.getModel();
    }*/
}
