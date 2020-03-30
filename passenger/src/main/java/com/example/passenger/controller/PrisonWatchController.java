package com.example.passenger.controller;

import com.example.passenger.config.AmqpConfig;
import com.example.passenger.entity.Device;
import com.example.passenger.entity.Line;
import com.example.passenger.entity.Station;
import com.example.passenger.entity.vo.DeviceVo;
import com.example.passenger.service.DeviceService;
import com.example.passenger.service.LineService;
import com.example.passenger.service.StationService;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MsgSend;
import com.example.passenger.utils.VisitCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@RequestMapping("/prisonWatch")
@Controller
@EnableAutoConfiguration
public class PrisonWatchController {
    private static final Logger logger = LoggerFactory.getLogger(PrisonWatchController.class);

    @Autowired
    DeviceService deviceService;
    @Autowired
    LineService lineService;
    @Autowired
    StationService stationService;
    @Autowired
    private MsgSend send;

    public static List<VisitCount> visitCountList = new ArrayList<>();

    //设备画面观看人数记录 deviceID ----> 观看人数(用户数)
    public static Map<Integer, Integer> deviceVisitPeopleCount = new HashMap<Integer, Integer>();

    @RequestMapping("/videoWatch")
    public String videoWatch() {
        return "rightContent/monitor/videoWatch";
    }

    /*@RequestMapping("/openPlay")
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
                        if (state==1) {
                            Integer count = visitCounts.get(i).getCount();
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

    @RequestMapping("/deletePlaylist")
    @ResponseBody
    public Map<String,Object> deletePlaylist(ModelAndView mv, HttpSession session, String deviceID){
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
                        *//*if (file.exists()) {
                            File [] files = file.listFiles();
                            for (int j = 0; j < files.length; j++){
                                files[j].delete();
                            }
                        }*//*
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
                        File file = new File("D://ftp/"+deviceID);
                        *//*if (file.exists()) {
                            File [] files = file.listFiles();
                            for (int j = 0;j < files.length; j++){
                                files[j].delete();
                            }
                        }*//*
                    }
                }
            }
        }
        return mv.getModel();
    }*/

   /* @Scheduled(cron = "0/30 * * * * ?")
    public void timingDeleteImage(){
        if(visitCountList.size()!=0){
            for (VisitCount visitCount:visitCountList){
                File file = new File("D://ftp/"+visitCount.getDeviceID());
                if(file.exists()) {
                    File [] files = file.listFiles();
                    if(files.length!=0){
                        if(files.length>50){
                            Integer count=files.length-5;
                            for (int i = 0; i < count; i++){
                                files[i].delete();
                            }
                        }
                    }
                }
            }
        }
    }*/

    /**
     * @param deviceID        当前所需要显示设备画面的设备id
     * @param devicePlayState 前端对设备的播放状态 1表示播放，0表示停止
     * @param pollingState    轮询状态 1表示轮询，0表示不轮询
     * @author suxijian
     * 设备播放/停止处理函数
     */
    @RequestMapping("/openPlayPolling")
    @ResponseBody
    public Map<String, Object> openPlayPolling(ModelAndView mv, HttpSession session, String deviceID, Integer devicePlayState, String pollingState) {
        //当前正在播放的设备列表
        List<VisitCount> playingDeviceList = (ArrayList<VisitCount>) session.getAttribute("playingDeviceList");
        //设备画面观看人数记录
        Map<Integer, Integer> playingDeviceVisitPeopleCount = deviceVisitPeopleCount;

        //线路下所有设备列表
        List<VisitCount> lineDeviceList = (ArrayList<VisitCount>) session.getAttribute("lineDeviceList");
        try {
            // 点击播放按钮
            if (devicePlayState == 1) {
                //设置当前播放设备列表
                //当前正在播放的设备列表未存在
                if (playingDeviceList == null) {
                    playingDeviceList = new ArrayList<>();
                }

                //被点击播放按钮的设备是否在正在播放设备列表
                Boolean isPlaying = false;
                if (playingDeviceList.size() > 0) {
                    for (VisitCount playDeviceTemp : playingDeviceList) {
                        if (playDeviceTemp.getDeviceID() == Integer.parseInt(deviceID)) {
                            isPlaying = true;
                            break;
                        }
                    }
                }
                //当设备不在设备播放列表，设备播放列表添加该设备
                if (!isPlaying) {
                    VisitCount playDevice = new VisitCount();
//           playDevice.setCount(1);
                    playDevice.setAlarmNumber(0);
                    playDevice.setRepeatCount(0);
                    playDevice.setFileName("");//图片
                    playDevice.setDeviceID(Integer.parseInt(deviceID));

                    //设备观看人数中未包含该设备(设备处于初始状态：无人观看，该设备未截图上传)
                    if (!playingDeviceVisitPeopleCount.containsKey(deviceID)) {
                        //发送消息，使对应设备截图上传
                        sendMessageStartCut(deviceID);
                      /* String smg="CAPT:<MSG>" +
                               "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
                               "<Type>5</Type>" +
                               "<Info><ID>"+deviceID+"</ID>" +
                               "<State>1</State>" +
                               "<Fps>50</Fps>" +
                               "<FtpUserName>ftp</FtpUserName>" +
                               "<FtpUserPwd>123456</FtpUserPwd>" +
                               "<FtpPath>ftp://"+ IPUtil.ip +"</FtpPath>" +
                               "</Info></MSG>";
                       send.sendMsg(deviceID,smg);*/
                        //test
//                       System.out.println("开始截图");
                        //更新该设备观看人数
                        playingDeviceVisitPeopleCount.put(Integer.parseInt(deviceID), 1);
                    } else { //设备观看人数中包含该设备
                        //更新该设备观看人数
                        playingDeviceVisitPeopleCount.put(Integer.parseInt(deviceID), playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceID)) + 1);
                    }

                    playingDeviceList.add(playDevice);
                    session.setAttribute("playingDeviceList", playingDeviceList);
                }

                //将正在播放设备列表转为前端使用到的设备信息列表
                List<DeviceVo> playingDeviceVoList = new ArrayList<>();
                for (VisitCount playDeviceTemp2 : playingDeviceList) {
                    //获取设备信息
                    Device device = deviceService.selectDevice(playDeviceTemp2.getDeviceID());
                    DeviceVo deviceVo = deviceToDeviceVo(device);
                    //获取车站名称
                    Station station = stationService.selectStation(device.getStationID());
                    deviceVo.setStationName(station.getName());
                    //获取线路名称
                    Line line = lineService.selectLine(device.getLineID());
                    deviceVo.setLineName(line.getName());
                    playingDeviceVoList.add(deviceVo);
                }
                mv.addObject("playingDeviceVoList", playingDeviceVoList);

                //若该设备正在播放则提示该设备正在播放，不允许同时播放多个同个设备
                mv.addObject("isPlaying", isPlaying);
            } else { //点击停止播放按钮
                //从正在播放的设备列表移除该设备
                Iterator<VisitCount> playDeviceIt = playingDeviceList.iterator();
                while (playDeviceIt.hasNext()) {
                    VisitCount playDeviceIttemp = playDeviceIt.next();
                    if (playDeviceIttemp.getDeviceID() == Integer.parseInt(deviceID)) {
                        playDeviceIt.remove();
                        break;
                    }
                }
                session.setAttribute("playingDeviceList", playingDeviceList);
                //设置设备观看人数
                if (playingDeviceVisitPeopleCount.containsKey(Integer.parseInt(deviceID)) && playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceID)) > 0) {
                    playingDeviceVisitPeopleCount.put(Integer.parseInt(deviceID), playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceID)) - 1);
                }
                //若观看人数为0删除图片
                updatePlayingDeviceVisitPeopleCount(playingDeviceVisitPeopleCount);
