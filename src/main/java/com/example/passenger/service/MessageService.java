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

    public Message getMessageByLevel(Integer deviceID,String level){
        return messageMapper.getMessageByLevel(deviceID,level);
    }

    public Integer getMessageByDeviceID(Integer deviceID){
        return messageMapper.getMessageByDeviceID(deviceID);
    }
    public List<Message> getMessage(String level,Integer deviceID){
        return messageMapper.getMessage(level,deviceID);
    }

    public  Integer getMaxLevel(Integer deviceID){
        return messageMapper.getMaxLevel(deviceID);
    }

    public Message selectMessage(Integer id){
        return messageMapper.selectMessage(id);
    }

    public Integer selectMessageID(){
        return messageMapper.selectMessageID();
    }

    public PageUtil selectPaging(Integer state,String msg,Integer playState,
                                 Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(messageMapper.count(state,msg,playState));
        pageUtil.setPageData(messageMapper.selectPaging(state,msg,playState,pageNum,pageSize));
        return pageUtil;
    }

    public PageUtil messageStatistics(Integer lineID,Integer stationID,Integer deviceID,
                                      String startDate,String endDate,Integer pageNum,
                                      Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(messageMapper.countStatistics(lineID,stationID,deviceID,startDate,endDate));
        pageUtil.setPageData(messageMapper.messageStatistics(lineID,stationID,deviceID,startDate,endDate,pageNum,pageSize));
        return pageUtil;
    }

    public List<Map<String,String>> getRelease(Integer lineID, Integer stationID, Integer deviceID,
                                               String startDate, String endDate){
        return messageMapper.getRelease(lineID,stationID,deviceID,startDate,endDate);
    }


    @Scheduled(cron = "0/5 * * * * ?")
    public void selectMessageByStartTime(){
        Integer i=0;
        List<Message> messageList=messageMapper.selectMessageByStartTime();
        for (Message message:messageList){
            if(message.getStartDate().length()!=0){
                if(message.getIsPlanMsg()==1){
                    List<Message> messageList1=messageMapper.getMessageByIsPlanMsg();
                    for (Message message1:messageList1){
                        if(message.getId().equals(message1.getId())){
                            i=1;
                        }else{
                            i=0;
                        }
                    }
                }else{
                    i=0;
                }
                if(i>0){
                    continue;
                }
                List<Message> messageList1=messageMapper.getMessage(message.getLevel(),message.getDeviceID());
                for (Message message1:messageList1){
                    messageMapper.updateMessage(message1.getId(),null,-1);
                }
                String content="PMSG:<MSG>" +
                        "<Type>"+message.getType()+"</Type>" +
                        "<Info>" +
                        "<ID>"+message.getId()+"</ID>" +
                        "<CtrlID>"+message.getCtrlID()+"</CtrlID>" +
                        "<Level>"+message.getLevel()+"</Level>" +
                        "<State>1</State>" +
                        "<Text>"+message.getMsg()+"</Text>" +
                        "</Info></MSG>";
                Device device=deviceMapper.selectDevice(message.getDeviceID());
                long ip=IPUtil.ipToLong(device.getIp());
                msgSend.sendMsg("pisplayer.occ."+ip,content);
                System.out.println("消息下发");
                Integer level=messageMapper.getMaxLevel(message.getDeviceID());
                if(level!=null){
                    MsgLevel msgLevel=msgLevelMapper.selectMsgLevelByLevel(message.getLevel(),null);
                    if(msgLevel.getLevel()>level){
                        Integer messageID=messageMapper.getMessageByDeviceID(message.getDeviceID());
                        messageMapper.updateMessage(messageID,null,2);
                        messageMapper.updateMessage(message.getId(),null,1);
                    }else{
                        messageMapper.updateMessage(message.getId(),null,2);
                    }

                }else{
                    messageMapper.updateMessage(message.getId(),null,1);
                }
            }
        }
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void selectMessageByEndTime(){
        List<Message> messageList=messageMapper.selectMessageByEndTime();
        for (Message message:messageList){
            if(message.getEndDate().length()!=0){
                String content="PMSG:<MSG>" +
                        "<Type>"+message.getType()+"</Type>" +
                        "<Info>" +
                        "<ID>"+message.getId()+"</ID>" +
                        "<CtrlID>"+message.getCtrlID()+"</CtrlID>" +
                        "<Level>"+message.getLevel()+"</Level>" +
                        "<State>0</State>" +
                        "<Text>"+message.getMsg()+"</Text>" +
                        "</Info></MSG>";
                Device device=deviceMapper.selectDevice(message.getDeviceID());
                long ip=IPUtil.ipToLong(device.getIp());
                msgSend.sendMsg("pisplayer.occ."+ip,content);
                messageMapper.updateMessage(message.getId(),null,-1);
            }

        }
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void selectMessageByPlanMsg(){
        List<Message> messageList=messageMapper.selectMessageByPlanMsg();
        for (Message message:messageList){
            if(message.getEndDate().length()!=0){
                String content="PMSG:<MSG>" +
                        "<Type>"+message.getType()+"</Type>" +
                        "<Info>" +
                        "<ID>"+message.getId()+"</ID>" +
                        "<CtrlID>"+message.getCtrlID()+"</CtrlID>" +
                        "<Level>"+message.getLevel()+"</Level>" +
                        "<State>0</State>" +
                        "<Text>"+message.getMsg()+"</Text>" +
                        "</Info></MSG>";
                Device device=deviceMapper.selectDevice(message.getDeviceID());
                long ip=IPUtil.ipToLong(device.getIp());
                msgSend.sendMsg("pisplayer.occ."+ip,content);
                System.out.println("发送定时取消");
                messageMapper.updateMessage(message.getId(),null,0);
            }

        }
    }

    @Transactional
    public Integer addMessage(Message message){
        try {
            message.setState(0);
            message.setPlayState(0);
            return messageMapper.addMessage(message);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateMessage(Integer id,Integer state,Integer playState){
        try {
            return messageMapper.updateMessage(id,state,playState);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteMessage(Integer id){
        try {
            return messageMapper.deleteMessage(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}
