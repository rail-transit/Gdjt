package com.example.passenger.service;

import com.example.passenger.entity.Device;
import com.example.passenger.entity.Message;
import com.example.passenger.mapper.DeviceMapper;
import com.example.passenger.mapper.MessageMapper;
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

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    MsgSend msgSend;


    public Message selectMessage(Integer id){
        return messageMapper.selectMessage(id);
    }

    public Integer selectMessageID(){
        return messageMapper.selectMessageID();
    }

    public PageUtil selectPaging(Integer playState,Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(messageMapper.count(playState));
        pageUtil.setPageData(messageMapper.selectPaging(playState,pageNum,pageSize));
        return pageUtil;
    }


    @Scheduled(cron = "0/5 * * * * ?")
    public void selectMessageByStartTime(){
        List<Message> messageList=messageMapper.selectMessageByStartTime();
        for (Message message:messageList){
            if(message.getStartDate().length()!=0){
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
                msgSend.sendMsg("pisplayer.*."+ip,content);
                messageMapper.updateMessage(message.getId(),null,1);
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
                msgSend.sendMsg("pisplayer.*."+ip,content);
                messageMapper.updateMessage(message.getId(),null,-1);
            }

        }
    }

    @Transactional
    public Integer addMessage(Message message){
        try {
            message.setState(0);
            message.setPlayState(0);
            message.setType(11);
            message.setIsPlanMsg(0);
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