//                   Iterator<Map.Entry<Integer,Integer>> playingDeviceVisitPeopleCountIterator = playingDeviceVisitPeopleCount.entrySet().iterator();
//                   while(playingDeviceVisitPeopleCountIterator.hasNext()){
//                       Map.Entry<Integer,Integer> playingDeviceVisitPeopleCountEntry = playingDeviceVisitPeopleCountIterator.next();
//                       //如果设备观看人数为0，则停止截图，并删除图片
////                       if (playingDeviceVisitPeopleCountEntry.getKey() == Integer.parseInt(deviceID)){
//                           if (playingDeviceVisitPeopleCountEntry.getValue() == 0){
//                               //发送消息，停止截图上传
//                               sendMessageStopCut(playingDeviceVisitPeopleCountEntry.getKey().toString());
//                               /*String smg="CAPT:<MSG>" +
//                                       "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
//                                       "<Type>5</Type>" +
//                                       "<Info><ID>"+playingDeviceVisitPeopleCountEntry.getKey()+"</ID>" +
//                                       "<State>0</State>" +
//                                       "<Fps>50</Fps>" +
//                                       "<FtpUserName>ftp</FtpUserName>" +
//                                       "<FtpUserPwd>123456</FtpUserPwd>" +
//                                       "<FtpPath>ftp://"+IPUtil.ip+"</FtpPath>" +
//                                       "</Info></MSG>";
//                               send.sendMsg(playingDeviceVisitPeopleCountEntry.getKey().toString(),smg);*/
//                               //test
////                               System.out.println("停止截图");
//                               //删除图片
//                               File file = new File("D://ftp/"+playingDeviceVisitPeopleCountEntry.getKey());
//
//                               if (file.exists()) {
//                                   File [] files = file.listFiles();
//                                   if (files.length != 0) {
//                                       for (int j = 0; j < files.length; j++) {
//                                           files[j].delete();
//                                       }
//                                   }
//                               }
//                               //从观看列表移除设备观看人数数据
//                               playingDeviceVisitPeopleCountIterator.remove();
////                               break;
//                           }
////                       }
//                   }


                //轮询状态下点击停止播放按钮，并且正在播放设备数为0
