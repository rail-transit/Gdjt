package com.example.passenger.service;

import com.example.passenger.entity.Device;
import com.example.passenger.entity.Message;
import com.example.passenger.entity.MsgLevel;
import com.example.passenger.mapper.DeviceMapper;
import com.example.passenger.mapper.MessageMapper;
import com.example.passenger.mapper.MsgLevelMapper;
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
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    MessageMapper messageMapper;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    MsgLevelMapper msgLevelMapper;
    @Autowired
    MsgSend msgSend;

    public List<Message> getMessage(String level, Integer deviceID) {
        return messageMapper.getMessage(level, deviceID);
    }

    public Message getMessageByCondition(Integer playState, Integer deviceID, String level,String msg) {
        return messageMapper.getMessageByCondition(playState, deviceID, level,msg);
    }

    public List<Message> queryGroupingMessage(){
        return messageMapper.queryGroupingMessage();
    }

    public Message selectMessage(Integer id) {
        return messageMapper.selectMessage(id);
    }

    public Integer getMaxLevel(Integer deviceID) {
        return messageMapper.getMaxLevel(deviceID);
    }


    public PageUtil selectPaging(Integer lineID, Integer stationID, Integer deviceID, String startDate,
                                 String endDate, Integer state, String msg, Integer playState,
                                 Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(messageMapper.count(lineID, stationID, deviceID, startDate, endDate, state, msg, playState));
        pageUtil.setPageData(messageMapper.selectPaging(lineID, stationID, deviceID, startDate, endDate, state, msg, playState, pageNum, pageSize));
        return pageUtil;
    }

    public List<Map<String, String>> getRelease(Integer lineID, Integer stationID, Integer deviceID,
                                                String startDate, String endDate) {
        return messageMapper.getRelease(lineID, stationID, deviceID, startDate, endDate);
    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void selectMessageByStartTime() {
        List<Message> messageList = messageMapper.selectMessageByStartTime();
        for (Message message : messageList) {
            if (message.getStartDate().length() != 0) {
                if (message.getIsPlanMsg() == 1) {
                    Message msg = messageMapper.getMessageByIsPlanMsg(message.getId());
                    if (msg != null) {
                        continue;
                    }
                }
                //消息下发
                String content = "PMSG:<MSG>" +
                        "<Type>" + message.getType() + "</Type>" +
                        "<Info>" +
                        "<ID>" + message.getId() + "</ID>" +
                        "<CtrlID>" + message.getCtrlID() + "</CtrlID>" +
                        "<Level>" + message.getLevel() + "</Level>" +
                        "<State>1</State>" +
                        "<Text>" + message.getMsg() + "</Text>" +
                        "</Info></MSG>";
                Device device = deviceMapper.selectDevice(message.getDeviceID());
                long ip = IPUtil.ipToLong(device.getIp());
                msgSend.sendMsg("pisplayer.occ." + ip, content);
                System.out.println("消息下发");
                //将当前设备级别相同的消息改为历史消息
                List<Message> messageList1 = messageMapper.getMessage(message.getLevel(), message.getDeviceID());
                for (Message message1 : messageList1) {
                    messageMapper.updateMessage(message1.getId(), null, -1);
                }
                //根据级别设置消息类型(待播,在播,待发布)
                //获取当前设备最高优先级
                Integer level = messageMapper.getMaxLevel(message.getDeviceID());
                if (level != null) {
                    //根据消息级别代码获取优先级
                    MsgLevel msgLevel = msgLevelMapper.selectMsgLevelByLevel(message.getLevel(), null);
                    if (msgLevel != null) {
                        //如当前消息优先级大于设备最高优先级
                        if (msgLevel.getLevel() > level) {
                            Message message1 = messageMapper.getMessageByCondition(1,
                                    message.getDeviceID(), null,null);
                            if (message1 != null) {
                                messageMapper.updateMessage(message1.getId(), null, 2);
                            }
                            messageMapper.updateMessage(message.getId(), null, 1);
                        } else {
                            messageMapper.updateMessage(message.getId(), null, 2);
                        }
                    }
                } else {
                    messageMapper.updateMessage(message.getId(), null, 1);
                }
            }
        }
    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void selectMessageByEndTime() {
        List<Message> messageList = messageMapper.selectMessageByEndTime();
        for (Message message : messageList) {
            if (message.getEndDate().length() != 0) {
                String content = "PMSG:<MSG>" +
                        "<Type>" + message.getType() + "</Type>" +
                        "<Info>" +
                        "<ID>" + message.getId() + "</ID>" +
                        "<CtrlID>" + message.getCtrlID() + "</CtrlID>" +
                        "<Level>" + message.getLevel() + "</Level>" +
                        "<State>0</State>" +
                        "<Text>" + message.getMsg() + "</Text>" +
                        "</Info></MSG>";
                Device device = deviceMapper.selectDevice(message.getDeviceID());
                long ip = IPUtil.ipToLong(device.getIp());
                msgSend.sendMsg("pisplayer.occ." + ip, content);
                messageMapper.updateMessage(message.getId(), null, -1);
                setBroadcastMsg(message);
                System.out.println("实时结束");
            }
        }
    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void selectMessageByPlanMsg() {
        List<Message> messageList = messageMapper.selectMessageByPlanMsg();
        for (Message message : messageList) {
            if (message.getEndDate().length() != 0) {
                String content = "PMSG:<MSG>" +
                        "<Type>" + message.getType() + "</Type>" +
                        "<Info>" +
                        "<ID>" + message.getId() + "</ID>" +
                        "<CtrlID>" + message.getCtrlID() + "</CtrlID>" +
                        "<Level>" + message.getLevel() + "</Level>" +
                        "<State>0</State>" +
                        "<Text>" + message.getMsg() + "</Text>" +
                        "</Info></MSG>";
                Device device = deviceMapper.selectDevice(message.getDeviceID());
                long ip = IPUtil.ipToLong(device.getIp());
                msgSend.sendMsg("pisplayer.occ." + ip, content);
                messageMapper.updateMessage(message.getId(), null, 0);
                setBroadcastMsg(message);
                System.out.println("定时结束");
            }
        }
    }

    public void setBroadcastMsg(Message msg) {
        Integer level = messageMapper.getMaxLevel(msg.getDeviceID());
        if (level != null) {
            MsgLevel msgLevel = msgLevelMapper.selectMsgLevelByLevel(null, level);
            if (msgLevel != null) {
                Message message = messageMapper.getMessageByCondition(2, msg.getDeviceID(),
                        msgLevel.getLevelCode(),null);
                if (message != null) {
                    messageMapper.updateMessage(message.getId(), null, 1);
                }
            }
        }
    }

    @Transactional
    public Integer addMessage(Message message) {
        try {
            message.setState(0);
            message.setPlayState(0);
            return messageMapper.addMessage(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateMessage(Integer id, Integer state, Integer playState) {
        try {
            return messageMapper.updateMessage(id, state, playState);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteMessage(Integer id) {
        try {
            return messageMapper.deleteMessage(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}
