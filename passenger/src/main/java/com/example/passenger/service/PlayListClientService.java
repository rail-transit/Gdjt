package com.example.passenger.service;

import com.example.passenger.config.AmqpConfig;
import com.example.passenger.entity.Device;
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

import java.util.*;

@Service
public class PlayListClientService {
    private static final Logger logger = LoggerFactory.getLogger(PlayListClientService.class);

    @Autowired
    PlayListClientMapper playListClientMapper;

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    MsgSend msgSend;

    public PlayListClient selectPlayListClient(Integer id) {
        return playListClientMapper.selectPlayListClient(id);
    }

    public PlayListClient selectClientByPlayListID(Integer playListID, Integer clientID) {
        return playListClientMapper.selectClientByPlayListID(playListID, clientID);
    }

    public List<PlayListClient> previousRelease(Integer playListID, Integer clientID, String auditTime) {
        return playListClientMapper.previousRelease(playListID, clientID, auditTime);
    }

    public List<Map<String, String>> getVideoRelease(Integer lineID, Integer stationID, Integer deviceID,
                                                     String startDate, String endDate) {
        return playListClientMapper.getVideoRelease(lineID, stationID, deviceID,
                startDate, endDate);
    }

    public PageUtil selectPaging(Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playListClientMapper.count());
        pageUtil.setPageData(playListClientMapper.selectPaging(pageNum, pageSize));
        return pageUtil;
    }

    public PageUtil getDownloadSpeed(Integer playListID, Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playListClientMapper.DownloadSpeedCount(playListID));
        pageUtil.setPageData(playListClientMapper.getDownloadSpeed(playListID, pageNum, pageSize));
        return pageUtil;
    }

    public PageUtil releasePaging(Integer lineID, Integer stationID, Integer deviceID,
                                  String startDate, String endDate, Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playListClientMapper.releaseCount(lineID, stationID, deviceID,
                startDate, endDate));
        pageUtil.setPageData(playListClientMapper.releasePaging(lineID, stationID, deviceID,
                startDate, endDate, pageNum, pageSize));
        return pageUtil;
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void selectClientBySequence() {
        List<Map<String, String>> list = AmqpConfig.deviceList;
        if (list != null) {
            Set set = new HashSet();
            List<Map<String, String>> newList = new ArrayList();
            for (Map cd : list) {
                if (set.add(cd)) {
                    newList.add(cd);
                }
            }
            for (Map<String, String> stringMap : newList) {
                for (String key : stringMap.keySet()) {
                    System.out.println("设备ip:" + key);
                    Device device = deviceMapper.selectDeviceByIp(key);
                    if (device != null) {
                        List<PlayListClient> playListClients
                                = playListClientMapper.selectPlayListClientBySequence(device.getId());
                        if (playListClients.size() > 0) {
                            for (PlayListClient playListClient : playListClients) {
                                System.out.println("需要下载的播表:" + playListClient.getId());
                                //消息体
                                String content = "UPSC:http://" + IPUtil.ip + ":8080/schedules/getScheduleById?clientid=" +
                                        playListClient.getClientID() + "&idxs=" + playListClient.getPlaylistID();
                                //ip转换
                                long ip = IPUtil.ipToLong(device.getIp());
                                //发送下载命令
                                msgSend.sendMsg("pisplayer.*." + ip, content);
                                msgSend.sendMsg("pisplayer.*." + ip, "DLPS:");
                            }
                        }
                    }
                }
            }
        }
    }

    @Transactional
    public Integer addPlayListClient(PlayListClient playListClient) {
        try {
            return playListClientMapper.addPlayListClient(playListClient);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updatePlayListClient(PlayListClient playListClient) {
        try {
            return playListClientMapper.updatePlayListClient(playListClient);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deletePlayListClient(Integer id) {
        try {
            return playListClientMapper.deletePlayListClient(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}