//               if ("1".equals(pollingState) && (playingDeviceList.size() == 0)){
//                   //若观看人数为0，停止截图上传并删除图片
//                   Iterator<Map.Entry<Integer,Integer>> playingDeviceVisitPeopleCountIterator = playingDeviceVisitPeopleCount.entrySet().iterator();
//                   while(playingDeviceVisitPeopleCountIterator.hasNext()){
//                       Map.Entry<Integer,Integer> playingDeviceVisitPeopleCountEntry = playingDeviceVisitPeopleCountIterator.next();
//                       //如果设备观看人数为0，则停止截图，并删除图片
//                       if (playingDeviceVisitPeopleCountEntry.getValue() == 0){
//                           //发送消息，停止截图上传
//                           sendMessageStopCut(playingDeviceVisitPeopleCountEntry.getKey().toString());
//                               /*String smg="CAPT:<MSG>" +
//                                       "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
//                                       "<Type>5</Type>" +
//                                       "<Info><ID>"+playingDeviceVisitPeopleCountEntry.getKey()+"</ID>" +
//                                       "<State>0</State>" +
//                                       "<Fps>50</Fps>" +
//                                       "<FtpUserName>ftp</FtpUserName>" +
//                                       "<FtpUserPwd>123456</FtpUserPwd>" +
//                                       "<FtpPath>ftp://"+IPUtil.ip+"</FtpPath>" +
//                                       "</Info></MSG>";
//                               send.sendMsg(playingDeviceVisitPeopleCountEntry.getKey().toString(),smg);*/
//                           //test
////                               System.out.println("停止截图");
//                           //删除图片
//                           File file = new File("D://ftp/"+playingDeviceVisitPeopleCountEntry.getKey());
//
//                           if (file.exists()) {
//                               File [] files = file.listFiles();
//                               if (files.length != 0) {
//                                   for (int j = 0; j < files.length; j++) {
//                                       files[j].delete();
//                                   }
//                               }
//                           }
//                           //从观看列表移除设备观看人数数据
//                           playingDeviceVisitPeopleCountIterator.remove();
//                       }
//                   }
//               }
            }
            mv.addObject("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
        }
        return mv.getModel();
    }

    /**
     * 更新设备观看人数列表
     *
     * @param playingDeviceVisitPeopleCount 设备观看人数列表
     * @author suxijian
     */
    private void updatePlayingDeviceVisitPeopleCount(Map<Integer, Integer> playingDeviceVisitPeopleCount) {
        //若观看人数为0删除图片
        Iterator<Map.Entry<Integer, Integer>> playingDeviceVisitPeopleCountIterator = playingDeviceVisitPeopleCount.entrySet().iterator();
        while (playingDeviceVisitPeopleCountIterator.hasNext()) {
            Map.Entry<Integer, Integer> playingDeviceVisitPeopleCountEntry = playingDeviceVisitPeopleCountIterator.next();
            //如果设备观看人数为0，则停止截图，并删除图片
//                       if (playingDeviceVisitPeopleCountEntry.getKey() == Integer.parseInt(deviceID)){
            if (playingDeviceVisitPeopleCountEntry.getValue() == 0) {
                //发送消息，停止截图上传
                sendMessageStopCut(playingDeviceVisitPeopleCountEntry.getKey().toString());
                               /*String smg="CAPT:<MSG>" +
                                       "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
                                       "<Type>5</Type>" +
                                       "<Info><ID>"+playingDeviceVisitPeopleCountEntry.getKey()+"</ID>" +
                                       "<State>0</State>" +
                                       "<Fps>50</Fps>" +
                                       "<FtpUserName>ftp</FtpUserName>" +
                                       "<FtpUserPwd>123456</FtpUserPwd>" +
                                       "<FtpPath>ftp://"+IPUtil.ip+"</FtpPath>" +
                                       "</Info></MSG>";
                               send.sendMsg(playingDeviceVisitPeopleCountEntry.getKey().toString(),smg);*/
                //test
//                               System.out.println("停止截图");
                //删除图片
                File file = new File("D://ftp/" + playingDeviceVisitPeopleCountEntry.getKey());

                if (file.exists()) {
                    File[] files = file.listFiles();
                    if (files.length != 0) {
                        for (int j = 0; j < files.length; j++) {
                            files[j].delete();
                        }
                    }
                }
                //从观看列表移除设备观看人数数据
                playingDeviceVisitPeopleCountIterator.remove();
//                               break;
            }
//                    }
        }
    }

    /**
     * 将device转为deviceVo
     */
    private DeviceVo deviceToDeviceVo(Device device) {
        DeviceVo deviceVo = new DeviceVo();
        deviceVo.setId(device.getId());
        deviceVo.setName(device.getName());
        deviceVo.setStationID(device.getStationID());
        deviceVo.setLineID(device.getLineID());
        deviceVo.setDeviceID(device.getDeviceID());
        return deviceVo;
    }

    /**
     * @param deviceId 当前播放显示设备id
     * @param lineId   线路id
     * @author suxijian
     * 轮询
     * 获取当前线路下一个设备信息
     */
    @RequestMapping("/getNextDevicePolling")
    @ResponseBody
    public Map<String, Object> getNextDevicePolling(ModelAndView mv, HttpSession session, String deviceId, String lineId, String divIndex) {

        //当前正在播放的设备列表
        List<VisitCount> playingDeviceList = (ArrayList<VisitCount>) session.getAttribute("playingDeviceList");

        //线路下所有设备列表
        List<VisitCount> lineDeviceList = (ArrayList<VisitCount>) session.getAttribute("lineDeviceList");

        //当前设备循环次数
//        Integer currentCycleIndex = 0;

        //下一个播放显示设备id
        Integer nextDeviceId = null;

        //设备画面观看人数记录
        Map<Integer, Integer> playingDeviceVisitPeopleCount = deviceVisitPeopleCount;

        //前端div与设备id的映射集合 divIndex --> deviceId
        Map<Integer, Integer> divIndexMapdeviceId = (Map<Integer, Integer>) session.getAttribute("divIndexMapdeviceId");

        //在线路设备列表中当前轮询到的位置
        Integer nextDeviceInLineDeviceListIndex = (Integer) session.getAttribute("nextDeviceInLineDeviceListIndex");
        if (nextDeviceInLineDeviceListIndex == null) {
            nextDeviceInLineDeviceListIndex = -1;
        }

        if (lineDeviceList == null) {
//            lineDeviceList =  new ArrayList<>();

            //获取线路下所有设备信息
            lineDeviceList = deviceService.getAllDeviceByLineId(Integer.parseInt(lineId));
//            session.setAttribute("lineDeviceList",lineDeviceList);
        }

        //找到下一个设备并更新相关信息以及返回相关信息

        if (nextDeviceInLineDeviceListIndex == (lineDeviceList.size() - 1)) {
            nextDeviceInLineDeviceListIndex = -1;
        }
        //是否在正在播放设备列表
        Boolean isInPlayingDeviceList;

        for (int i = nextDeviceInLineDeviceListIndex + 1; i < lineDeviceList.size(); i++) {
            isInPlayingDeviceList = false;
            //判断是否正在播放，若是，则判断下一个
            for (int j = 0; j < playingDeviceList.size(); j++) {
                if (lineDeviceList.get(i).getDeviceID().equals(playingDeviceList.get(j).getDeviceID())) {
                    isInPlayingDeviceList = true;
                    break;
                }
            }
            if (!isInPlayingDeviceList) {
                nextDeviceId = lineDeviceList.get(i).getDeviceID();
                nextDeviceInLineDeviceListIndex = i;
                break;
            }
        }

        //更新正在播放设备列表
        Iterator<VisitCount> playingDeviceListIt = playingDeviceList.iterator();
        while (playingDeviceListIt.hasNext()) {
            VisitCount playingDevice = playingDeviceListIt.next();
            if (playingDevice.getDeviceID() == Integer.parseInt(deviceId)) {
                playingDeviceListIt.remove();
                //更新设备观看人数
                if (playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceId)) > 0) {
                    playingDeviceVisitPeopleCount.put(Integer.parseInt(deviceId), playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceId)) - 1);
                }
                break;
            }

        }
        VisitCount playDevice = new VisitCount();
