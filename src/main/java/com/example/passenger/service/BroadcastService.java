package com.example.passenger.service;

import com.example.passenger.config.AmqpConfig;
import com.example.passenger.entity.Broadcast;
import com.example.passenger.entity.Device;
import com.example.passenger.entity.Line;
import com.example.passenger.entity.Station;
import com.example.passenger.mapper.BroadcastMapper;
import com.example.passenger.mapper.DeviceMapper;
import com.example.passenger.mapper.LineMapper;
import com.example.passenger.mapper.StationMapper;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MsgSend;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BroadcastService {
    @Autowired
    BroadcastMapper broadcastMapper;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    StationMapper stationMapper;
    @Autowired
    LineMapper lineMapper;
    @Autowired
    MsgSend msgSend;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateLog(){
        List<Map<String,String>> list= AmqpConfig.deviceList;
        if(list!=null) {
            Set set = new HashSet();
            List<Map<String, String>> newList = new ArrayList();
            for (Map cd : list) {
                if (set.add(cd)) {
                    newList.add(cd);
                }
            }
            for (Map<String, String> stringMap : newList) {
                for (String key : stringMap.keySet()) {
                    Device device = deviceMapper.selectDeviceByIp(key);
                    //消息体
                    String content = "UPLG:ftp://ftp:FTPmedia@10.1.9.11/playLog/" ;
                    //ip转换
                    long ip = IPUtil.ipToLong(device.getIp());
                    //发送下载命令
                    msgSend.sendMsg("pisplayer.*." + ip, content);
                    System.out.println("发送上传播放日志消息");
                }
            }
        }
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void handleStatistics(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Broadcast> rollmsgList=new ArrayList<>();
        List<Broadcast> videoList=new ArrayList<>();
        Connection conn = null;
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            File file=new File("D:\\ftp\\playLog");
            if(file.exists()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++){
                    if(files[i].getName().indexOf(sdf.format(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)))!=-1){
                        String deviceIP=files[i].getName().substring(0,files[i].getName().indexOf("-"));
                        Device device=deviceMapper.selectDeviceByIp(IPUtil.longToIP(Long.valueOf(deviceIP)));
                        Station station=stationMapper.selectStation(device.getStationID());
                        Line line=lineMapper.selectLine(device.getLineID());
                        conn = DriverManager.getConnection("jdbc:sqlite:"+files[i].getPath());
                        statement = conn.createStatement();
                        resultSet = statement.executeQuery("SELECT description,ftype,time FROM log where event='STARTPLAYING'");
                        while (resultSet.next()){
                            Broadcast broadcast=new Broadcast();
                            String type=resultSet.getString("ftype");
                            if (type.equals("video")){
                                broadcast.setName(resultSet.getString("description"));
                                broadcast.setLineID(line.getId());
                                broadcast.setStationID(station.getId());
                                broadcast.setDeviceID(device.getId());
                                broadcast.setPlayCount(1);
                                broadcast.setDuration(resultSet.getString("time"));
                                broadcast.setPlayDate(resultSet.getString("time"));
                                broadcast.setType(0);
                                videoList.add(broadcast);
                            }
                            if(type.equals("rollmsg")){
                                broadcast.setName(resultSet.getString("description"));
                                broadcast.setLineID(line.getId());
                                broadcast.setStationID(station.getId());
                                broadcast.setDeviceID(device.getId());
                                broadcast.setPlayCount(1);
                                broadcast.setDuration("0");
                                broadcast.setPlayDate(resultSet.getString("time"));
                                broadcast.setType(1);
                                rollmsgList.add(broadcast);
                            }
                        }
                        if(videoList.size()>0){
                            handleCount(videoList);
                        }
                        if(rollmsgList.size()>0){
                            handleCount(rollmsgList);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 处理播放时长和播放次数 写入数据库
     * @param list
     * @return
     */
    public List<Broadcast> handleCount(List<Broadcast> list){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Broadcast> broadcastList=new ArrayList<>();
        try {
            //处理时长
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType()==0){
                    Date d1=null;
                    Date d2=null;
                    if (i+1<list.size()){
                        d1 = df.parse(list.get(i).getDuration());
                        d2 = df.parse(list.get(i+1).getDuration());
                        list.get(i).setDuration(String.valueOf((d2.getTime() - d1.getTime()) / 1000 ));
                    }else{
                        /*d1 = df.parse(mapList.get(i-1).getDuration());
                        d2 = df.parse(mapList.get(i).getDuration());*/
                        list.get(i).setDuration("0");
                    }
                }
            }
            //统计次数和时长
            for (int i = 0; i < list.size(); i++) {
                int num=list.get(i).getPlayCount();
                int duration=Integer.parseInt(list.get(i).getDuration());
                for (int j = 0; j < list.size(); j++) {
                    if(i==j) {
                        continue;
                    }else {
                        if (list.get(i).getName().equals(list.get(j).getName())) {
                            num += list.get(j).getPlayCount();
                            duration += Integer.parseInt(list.get(i).getDuration());
                        }
                    }
                }
                Broadcast broadcast=new Broadcast();
                broadcast.setLineID(list.get(i).getLineID());
                broadcast.setStationID(list.get(i).getStationID());
                broadcast.setDeviceID(list.get(i).getDeviceID());
                broadcast.setName(list.get(i).getName());
                broadcast.setDuration(String.valueOf(duration));
                broadcast.setPlayDate(list.get(i).getPlayDate());
                broadcast.setPlayCount(num);
                broadcast.setType(list.get(i).getType());
                broadcastList.add(broadcast);
            }
            //处理重复
            for (int i = 0; i < broadcastList.size() - 1; i++) {
                for (int j = broadcastList.size() - 1; j > i; j--) {
                    if (broadcastList.get(j).getName().equals(broadcastList.get(i).getName())) {
                        broadcastList.remove(j);
                    }
                }
            }
            //写入数据库
            for (Broadcast broadcast:broadcastList) {
                broadcastMapper.addBroadcast(broadcast);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return broadcastList;
    }

    public PageUtil broadcastPaging(Integer lineID,Integer stationID,Integer deviceID,Integer type,
                                    String startDate,String endDate,Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(broadcastMapper.broadcastCount(lineID,stationID,deviceID,type,startDate,endDate));
        pageUtil.setPageData(broadcastMapper.broadcastPaging(lineID,stationID,deviceID,type,startDate,endDate,pageNum,pageSize));
        return pageUtil;
    }

    public List<Map<String,String>> getBroadcast(Integer lineID,Integer stationID,Integer deviceID,Integer type,
                                                 String startDate,String endDate){
        return broadcastMapper.getBroadcast(lineID,stationID,deviceID,type,startDate,endDate);
    }

   /* public List<BroadcastVo> show(Integer lineID,Integer stationID,Integer id,String startDate, String endDate,Integer state) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Connection conn = null;
        ResultSet resultSet = null;
        Statement statement = null;
        List<BroadcastVo> broadcastList=new ArrayList<>();
        List<BroadcastVo> mapList=new ArrayList<>();
        List<Device> deviceList=getDeviceList(lineID,stationID,id);
        List<String> list=getDate(startDate,endDate);
        try {
            if (list.size()!=0){
                for (String s:list){
                    for (Device device:deviceList){
                        Line line=lineMapper.selectLine(device.getLineID());
                        Station station=stationMapper.selectStation(device.getStationID());
                        File file=new File("D:\\ftp\\playLog\\"+ IPUtil.ipToLong(device.getIp())+
                                "-"+s+".sqlite");
                        if(file.exists()){
                            Class.forName("org.sqlite.JDBC");
                            conn = DriverManager.getConnection("jdbc:sqlite:D:\\ftp\\playLog\\"+ IPUtil.ipToLong(device.getIp())+
                                    "-"+s+".sqlite");
                            statement = conn.createStatement();
                            resultSet = statement.executeQuery("SELECT description,ftype,time FROM log where event='STARTPLAYING'");

                            while (resultSet.next()){
                                BroadcastVo broadcast=new BroadcastVo();
                                String type=resultSet.getString("ftype");
                                if(state==0){
                                    if (type.equals("video")){
                                        broadcast.setDescription(resultSet.getString("description"));
                                        broadcast.setLineName(line.getName());
                                        broadcast.setStationName(station.getName());
                                        broadcast.setDeviceName(device.getName());
                                        broadcast.setDuration(resultSet.getString("time"));
                                        broadcast.setCount(1);
                                        mapList.add(broadcast);
                                    }
                                }else if (state==1){
                                    if (type.equals("rollmsg")){
                                        broadcast.setDescription(resultSet.getString("description"));
                                        broadcast.setLineName(line.getName());
                                        broadcast.setStationName(station.getName());
                                        broadcast.setDeviceName(device.getName());
                                        broadcast.setDuration("0");
                                        broadcast.setCount(1);
                                        mapList.add(broadcast);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (state==0){
                for (int i = 0; i < mapList.size(); i++) {
                    Date d1=null;
                    Date d2=null;
                    if (i+1<mapList.size()){
                        d1 = df.parse(mapList.get(i).getDuration());
                        d2 = df.parse(mapList.get(i+1).getDuration());
                        mapList.get(i).setDuration(String.valueOf((d2.getTime() - d1.getTime()) / 1000 ));
                    }else{
                  *//*  d1 = df.parse(mapList.get(i-1).getDuration());
                    d2 = df.parse(mapList.get(i).getDuration());*//*
                        mapList.get(i).setDuration("0");
                        //System.out.println((d2.getTime() - d1.getTime()) / 1000 + "秒1");
                    }
                }
            }
            for (int i = 0; i < mapList.size(); i++) {
                int num=mapList.get(i).getCount();
                int duration=Integer.parseInt(mapList.get(i).getDuration());
                for (int j = 0; j < mapList.size(); j++) {
                    if(i==j) {
                        continue;
                    }else {
                        if (mapList.get(i).getDescription().equals(mapList.get(j).getDescription())) {
                            num += mapList.get(j).getCount();
                            duration += Integer.parseInt(mapList.get(i).getDuration());
                        }
                    }
                }
                BroadcastVo broadcast=new BroadcastVo();
                broadcast.setLineName(mapList.get(i).getLineName());
                broadcast.setStationName(mapList.get(i).getStationName());
                broadcast.setDeviceName(mapList.get(i).getDeviceName());
                broadcast.setDescription(mapList.get(i).getDescription());
                broadcast.setDuration(String.valueOf(duration));
                broadcast.setCount(num);
                broadcastList.add(broadcast);
            }
        }catch (ClassNotFoundException ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        catch (SQLException sqlex){
            System.out.println(sqlex.getMessage());
            sqlex.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                conn.close();
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return broadcastList;
    }

    public List<Device> getDeviceList(Integer lineID,Integer stationID,Integer deviceID){
        List<Device> deviceList=deviceMapper.getDeviceList(lineID,stationID,deviceID);
        return deviceList;
    }

    public List<String> getDate(String startDate, String endDate){
        List<String> lDate = new ArrayList<String>();
        try {
            if (startDate!=null && endDate!=null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                lDate.add(startDate);//把开始时间加入集合
                Calendar cal = Calendar.getInstance();
                //使用给定的 Date 设置此 Calendar 的时间
                cal.setTime(sdf.parse(startDate));
                boolean bContinue = true;
                while (bContinue) {
                    //根据日历的规则，为给定的日历字段添加或减去指定的时间量
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    // 测试此日期是否在指定日期之后
                    if (sdf.parse(endDate).after(cal.getTime())) {
                        lDate.add(sdf.format(cal.getTime()));
                    } else {
                        break;
                    }
                }
                lDate.add(endDate);//把结束时间加入集合
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return lDate;
    }*/
}
