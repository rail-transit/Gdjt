package com.example.passenger.utils;

import com.example.passenger.config.AmqpConfig;
import com.example.passenger.entity.Device;
import com.example.passenger.entity.MsgLevel;
import com.example.passenger.entity.PlayListClient;
import com.example.passenger.entity.PlayListDownloadStatus;
import com.example.passenger.mapper.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MsgReceiver {
    @Autowired
    PlayListDownloadStatusMapper playListDownloadStatusMapper;
    @Autowired
    PlayListClientMapper playListClientMapper;
    @Autowired
    StyleContentMapper styleContentMapper;
    @Autowired
    PlayStyleMapper playStyleMapper;
    @Autowired
    MsgLevelMapper msgLevelMapper;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    MsgSend msgSend;

    @RabbitHandler
    @RabbitListener(queues = AmqpConfig.QUEUEB)
    public void process(Message message) {
        byte[] body = message.getBody();
        String msg=new String(body);
        if (msg.substring(0,5).equals("SQLS:")){
            //截取sql类型1:操作,0:查询
            String sqlType=msg.substring(msg.indexOf("<SQLType>")+9,msg.indexOf("</SQLType>"));
            //截取操作对象
            String typeName=msg.substring(msg.indexOf("<TableName>")+11,msg.indexOf("</TableName>"));
            //截取操作sql
            String sql=msg.substring(msg.indexOf("<SQL>")+5,msg.indexOf("</SQL>"));
            //截取rabbitMQ路由关键字
            String routingKey=msg.substring(msg.indexOf("<Key>")+5,
                    msg.indexOf("</Key>"));
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
            //判断是sql类型
            if(sqlType.equals("0")){
                try {
                    //执行查询操作
                    List<Map<String,String>> mapList=playStyleMapper.selectContent(new String(sql.getBytes(),"utf-8"));
                    StringBuffer stringBuffer=new StringBuffer();
                    if(mapList!=null){
                        for (Map<String,String> stringMap :mapList) {
                            StringBuffer buffer=new StringBuffer();
                            for (String key:stringMap.keySet()){
                                String tableInfo=null;
                                tableInfo ="<"+key+">"+String.valueOf(stringMap.get(key))+"</"+key+">\r\n";

                                if(String.valueOf(stringMap.get(key))==null){
                                    tableInfo="<"+key+"/>\r\n";
                                }
                                if (key.equals("ContentText")||key.equals("getcontents")){
                                    String numberOne=stringMap.get(key).replace(">","&gt;");
                                    String numberTwo=numberOne.replace("<","&lt;");
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
                        content=msgBody.replace("<Content>","<DataList/>");
                    }
                    System.out.println("查询成功:"+typeName);
                    //结果返回
                    msgSend.sendMsg(routingKey,content);
                }catch (Exception e){
                    System.out.println("查询失败");
                    String errorContent="SQLR:<MSG>\r\n" +
                            "<Type>10</Type>\r\n" +
                            "<Info>\r\n" +
                            "<SQLType>0</SQLType>\r\n" +
                            "<SQLResult>0</SQLResult>\r\n" +
                            "<DataResult>0</DataResult>\r\n" +
                            "</Info>\r\n" +
                            "</MSG>";
                    //结果返回
                    msgSend.sendMsg(routingKey,errorContent);
                }
            }else if(sqlType.equals("1")){
                try {
                    if(sql.indexOf("&lt;")>-1){
                        String contentOne=sql.replace("&lt;","<");
                        content=contentOne.replace("&gt;",">");
                    }else{
                        content=sql;
                    }
                    Integer count=playStyleMapper.comAddPlayStyle(new String(content.getBytes(),"utf-8"));
                    if(count>0){
                        String sendContent="SQLR:<MSG>" +
                                "<Type>10</Type>" +
                                "<Info>" +
                                "<SQLType>1</SQLType>" +
                                "<SQLResult>1</SQLResult>" +
                                "<DataResult>1</DataResult>" +
                                "<TableName>"+typeName+"</TableName>" +
                                "</Info>" +
                                "</MSG>";
                        //结果返回
                        msgSend.sendMsg(routingKey,sendContent);
                    }
                }catch (Exception e){
                    String sendContent="SQLR:<MSG>" +
                            "<Type>10</Type>" +
                            "<Info>" +
                            "<SQLType>1</SQLType>" +
                            "<SQLResult>0</SQLResult>" +
                            "<DataResult>0</DataResult>" +
                            "<TableName>"+typeName+"</TableName>" +
                            "</Info>" +
                            "</MSG>";
                    //结果返回
                    msgSend.sendMsg(routingKey,sendContent);
                }
            }
        }

        if(msg.substring(0,5).equals("ONLI:")){
            String routingKey=msg.substring(5,14);
            List<MsgLevel> levels=msgLevelMapper.selectMsgLevelAll();
            StringBuffer buffer=new StringBuffer();
            String content="MSLV:<MSG>\r\n" +
                    "<MsgLevel>\r\n" +
                    "</MsgLevel>\r\n" +
                    "</MSG>";
            buffer.append(content);
            if(levels!=null){
                StringBuffer stringBuffer=new StringBuffer();
                for (MsgLevel msgLevel:levels){
                    String param= "<LevelItem>\r\n" +
                            "<Level>"+msgLevel.getLevel()+"</Level>\r\n" +
                            "<LevelCode>"+msgLevel.getLevelCode()+"</LevelCode>\r\n" +
                            "<IsFull>"+msgLevel.getNote()+"</IsFull>\r\n" +
                            "</LevelItem>";
                    stringBuffer.append(param);
                }
                buffer.insert(buffer.indexOf("<MsgLevel>")+10,stringBuffer);
            }else{
                buffer.insert(buffer.indexOf("<MsgLevel>")+10,"<LevelItem/>");
            }
            msgSend.sendMsg("pisplayer.occ."+routingKey,buffer.toString());
        }

        if(msg.substring(0,5).equals("PRGS:")||msg.substring(0,5).equals("FTPS:")){
            String content=msg.substring(msg.lastIndexOf(':')+1);
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
                }else{
                    playListDownloadStatus.setPercen("");
                    playListDownloadStatus.setSpeed("");
                }
                count=playListDownloadStatusMapper.selectDownload(arr[0],device.getId().toString(),arr[2]);
            }
            if (count!=null){
                System.out.println("执行修改");
                playListDownloadStatus.setId(count);
                playListDownloadStatusMapper.updateDownload(playListDownloadStatus);
            }else{
                System.out.println("执行添加");
                playListDownloadStatusMapper.addDownload(playListDownloadStatus);
            }
        }

        if(msg.substring(0,5).equals("SCSV:")){
            String deviceIP= IPUtil.longToIP(Long.valueOf(msg.substring(5,
                    msg.indexOf(";"))));
            String playID= msg.substring(msg.lastIndexOf(';')+1);
            System.out.println("修改状态:"+msg);
            //获取设备ID
            Device device=deviceMapper.selectDeviceByIp(deviceIP);
            //获取ClientID
            Integer clientID=playListClientMapper.selectClientByPlayID(device.getId(),Integer.parseInt(playID));
            //修改状态
            PlayListClient playListClient1=new PlayListClient();
            playListClient1.setId(clientID);
            playListClient1.setSequence(1);
            playListClientMapper.updatePlayListClient(playListClient1);
        }
    }
}