//           playDevice.setCount(1);
        playDevice.setAlarmNumber(0);
        playDevice.setRepeatCount(0);
        playDevice.setFileName("");//图片
        playDevice.setDeviceID(nextDeviceId);
        playingDeviceList.add(playDevice);

        //设备观看人数中未包含该设备(处于初始状态：无人观看，该设备未截图上传)
        if (!playingDeviceVisitPeopleCount.containsKey(nextDeviceId)) {
            //发送消息，使对应设备截图上传
            sendMessageStartCut(nextDeviceId.toString());
            /*String smg="CAPT:<MSG>" +
                    "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
                    "<Type>5</Type>" +
                    "<Info><ID>"+nextDeviceId+"</ID>" +
                    "<State>1</State>" +
                    "<Fps>50</Fps>" +
                    "<FtpUserName>ftp</FtpUserName>" +
                    "<FtpUserPwd>123456</FtpUserPwd>" +
                    "<FtpPath>ftp://"+ IPUtil.ip +"</FtpPath>" +
                    "</Info></MSG>";
            send.sendMsg(nextDeviceId.toString(),smg);*/
            //更新该设备观看人数
            playingDeviceVisitPeopleCount.put(nextDeviceId, 1);
        } else {
            //更新该设备观看人数
            playingDeviceVisitPeopleCount.put(nextDeviceId, playingDeviceVisitPeopleCount.get(nextDeviceId) + 1);
        }

        //更新前端div与设备id的映射集合 divIndex --> deviceId
        for (Map.Entry<Integer, Integer> entry : divIndexMapdeviceId.entrySet()) {
            if ((entry.getValue() == Integer.parseInt(deviceId)) && (entry.getKey() == Integer.parseInt(divIndex))) {
                entry.setValue(nextDeviceId);
                break;
            }
        }

        session.setAttribute("playingDeviceList", playingDeviceList);
        session.setAttribute("lineDeviceList", lineDeviceList);
        session.setAttribute("nextDeviceInLineDeviceListIndex", nextDeviceInLineDeviceListIndex);
        session.setAttribute("divIndexMapdeviceId", divIndexMapdeviceId);

        //将正在播放设备列表转为前端使用到的设备信息列表
        List<DeviceVo> playingDeviceVoList = new ArrayList<>();
        for (VisitCount playDeviceTemp : playingDeviceList) {
            //获取设备信息
            Device device = deviceService.selectDevice(playDeviceTemp.getDeviceID());
            DeviceVo deviceVo = deviceToDeviceVo(device);
            //获取车站名称
            Station station = stationService.selectStation(device.getStationID());
            deviceVo.setStationName(station.getName());
            //获取线路名称
            Line line = lineService.selectLine(device.getLineID());
            deviceVo.setLineName(line.getName());
            playingDeviceVoList.add(deviceVo);
        }
        mv.addObject("playingDeviceVoList", playingDeviceVoList);
        mv.addObject("nextDeviceId", nextDeviceId);

        return mv.getModel();
    }

    /**
     * @author suxijian
     * 获取画面图片
     */
    @RequestMapping("/getImagePolling")
    @ResponseBody
    public Map<String, Object> getImagePolling(ModelAndView mv, HttpSession session, String imageDirectoryName) {
        //当前正在播放的设备列表
        List<VisitCount> playingDeviceList = (ArrayList<VisitCount>) session.getAttribute("playingDeviceList");
        //设备画面观看人数记录
        Map<Integer, Integer> playingDeviceVisitPeopleCount = deviceVisitPeopleCount;
        //该设备id字符串
        String deviceIdStr = imageDirectoryName;
//        List<VisitCount> visitCounts=visitCountList;
        try {
            //显示的图片名，是文件夹最新图片的前一张图，即最后修改时间第二大的图片
            String imgName = null;
            File file = new File("D://ftp/" + imageDirectoryName);
            //最新图片的前一张图： 图片名 ---> 最后修改时间时间戳
            Map<String, Long> latestImageMap = (HashMap<String, Long>) session.getAttribute("latestImageMap");

            //保留两张图片(最新与次新)
            //每次取最后修改时间时间戳最大的前一张图片，每张图片文件的最后修改时间时间戳不能相同
            if ((!file.exists()) && ((Integer.parseInt(imageDirectoryName) != -1))) {// "D://ftp/"+imageDirectoryName 文件夹不存在
                // "D://ftp/"+imageDirectoryName 创建文件夹
                file.mkdirs();
            }

            //文件夹存在
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files.length == 0) { //"D://ftp/"+imageDirectoryName 文件夹为空
                    //多次请求后，若文件夹仍为空，则可提示设备连接异常Z
                    imgName = null;
                } else { //"D://ftp/"+imageDirectoryName 文件夹不为空

                    //最后修改时间的时间戳最大值
                    Long maxLastModifiedTime = 0L;

                    //最新图片名
                    String lastImageName = null;

                    //最后修改时间的时间戳第二大值
                    Long secondMaxLastModifiedTime = 0L;

                    //最后修改时间的时间戳
                    Long lastModifiedTime = 0L;

                    if (latestImageMap == null) {
                        latestImageMap = new HashMap<String, Long>();
                        latestImageMap.put("00", 0L);
                    }
                    for (File f : files) {
                        lastModifiedTime = f.lastModified();
                        if (lastModifiedTime >= maxLastModifiedTime) {

                            //最后修改时间第二大的图片
                            secondMaxLastModifiedTime = maxLastModifiedTime;
                            imgName = lastImageName;

                            //最后修改时间最大的图片
                            maxLastModifiedTime = lastModifiedTime;
                            lastImageName = f.getName();
                        }
                    }
                    if (!latestImageMap.containsKey(imgName)) {//找到的最新图片文件名与上次最新图片文件名不相同
                        //从Map中删除上次图片信息
                        latestImageMap.clear();
                        //保存当前最新的图片信息
                        latestImageMap.put(imgName, secondMaxLastModifiedTime);
                    }

                    //删除过期图片
                    for (File f : files) {
                        lastModifiedTime = f.lastModified();
                        if (lastModifiedTime < secondMaxLastModifiedTime) {
                            f.delete();
                        }
                    }
                }
            }
            if (playingDeviceList != null && playingDeviceList.size() > 0) {
                String deviceConnectState = "connectNormal";//设备连接状态，connectNormal表示连接正常,connectError表示连接异常,interrupt表示连接中断
                for (int i = playingDeviceList.size() - 1; i >= 0; i--) {
                    if (playingDeviceList.get(i).getDeviceID() == Integer.parseInt(deviceIdStr)) {

                        Integer repeatCount = playingDeviceList.get(i).getRepeatCount();
                        Integer alarmNumber = playingDeviceList.get(i).getAlarmNumber();
//                    Integer count=playingDeviceList.get(i).getCount();
                        if (imgName == null) {
                            alarmNumber++;
                            playingDeviceList.get(i).setAlarmNumber(alarmNumber);
                            if ((alarmNumber == 0) || (30 % alarmNumber == 0)) {
                                //发送消息截图上传
                                sendMessageStartCut(deviceIdStr);
                            }
                            if (alarmNumber > 30) {
                                //设备连接异常
                                deviceConnectState = "connectError";
                                //将该设备从播放中的设备列表移除
                                playingDeviceList.remove(i);
//                                session.setAttribute("playingDeviceList",playingDeviceList);
                                //减少该设备观看人数
                                if (playingDeviceVisitPeopleCount.containsKey(Integer.parseInt(deviceIdStr)) && playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceIdStr)) > 0) {
                                    playingDeviceVisitPeopleCount.put(Integer.parseInt(deviceIdStr), playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceIdStr)) - 1);
                                }
                                break;
                            }
                        } else {
                            playingDeviceList.get(i).setAlarmNumber(0);
                            if (playingDeviceList.get(i).getFileName().equals(imgName)) {
                                repeatCount++;
                                playingDeviceList.get(i).setRepeatCount(repeatCount);
                                if ((repeatCount == 0) || (30 % repeatCount) == 0) {
                                    //发送消息截图上传
                                    sendMessageStartCut(deviceIdStr);
                                }
                                if (repeatCount > 30) {
                                    //设备连接中断
                                    deviceConnectState = "interrupt";
                                    //将该设备从播放中的设备列表移除
                                    playingDeviceList.remove(i);
//                                    session.setAttribute("playingDeviceList",playingDeviceList);
                                    //减少该设备观看人数
                                    if (playingDeviceVisitPeopleCount.containsKey(Integer.parseInt(deviceIdStr)) && playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceIdStr)) > 0) {
                                        playingDeviceVisitPeopleCount.put(Integer.parseInt(deviceIdStr), playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceIdStr)) - 1);
                                    }
                                    break;
                                }
                            } else {
                                playingDeviceList.get(i).setRepeatCount(0);
                                playingDeviceList.get(i).setAlarmNumber(0);
                                playingDeviceList.get(i).setFileName(imgName);
                                mv.addObject("imgName", imgName);
                                break;
                            }
                        }
                    }
                }

                session.setAttribute("playingDeviceList", playingDeviceList);
                if (!"connectNormal".equals(deviceConnectState)) {
                    Device device = deviceService.selectDevice(Integer.parseInt(deviceIdStr));
                    Station station = stationService.selectStation(device.getStationID());
                    Line line = lineService.selectLine(device.getLineID());
                    String alarmDeviceFullName = line.getName() + "-" + station.getName() + "-" + device.getName();
                    mv.addObject("alarmDeviceFullName", alarmDeviceFullName);

                    if ("connectError".equals(deviceConnectState)) {//设备连接异常
                        mv.addObject("result", "connectError");
                    } else if ("interrupt".equals(deviceConnectState)) {//设备连接中断
                        mv.addObject("result", "interrupt");
                    }

                    //更新正在播放设备列表
//                    if ((playingDeviceList != null)){
//                        Iterator<VisitCount> playDeviceIt = playingDeviceList.iterator();
//                        while (playDeviceIt.hasNext()){
//                            VisitCount playDeviceIttemp = playDeviceIt.next();
//                            if (playDeviceIttemp.getDeviceID().equals(Integer.parseInt(deviceIdStr))){
//                                playDeviceIt.remove();
//                                //减少该设备观看人数
//                                if (playingDeviceVisitPeopleCount.containsKey(Integer.parseInt(deviceIdStr)) && playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceIdStr)) > 0){
//                                    playingDeviceVisitPeopleCount.put(Integer.parseInt(deviceIdStr),playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceIdStr))-1);
//                                }
//                                break;
//                            }
//                        }
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    /**
     * @author suxijian
     * 离开监看画面时，停止播放画面
     */
    @RequestMapping("/closePrisonWatchPolling")
    @ResponseBody
    public Map<String, Object> closePrisonWatchPolling(ModelAndView mv, HttpSession session) {

        //test
//        System.out.println("关闭或刷新页面");

        //当前正在播放的设备列表
        List<VisitCount> playingDeviceList = (ArrayList<VisitCount>) session.getAttribute("playingDeviceList");

        //设备画面观看人数记录
        Map<Integer, Integer> playingDeviceVisitPeopleCount = deviceVisitPeopleCount;

        //更新设备观看人数
        Iterator<VisitCount> playingDeviceListIterator = playingDeviceList.iterator();
        while (playingDeviceListIterator.hasNext()) {
            VisitCount playingDevice = playingDeviceListIterator.next();
            for (Map.Entry<Integer, Integer> playingDeviceVisitPeopleCountEntry : playingDeviceVisitPeopleCount.entrySet()) {
                if (playingDeviceVisitPeopleCountEntry.getKey() == playingDevice.getDeviceID()) {
                    if (playingDeviceVisitPeopleCountEntry.getValue() > 0) {
                        playingDeviceVisitPeopleCountEntry.setValue(playingDeviceVisitPeopleCountEntry.getValue() - 1);
                    }
                    break;
                }
            }
        }

        //判断设备观看人数是否为0.若是则停止截图并删除图片
        updatePlayingDeviceVisitPeopleCount(playingDeviceVisitPeopleCount);
//        Iterator<Map.Entry<Integer,Integer>> playingDeviceVisitPeopleCountIterator = playingDeviceVisitPeopleCount.entrySet().iterator();
//        while(playingDeviceVisitPeopleCountIterator.hasNext()){
//            Map.Entry<Integer,Integer> playingDeviceVisitPeopleCountEntry = playingDeviceVisitPeopleCountIterator.next();
//            //如果设备观看人数为0，则关闭设备
//            if (playingDeviceVisitPeopleCountEntry.getValue() == 0){
//                //发送消息，停止截图上传
//                sendMessageStopCut(playingDeviceVisitPeopleCountEntry.getKey().toString());
//                /*String smg="CAPT:<MSG>" +
//                        "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
//                        "<Type>5</Type>" +
//                        "<Info><ID>"+playingDeviceVisitPeopleCountEntry.getKey()+"</ID>" +
//                        "<State>0</State>" +
//                        "<Fps>50</Fps>" +
//                        "<FtpUserName>ftp</FtpUserName>" +
//                        "<FtpUserPwd>123456</FtpUserPwd>" +
//                        "<FtpPath>ftp://"+IPUtil.ip+"</FtpPath>" +
//                        "</Info></MSG>";
//                send.sendMsg(playingDeviceVisitPeopleCountEntry.getKey().toString(),smg);*/
//                //test
////                System.out.println("停止截图");
//                //删除图片
//                File file = new File("D://ftp/"+playingDeviceVisitPeopleCountEntry.getKey());
//                if (file.exists()) {
//                    File [] files = file.listFiles();
//                    if (files.length != 0) {
//                        for (int i = 0; i < files.length; i++) {
//                            files[i].delete();
//                        }
//                    }
//                }
//                //从观看列表移除设备观看人数数据
//                playingDeviceVisitPeopleCountIterator.remove();
//            }
//        }
        //还原各种数据
        session.setAttribute("playingDeviceList", null);
        session.setAttribute("lineDeviceList", null);
        session.setAttribute("nextDeviceInLineDeviceListIndex", null);
//        session.setAttribute("divIndexMapdeviceId",null);

        return mv.getModel();
    }

    /**
     * @param deviceID     移除的设备id
     * @param playStatus   移除的设备当前的状态 1表示播放，0表示停止
     * @param divIndex     移除的设备当前所在的div位置
     * @param pollingState 轮询状态 1表示轮询，0表示不轮询
     * @author suxijian
     * 从监看页面显示屏中移除设备
     */
    @RequestMapping("/deleteDeviceFromDevicelistPolling")
    @ResponseBody
    public Map<String, Object> deleteDeviceFromDevicelistPolling(ModelAndView mv, HttpSession session, String deviceID, Integer playStatus, String divIndex, String pollingState) {
        //当前正在播放的设备列表
        List<VisitCount> playingDeviceList = (ArrayList<VisitCount>) session.getAttribute("playingDeviceList");

        //设备画面观看人数记录
        Map<Integer, Integer> playingDeviceVisitPeopleCount = deviceVisitPeopleCount;

        //前端div与设备id的映射集合 divIndex --> deviceId
        Map<Integer, Integer> divIndexMapdeviceId = (Map<Integer, Integer>) session.getAttribute("divIndexMapdeviceId");

        //更新前端div与设备id的映射集合 divIndex --> deviceId
        Iterator<Map.Entry<Integer, Integer>> divIndexMapdeviceIdIterator = divIndexMapdeviceId.entrySet().iterator();
        while (divIndexMapdeviceIdIterator.hasNext()) {
            Map.Entry<Integer, Integer> divIndexMapdeviceIdEntry = divIndexMapdeviceIdIterator.next();
            if ((divIndexMapdeviceIdEntry.getValue() == Integer.parseInt(deviceID))) {
                if (divIndexMapdeviceIdEntry.getKey() == Integer.parseInt(divIndex)) {
                    divIndexMapdeviceIdEntry.setValue(-1);
                    session.setAttribute("divIndexMapdeviceId", divIndexMapdeviceId);
//                divIndexMapdeviceIdIterator.remove();
                    break;
                }
            }
        }

        //更新正在播放设备列表
        if ((playingDeviceList != null) && (playStatus == 0)) {
            Iterator<VisitCount> playDeviceIt = playingDeviceList.iterator();
            while (playDeviceIt.hasNext()) {
                VisitCount playDeviceIttemp = playDeviceIt.next();
                if (playDeviceIttemp.getDeviceID() == Integer.parseInt(deviceID)) {
                    playDeviceIt.remove();
                    //设置设备观看人数
                    if (playingDeviceVisitPeopleCount.containsKey(Integer.parseInt(deviceID)) && playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceID)) > 0) {
                        playingDeviceVisitPeopleCount.put(Integer.parseInt(deviceID), playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceID)) - 1);
                    }
                    break;
                }
            }
        }


        //遍历设备观看人数记录，判断是否有设备的观看人数为0，若观看人数为0，停止截图上传并删除图片
        updatePlayingDeviceVisitPeopleCount(playingDeviceVisitPeopleCount);

        //若当前设备观看人数为0，则停止截图
