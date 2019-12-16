package com.example.passenger.service;

import com.example.passenger.config.AmqpConfig;
import com.example.passenger.entity.MsgLevel;
import com.example.passenger.mapper.MsgLevelMapper;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MsgSend;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MsgLevelService {
    private static final Logger logger = LoggerFactory.getLogger(MsgLevelService.class);

    @Autowired
    MsgLevelMapper msgLevelMapper;
    @Autowired
    MsgSend msgSend;

    public Integer getMaxLevel(){
        return msgLevelMapper.getMaxLevel();
    }

    public MsgLevel selectMsgLevelByLevel(String levelCode,Integer level){
        return msgLevelMapper.selectMsgLevelByLevel(levelCode,level);
    }

    public Integer selectLevelByID(Integer level,Integer id){
        return msgLevelMapper.selectLevelByID(level,id);
    }

    public MsgLevel selectMsgLevel(Integer id){
        return msgLevelMapper.selectMsgLevel(id);
    }

    public List<MsgLevel> selectMsgLevelAll(){
        return msgLevelMapper.selectMsgLevelAll();
    }

    public List<MsgLevel> selectMsgLevelByCode(String levelCode){
        return msgLevelMapper.selectMsgLevelByCode(levelCode);
    }

    public PageUtil selectPaging(Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(msgLevelMapper.count());
        pageUtil.setPageData(msgLevelMapper.selectPaging(pageNum,pageSize));
        return pageUtil;
    }


    @Transactional
    public Integer addMsgLevel(MsgLevel msgLevel){
        try {
           /* Integer level=msgLevelMapper.getMaxLevel();
            if(level==null){
                level=1;
            }else{
                level+=1;
            }
            msgLevel.setLevel(level);
            msgLevel.setLevelCode("test");
            msgLevel.setTitle("test");
            msgLevel.setNote("test");*/
            Integer i=msgLevelMapper.addMsgLevel(msgLevel);
            if(i>0){
                sendLevel();
            }
            return i;
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateMsgLevel(MsgLevel msgLevel){
        try{
            Integer i=msgLevelMapper.updateMsgLevel(msgLevel);
            if(i>0){
                sendLevel();
            }
            return i;
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteMsgLevel(Integer id){
        try{
            Integer i=msgLevelMapper.deleteMsgLevel(id);
            if(i>0){
                sendLevel();
            }
            return i;
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    public void sendLevel(){
        List<MsgLevel> levels=msgLevelMapper.selectMsgLevelAll();
        StringBuffer buffer=new StringBuffer();
        String content="MSLV:<MSG>\r\n" +
                "<MsgLevel>\r\n" +
                "</MsgLevel>\r\n" +
                "</MSG>";
        buffer.append(content);
        if(levels!=null){
            StringBuffer stringBuffer=new StringBuffer();
            for (MsgLevel msgLevel1:levels){
                String param= "<LevelItem>\r\n" +
                        "<Level>"+msgLevel1.getLevel()+"</Level>\r\n" +
                        "<LevelCode>"+msgLevel1.getLevelCode()+"</LevelCode>\r\n" +
                        "<IsFull>"+msgLevel1.getNote()+"</IsFull>\r\n" +
                        "</LevelItem>";
                stringBuffer.append(param);
            }
            buffer.insert(buffer.indexOf("<MsgLevel>")+10,stringBuffer);
        }else{
            buffer.insert(buffer.indexOf("<MsgLevel>")+10,"<LevelItem/>");
        }
        List<Map<String,String>> list= AmqpConfig.deviceList;
        for (Map<String,String> stringMap:list){
            for (String key:stringMap.keySet()){
                msgSend.sendMsg("pisplayer.*."+ IPUtil.ipToLong(key),buffer.toString());
            }
        }

    }
}
