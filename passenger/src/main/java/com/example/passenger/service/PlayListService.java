package com.example.passenger.service;

import com.example.passenger.entity.PlayList;
import com.example.passenger.entity.PlayListStyle;
import com.example.passenger.entity.PlayStyle;
import com.example.passenger.entity.Users;
import com.example.passenger.mapper.PlayListMapper;
import com.example.passenger.mapper.PlayListStyleMapper;
import com.example.passenger.mapper.PlayStyleMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class PlayListService {
    private static final Logger logger = LoggerFactory.getLogger(PlayListService.class);

    @Autowired
    PlayListMapper playListMapper;

    @Autowired
    PlayStyleMapper playStyleMapper;

    @Autowired
    PlayListStyleMapper playListStyleMapper;

    public PlayList selectPlayList(Integer id){
        return playListMapper.selectPlayList(id);
    }

    public PageUtil selectPaging(Integer state,String timeLength,Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playListMapper.count(state,timeLength));
        pageUtil.setPageData(playListMapper.selectPaging(state,timeLength,pageNum,pageSize));
        return pageUtil;
    }

    public Integer auditPlayList(PlayList playList){
        try {
            return playListMapper.auditPlayList(playList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer addPlayList(PlayList playList,Users users){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String description=playList.getDescription();

            //日期转换
            DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time=LocalDateTime.parse(playList.getStartDate());
            playList.setStartDate(df.format(time));
            //日期转换
            LocalDateTime time1=LocalDateTime.parse(playList.getEndDate());
            playList.setEndDate(df.format(time1));

            playList.setState(0);
            playList.setUpdateTime(sdf.format(new Date()));
            playList.setLevel("0");
            playList.setEditorID(users.getId());
            playList.setEditTime(sdf.format(new Date()));
            playList.setScopeEditTime("");
            playList.setAuditLevel(0);
            playList.setIssync(0);
            playList.setDescription("");
            Integer playListID=playListMapper.selectPlayListID();
            playListID+=1;
            playList.setContentText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<root>\r\n" +
                    "<schedule>\r\n" +
                    "<id>"+playListID+"</id>\r\n" +
                    "<type>"+playList.getType()+"</type>\r\n" +
                    "<summary>"+playList.getSummary()+"</summary>\r\n" +
                    "<color>0xFF0000</color>\r\n" +
                    "<description>"+description+"</description>\r\n" +
                    "<modifydt>"+sdf.format(new Date())+"</modifydt>\r\n" +
                    "<startcron/>\r\n" +
                    "<endcron/>\r\n" +
                    "<cronexpression/>\r\n" +
                    "<executeexpression/>\r\n" +
                    "<allday>0</allday>\r\n" +
                    "<timelength/>\r\n" +
                    "<start>"+playList.getStartDate()+"</start>\r\n" +
                    "<end>"+playList.getEndDate()+"</end>\r\n" +
                    "<oldStart>"+playList.getStartDate()+"</oldStart>\r\n" +
                    "<oldEnd>"+playList.getEndDate()+"</oldEnd>\r\n" +
                    "</schedule>\r\n" +
                    "</root>");
            return playListMapper.addPlayList(playList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer addMultimedia(PlayList playList, Users users,String width,String height){
        try {
            //创建对象
            PlayStyle playStyle=new PlayStyle();
            PlayListStyle playListStyle=new PlayListStyle();

            //set参数
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            playList.setState(0);
            playList.setTimeLength("0");
            playList.setType(7);
            playList.setUpdateTime(sdf.format(new Date()));
            playList.setEditorID(users.getId());
            playList.setEditTime(sdf.format(new Date()));
            playList.setAuditLevel(0);
            playList.setIssync(0);
            playList.setStartDate("");
            playList.setEndDate("");
            playList.setScopeEditTime(" ");
            Integer playListID=playListMapper.selectPlayListID();
            playListID+=1;
            playList.setContentText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<root>\r\n" +
                    "<schedule>\r\n" +
                    "<id>"+playListID+"</id>\r\n" +
                    "<type>7</type>\r\n" +
                    "<summary/>\r\n" +
                    "<color>0xFF0000</color>\r\n" +
                    "<description>"+playList.getNote()+"</description>\r\n" +
                    "<modifydt/>\r\n" +
                    "<startcron/>\r\n" +
                    "<endcron/>\r\n" +
                    "<cronexpression/>\r\n" +
                    "<executeexpression/>\r\n" +
                    "<allday>0</allday>\r\n" +
                    "<timelength>0</timelength>\r\n" +
                    "<start>"+sdf.format(new Date())+"</start>\r\n" +
                    "<end>2099-12-30 23:00:00</end>\r\n" +
                    "<oldStart>"+sdf.format(new Date())+"</oldStart>\r\n" +
                    "<oldEnd>2099-12-30 23:00:00</oldEnd>\r\n" +
                    "</schedule>\r\n" +
                    "</root>\r\n");
            Integer i=playListMapper.addPlayList(playList);
            if (i>0){
                playStyle.setName(playList.getName());
                playStyle.setEditorID(users.getName());
                playStyle.setState(0);
                playStyle.setPath("");
                playStyle.setIsTemplate(2);
                playStyle.setEditTime(sdf.format(new Date()));
                Integer playStyleID=playStyleMapper.selectPlayStyleID();
                playStyleID+=1;
                playStyle.setContentText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<root>\r\n" +
                        "<programmes>\r\n" +
                        "<id>"+playStyleID+"</id>\r\n" +
                        "<thumbnailimage/>\r\n" +
                        "<title>"+playList.getName()+"</title>\r\n" +
                        "<width>"+width+"</width>\r\n" +
                        "<height>"+height+"</height>\r\n" +
                        "<fullimage/>\r\n" +
                        "<starttime/>\r\n" +
                        "<endtime/>\r\n" +
                        "<bgcolor>0xFFFFFF</bgcolor>\r\n" +
                        "<layout>\r\n" +
                        "<id>1</id>\r\n" +
                        "<layoutname>"+playList.getName()+"</layoutname>\r\n" +
                        "<width>"+width+"</width>\r\n" +
                        "<height>"+height+"</height>\r\n" +
                        "<x>0</x>\r\n" +
                        "<y>0</y>\r\n" +
                        "<type>"+playList.getDescription()+"</type>\r\n" +
                        "</layout>\r\n" +
                        "</programmes>\r\n" +
                        "</root>\r\n");
                Integer j= playStyleMapper.addPlayStyle(playStyle);
                if(j>0){
                    playListStyle.setPlaylistID(playListMapper.selectPlayListID());
                    playListStyle.setStyleID(playStyleMapper.selectPlayStyleID());
                    playListStyle.setStyleName(playStyle.getName());
                    playListStyle.setStylePath("");
                    playListStyle.setSequence(1);
                    playListStyle.setPlayTimes(1);
                    playListStyle.setPlayType("23:59:59");
                    return playListStyleMapper.addPlayListStyle(playListStyle);
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
        return -1;
    }

    @Transactional
    public Integer updatePlayList(PlayList playList){
        try {
            //定义日期格式
            DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if(playList.getStartDate()!=null){
                //日期转换
                LocalDateTime time=LocalDateTime.parse(playList.getStartDate());
                playList.setStartDate(df.format(time));
            }
           if(playList.getEndDate()!=null){
               //日期转换
               LocalDateTime time1=LocalDateTime.parse(playList.getEndDate());
               playList.setEndDate(df.format(time1));
           }
            return playListMapper.updatePlayList(playList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deletePlayList(Integer id){
        try {
            Integer count=0;
            Integer i=playListMapper.deletePlayList(id);
            if(i>0){
                count=playListStyleMapper.deleteByPlayListID(id);
            }
           return count;
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}
