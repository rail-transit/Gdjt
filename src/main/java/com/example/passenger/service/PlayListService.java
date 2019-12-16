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

    public Integer selectCountByName(String name,Integer type,Integer id){
        return playListMapper.selectCountByName(name,type,id);
    }

    public PageUtil selectPaging(Integer state,Integer type,Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playListMapper.count(state,type));
        pageUtil.setPageData(playListMapper.selectPaging(state,type,pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer auditPlayList(PlayList playList){
        try {
            return playListMapper.auditPlayList(playList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updatePlayListByID(Integer id,Integer state,String endDate){
        try {
            return playListMapper.updatePlayListByID(id,state,endDate);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer addPlayList(PlayList playList,Users users,String startWeek,String endWeek){
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
            if(playListID==null){
                playListID=1;
            }else{
                playListID+=1;
            }

            String expression="";
            if (playList.getType()==3){
                String startYear=playList.getStartDate().substring(0,4);
                String endYear=playList.getEndDate().substring(0,4);
                String startMonth=playList.getStartDate().substring(5,7);
                String endMonth=playList.getEndDate().substring(5,7);
                expression="<executeexpression>0 0 0 * "+startMonth+" "+startWeek+"-"+endWeek+" "+startYear+"-"+endYear+"</executeexpression>\r\n";
            }else{
                expression="<executeexpression/>";
            }
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
                     expression +
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
            playList.setStartDate(sdf.format(new Date()));
            playList.setEndDate("");
            playList.setScopeEditTime(" ");
            Integer playListID=playListMapper.selectPlayListID();
            if(playListID==null){
                playListID=1;
            }else{
                playListID+=1;
            }
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
                playStyle.setEditTime(sdf.format(new Date()));
                playStyle.setState(0);
                playStyle.setPath("");
                playStyle.setIsTemplate(2);
                Integer playStyleID=playStyleMapper.selectPlayStyleID();
                if(playStyleID==null){
                    playStyleID=1;
                }else{
                    playStyleID+=1;
                }
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
    public Integer updatePlayList(PlayList playList,String startWeek,String endWeek){
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
           if(playList.getType()!=null){
               if (playList.getType()==3){
                   String startYear=playList.getStartDate().substring(0,4);
                   String endYear=playList.getEndDate().substring(0,4);
                   String startMonth=playList.getStartDate().substring(5,7);
                   String endMonth=playList.getEndDate().substring(5,7);
                   String expression="<executeexpression>0 0 0 * "+startMonth+" "+startWeek+"-"+endWeek+" "+startYear+"-"+endYear+"</executeexpression>\r\n";
                   PlayList playList1=playListMapper.selectPlayList(playList.getId());
                   StringBuffer stringBuffer=new StringBuffer(playList1.getContentText());
                   stringBuffer.replace(stringBuffer.indexOf("<cronexpression/>")+17,
                           stringBuffer.indexOf("<allday>"),expression);
                   playList.setContentText(stringBuffer.toString());
               }
           }
            return playListMapper.updatePlayList(playList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deletePlayList(PlayList playList){
        try {
            Integer i=playListMapper.deletePlayList(playList);
            if(i>0){
                playListStyleMapper.deleteByPlayListID(playList.getId());
            }
           return i;
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}
