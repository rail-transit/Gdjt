package com.example.passenger.service;

import com.example.passenger.config.AmqpConfig;
import com.example.passenger.entity.PlayListClient;
import com.example.passenger.mapper.DeviceMapper;
import com.example.passenger.mapper.PlayListClientMapper;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MsgSend;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PlayListClientService {
    private static final Logger logger = LoggerFactory.getLogger(PlayListClientService.class);

    @Autowired
    PlayListClientMapper playListClientMapper;

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    MsgSend msgSend;

    public PlayListClient selectPlayListClient(Integer id){
        return playListClientMapper.selectPlayListClient(id);
    }

    public Integer selectClientByPlayListID(Integer playListID,Integer clientID){
        return playListClientMapper.selectClientByPlayListID(playListID,clientID);
    }

    public Integer selectClientByPlayID(Integer clientID,Integer playListID){
        return playListClientMapper.selectClientByPlayID(clientID,playListID);
    }

    public PageUtil selectPaging(Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playListClientMapper.count());
        pageUtil.setPageData(playListClientMapper.selectPaging(pageNum,pageSize));
        return pageUtil;
    }

   /* @Scheduled(cron = "0/3 * * * * ?")
    public void addDownload(){
        List<String> download=AmqpConfig.downloadList;
        if(download!=null){
            for (int i=0;i<download.size();i++){
                String content=download.get(i).substring(download.get(i).lastIndexOf(':')+1);
                String[] arr=content.split(";");
                Integer count=null;
                PlayListDownloadStatus playListDownloadStatus=new PlayListDownloadStatus();
                for (int j=0;j<arr.length;j++){
                    //通过ip获取设备
                    Device device=deviceMapper.selectDeviceByIp(IPUtil.longToIP(Long.valueOf(arr[1])));
                    //初始化对象
                    playListDownloadStatus.setPlaylistID(arr[0]);
                    playListDownloadStatus.setClientID(device.getId().toString());
                    playListDownloadStatus.setFileName(arr[2]);
                    playListDownloadStatus.setStatus(arr[3]);
                   if(arr.length>4){
                       playListDownloadStatus.setPercen(arr[4]);
                       playListDownloadStatus.setSpeed(arr[5]);
                   }
                    count=playListDownloadStatusMapper.selectDownload(arr[0],device.getId().toString(),arr[2]);
                }
                if (count!=null){
                    System.out.println("执行修改");
                    playListDownloadStatusMapper.updateDownload(playListDownloadStatus);
                }else{
                    System.out.println("执行添加");
                    playListDownloadStatusMapper.addDownload(playListDownloadStatus);
                }
                download.remove(i);
            }
        }
    }*/

   /* @Scheduled(cron = "0/3 * * * * ?")
    public void updateClient(){
        List<String> updateList=AmqpConfig.updateList;
        if(updateList!=null){
            for (int i=0;i<updateList.size();i++){
                String deviceIP= IPUtil.longToIP(Long.valueOf(updateList.get(i).substring(5,
                        updateList.get(i).indexOf(";"))));
                String playID= updateList.get(i).substring(updateList.get(i).lastIndexOf(';')+1);
                System.out.println("修改状态:"+updateList.get(i));
                //获取设备ID
                Device device=deviceMapper.selectDeviceByIp(deviceIP);
                //获取ClientID
                Integer clientID=playListClientMapper.selectClientByPlayID(device.getId(),Integer.parseInt(playID));
                //修改状态
                PlayListClient playListClient1=new PlayListClient();
                playListClient1.setId(clientID);
                playListClient1.setSequence(1);
                Integer count=playListClientMapper.updatePlayListClient(playListClient1);
                if (count>0){
                    System.out.println("修改成功");
                    //移除map
                    updateList.remove(i);
                }
            }
        }
    }*/

    @Scheduled(cron = "0/5 * * * * ?")
    public void selectClientBySequence(){
        List<Map<String,String>> list= AmqpConfig.deviceList;
        //if(list!=null){
           /* Set set = new HashSet();
            List<String> newList = new ArrayList();
            for (String cd:list) {
                if(set.add(cd)){
                    newList.add(cd);
                }
            }*/
            //for (Map<String,String> stringMap :list){
                //for (String key:stringMap.keySet()){
                   /* System.out.println("设备id:"+key);
                    Device device=deviceMapper.selectDeviceByIp(key);
                    System.out.println("设备名称:"+device.getName());*/
                    List<PlayListClient> playListClients
                            =playListClientMapper.selectPlayListClientBySequence(1);
                    if(playListClients!=null){
                        for (PlayListClient playListClient:playListClients){
                            System.out.println("需要下载的播表:"+playListClient.getId());
                            //消息体
                            String content="UPSC:http://10.1.9.168:8080/schedules/getScheduleById?clientid="+
                                    playListClient.getClientID()+"&idxs="+playListClient.getPlaylistID();
                            //ip转换
                            long ip= IPUtil.ipToLong("10.1.9.81");
                            //发送下载命令
                            msgSend.sendMsg("pisplayer.*."+ip,content);
                        }
                    }
               // }
            //}
       // }
    }

    @Transactional
    public Integer addPlayListClient(PlayListClient playListClient){
        try {
            return playListClientMapper.addPlayListClient(playListClient);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updatePlayListClient(PlayListClient playListClient){
        try {
            return playListClientMapper.updatePlayListClient(playListClient);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deletePlayListClient(Integer id){
        try {
            return playListClientMapper.deletePlayListClient(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}
