package com.example.passenger.service;

import com.example.passenger.config.AmqpConfig;
import com.example.passenger.entity.PlayStyle;
import com.example.passenger.mapper.PlayListStyleMapper;
import com.example.passenger.mapper.PlayStyleMapper;
import com.example.passenger.mapper.StyleContentMapper;
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
public class PlayStyleService {
    private static final Logger logger = LoggerFactory.getLogger(PlayStyleService.class);

    @Autowired
    PlayStyleMapper playStyleMapper;

    @Autowired
    PlayListStyleMapper playListStyleMapper;

    @Autowired
    StyleContentMapper styleContentMapper;

    @Autowired
    MsgSend msgSend;

    public PlayStyle selectPlayStyle(Integer id){
        return playStyleMapper.selectPlayStyle(id);
    }

    public PageUtil selectPaging(Integer state,String name,Integer isTemplate,Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playStyleMapper.count(state,name,isTemplate));
        pageUtil.setPageData(playStyleMapper.selectPaging(state,name,isTemplate,pageNum,pageSize));
        return pageUtil;
    }

   /* @Scheduled(cron = "0/1 * * * * ?")
    public void getStyleContent(){
        List<String> list=AmqpConfig.selectList;
        if (list!=null){
            for (int i=0;i<list.size();i++){
                //截取操作sql
                String sql=list.get(i).substring(list.get(i).indexOf("<SQL>")+5,
                        list.get(i).indexOf("</SQL>"));
                //截取rabbitMQ路由关键字
                String routingKey=list.get(i).substring(list.get(i).indexOf("<Key>")+5,
                        list.get(i).indexOf("</Key>"));
                //截取操作对象
                String typeName=list.get(i).substring(list.get(i).indexOf("<TableName>")+11,
                        list.get(i).indexOf("</TableName>"));
                //初始化类容对象
                String content=null;
                //初始化消息主体
                String msgBody="SQLR:<MSG>\r\n" +
                        "<Type>10</Type>\r\n" +
                        "<Info>\r\n" +
                        "<SQLType>0</SQLType>\r\n" +
                        "<SQLResult>1</SQLResult>\r\n" +
                        "<DataResult>1</DataResult>\r\n" +
                        "<TableName>"+typeName+"</TableName>\r\n" +
                        "<Content>" +
                        "</Info>\r\n" +
                        "</MSG>";
                try {
                    //执行查询操作
                    List<Map<String,String>> mapList=playStyleMapper.selectContent(sql);
                    StringBuffer stringBuffer=new StringBuffer();
                    if(mapList!=null){
                        for (Map<String,String> stringMap :mapList) {
                            StringBuffer buffer=new StringBuffer();
                            for (String key:stringMap.keySet()){
                                String tableInfo="<"+key+">"+String.valueOf(stringMap.get(key))+"</"+key+">\r\n";
                                *//*if(String.valueOf(stringMap.get(key)).equals("")){
                                    tableInfo="<"+key+"/>\r\n";
                                }*//*
                                if(String.valueOf(stringMap.get(key))==null){
                                    tableInfo="<"+key+"/>\r\n";
                                }
                                if (key.equals("ContentText")){
                                    String numberOne=stringMap.get(key).replace(">","&gt;");
                                    String numberTwo=numberOne.replace("<","&lt;");
                                    //String numberthree=numberTwo.replace("","");
                                    tableInfo="<"+key+">"+numberTwo+"</"+key+">\r\n";
                                }
                                buffer.append(tableInfo);
                            }
                            stringBuffer.append("<TableInfo>\r\n"+buffer+"</TableInfo>\r\n");
                        }
                    }
                    if(stringBuffer.length()!=0){
                        content=msgBody.replace("<Content>","<DataList>\r\n"+
                                stringBuffer+"</DataList>\r\n");
                    }else{
                        content=msgBody.replace("<Content>",stringBuffer);
                    }
                    System.out.println("输出"+content);

                    //消息下发
                    msgSend.sendMsg(routingKey,content);
                    list.remove(i);
                }catch (Exception e){
                    System.out.println(e);
                    list.remove(i);
                }

            }
        }
    }*/

    @Scheduled(cron = "0/3 * * * * ?")
    public void addStyleList(){
        List<String> addList= AmqpConfig.insertStyleList;
        if(addList!=null){
            for (int i=0;i<addList.size();i++){
                String content=addList.get(i);
                if(addList.get(i).indexOf("&lt;")!=-1){
                    String contentOne=addList.get(i).replace("&lt;","<");
                    content=contentOne.replace("&gt;",">");
                }
                Integer count=playStyleMapper.comAddPlayStyle(content);
                if(count>0){
                    System.out.println("执行成功:"+"结果:"+count+":"+addList.get(i));
                }else{
                    logger.error("执行失败"+"结果:"+count+":"+addList.get(i));
                }
                addList.remove(i);
            }
        }
    }

    @Transactional
    public Integer addPlayStyle(PlayStyle playStyle){
        try {
            return playStyleMapper.addPlayStyle(playStyle);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updatePlayStyle(PlayStyle playStyle){
        try {
            return playStyleMapper.updatePlayStyle(playStyle);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updatePlayStyleContent(Integer id,String content){
        try {
            return playStyleMapper.updatePlayStyleContent(id,content);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deletePlayStyle(Integer id){
        try {
            Integer count=0;
            Integer i=playStyleMapper.deletePlayStyle(id);
            if(i>0){
                Integer j=styleContentMapper.deleteStyleContentByStyleID(id);
                if(j>0){
                    count=playListStyleMapper.deleteByStyleID(id);
                }
            }
            return count;
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}