//        if (playingDeviceVisitPeopleCount.containsKey(Integer.parseInt(deviceID))){
////        if (playingDeviceVisitPeopleCount.get(Integer.parseInt(deviceID)) != null){
//            Iterator<Map.Entry<Integer,Integer>> playingDeviceVisitPeopleCountIterator = playingDeviceVisitPeopleCount.entrySet().iterator();
//            while(playingDeviceVisitPeopleCountIterator.hasNext()) {
//                Map.Entry<Integer, Integer> playingDeviceVisitPeopleCountEntry = playingDeviceVisitPeopleCountIterator.next();
//                if (playingDeviceVisitPeopleCountEntry.getKey() == Integer.parseInt(deviceID)) {
//                    //如果设备观看人数为0，则关闭设备
//                    if (playingDeviceVisitPeopleCountEntry.getValue() == 0) {
//                        //发送消息，停止截图上传
//                        sendMessageStopCut(playingDeviceVisitPeopleCountEntry.getKey().toString());
//                        /*String smg = "CAPT:<MSG>" +
//                                "<RouteKey>" + AmqpConfig.ROUTINGKEY + "</RouteKey>" +
//                                "<Type>5</Type>" +
//                                "<Info><ID>" + playingDeviceVisitPeopleCountEntry.getKey() + "</ID>" +
//                                "<State>0</State>" +
//                                "<Fps>50</Fps>" +
//                                "<FtpUserName>ftp</FtpUserName>" +
//                                "<FtpUserPwd>123456</FtpUserPwd>" +
//                                "<FtpPath>ftp://" + IPUtil.ip + "</FtpPath>" +
//                                "</Info></MSG>";
//                        send.sendMsg(playingDeviceVisitPeopleCountEntry.getKey().toString(), smg);*/
//
//                        //test
////                        System.out.println("停止截图");
//
//                        //删除图片文件
//                        File file = new File("D://ftp/"+deviceID);
//                        if (file.exists()) {
//                            File [] files = file.listFiles();
//                            if (files.length != 0){
//                                for (int i = 0; i < files.length; i++){
//                                    files[i].delete();
//                                }
//                            }
//                        }
//                        //从观看列表移除设备观看人数数据
//                        playingDeviceVisitPeopleCountIterator.remove();
//                        break;
//                    }
//                }
//            }
//        }

        //轮询状态下点击删除按钮，并且正在播放设备数为0
        if ("1".equals(pollingState)) {

            if (playingDeviceList != null) {
                //正在播放设备数为0
                if (playingDeviceList.size() == 0) {
                    //在线路设备列表中当前轮询到的位置
                    Integer nextDeviceInLineDeviceListIndex = (Integer) session.getAttribute("nextDeviceInLineDeviceListIndex");
                    //还原轮询位置，下次轮询将重新从线路设备列表第一个设备开始轮询
                    if (nextDeviceInLineDeviceListIndex != -1) {
                        nextDeviceInLineDeviceListIndex = -1;
                    }
                    session.setAttribute("nextDeviceInLineDeviceListIndex", nextDeviceInLineDeviceListIndex);

                    //遍历设备观看人数记录，判断是否有设备的观看人数为0，若观看人数为0，停止截图上传并删除图片
//                    Iterator<Map.Entry<Integer,Integer>> playingDeviceVisitPeopleCountIterator = playingDeviceVisitPeopleCount.entrySet().iterator();
//                    while(playingDeviceVisitPeopleCountIterator.hasNext()){
//                        Map.Entry<Integer,Integer> playingDeviceVisitPeopleCountEntry = playingDeviceVisitPeopleCountIterator.next();
//                        //如果设备观看人数为0，则停止截图，并删除图片
//                        if (playingDeviceVisitPeopleCountEntry.getValue() == 0){
//                            //发送消息，停止截图上传
//                            sendMessageStopCut(playingDeviceVisitPeopleCountEntry.getKey().toString());
//                               /*String smg="CAPT:<MSG>" +
//                                       "<RouteKey>"+ AmqpConfig.ROUTINGKEY +"</RouteKey>"+
//                                       "<Type>5</Type>" +
//                                       "<Info><ID>"+playingDeviceVisitPeopleCountEntry.getKey()+"</ID>" +
//                                       "<State>0</State>" +
//                                       "<Fps>50</Fps>" +
//                                       "<FtpUserName>ftp</FtpUserName>" +
//                                       "<FtpUserPwd>123456</FtpUserPwd>" +
//                                       "<FtpPath>ftp://"+IPUtil.ip+"</FtpPath>" +
//                                       "</Info></MSG>";
//                               send.sendMsg(playingDeviceVisitPeopleCountEntry.getKey().toString(),smg);*/
//                            //test
////                               System.out.println("停止截图");
//                            //删除图片
//                            File file = new File("D://ftp/"+playingDeviceVisitPeopleCountEntry.getKey());
//
//                            if (file.exists()) {
//                                File [] files = file.listFiles();
//                                if (files.length != 0) {
//                                    for (int j = 0; j < files.length; j++) {
//                                        files[j].delete();
//                                    }
//                                }
//                            }
//                            //从观看列表移除设备观看人数数据
//                            playingDeviceVisitPeopleCountIterator.remove();
//                        }
//                    }
                }
            }
        }
        return mv.getModel();
    }

    /**
     * @param deviceID 设备id
     * @author suxijian
     * 开始截图
     */
    //发送消息，使对应设备开始截图上传
    public void sendMessageStartCut(String deviceID) {
        String smg = "CAPT:<MSG>" +
                "<RouteKey>" + AmqpConfig.ROUTINGKEY + "</RouteKey>" +
                "<Type>5</Type>" +
                "<Info><ID>" + deviceID + "</ID>" +
                "<State>1</State>" +
                "<Fps>50</Fps>" +
                "<FtpUserName>ftp</FtpUserName>" +
                "<FtpUserPwd>123456</FtpUserPwd>" +
                "<FtpPath>ftp://" + IPUtil.IP + "</FtpPath>" +
                "</Info></MSG>";
        send.sendMsg(deviceID, smg);
    }

    /**
     * @param deviceID 设备id
     * @author suxijian
     * 停止截图
     */
    //发送消息，使对应设备停止截图上传
    public void sendMessageStopCut(String deviceID) {
        String smg = "CAPT:<MSG>" +
                "<RouteKey>" + AmqpConfig.ROUTINGKEY + "</RouteKey>" +
                "<Type>5</Type>" +
                "<Info><ID>" + deviceID + "</ID>" +
                "<State>0</State>" +
                "<Fps>50</Fps>" +
                "<FtpUserName>ftp</FtpUserName>" +
                "<FtpUserPwd>123456</FtpUserPwd>" +
                "<FtpPath>ftp://" + IPUtil.IP + "</FtpPath>" +
                "</Info></MSG>";
        send.sendMsg(deviceID, smg);
    }
}
