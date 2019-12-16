package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.EventStateVo;
import com.example.passenger.entity.vo.MapEntityVo;
import com.example.passenger.service.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/map")
public class MapController {
    @Autowired
    OperationLogService operationLogService;
    @Autowired
    StationService stationService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    LineService lineService;
    @Autowired
    MapService mapService;

    @RequestMapping("/lineMapManagement")
    public String mapManagement(Model model,Integer id){
        List<Station> stationList=stationService.selectAllStation(id);
        model.addAttribute("id",id);
        model.addAttribute("line",null);
        model.addAttribute("menuList",stationList);
        return "map";
    }

    @RequestMapping("/stationMapManagement")
    public String stationMapManagement(Model model,Integer id){
        Station station=stationService.selectStation(id);
        Line line=lineService.selectLine(station.getLineID());
        List<Device> deviceList=deviceService.selectAllDevice(id);
        model.addAttribute("id",id);
        model.addAttribute("line",line);
        model.addAttribute("menuList",deviceList);
        return "map";
    }

    @RequestMapping("/addMap")
    @ResponseBody
    public Map<String,Object> addMap(ModelAndView mv,HttpSession session,String id,String  list){
        Users users=(Users) session.getAttribute("user");
        try {
            MapEntity mapEntity=mapService.selectMapByID(id);
            //将字符串转换成json
            JSONArray jArray= JSONArray.fromObject(list);
            List<Map<String,String>> mapList = jArray;

            List<String> stringList=new ArrayList<>();
            StringBuffer buffer=new StringBuffer();
            for (Map<String,String> map:mapList){
                for(String s:map.keySet()){
                    String parameter=null;
                    if(s.equals("top")){
                        parameter="<"+s+">"+map.get(s)+"</"+s+"><end>";
                    }else{
                        parameter="<"+s+">"+map.get(s)+"</"+s+">";
                    }
                    buffer.append(parameter);
                }
            }
            stringList.add(buffer.toString());
            Integer i=0;
            if(mapEntity!=null){
                i=mapService.updateMap(id,String.join("",stringList));
                if (i>0){
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }else{
                i=mapService.addMap(id,String.join("",stringList));
                if (i>0){
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }
            //日志记录
            OperationLog operationLog=new OperationLog();
            operationLog.setOperator(users.getId().toString());
            operationLog.setType("系统配置管理");
            operationLog.setContent("用户("+users.getName()+") 绘制地图!");
            operationLogService.addOperationLog(operationLog);
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/selectMap")
    @ResponseBody
    public Map<String,Object> selectMap(HttpSession session, ModelAndView mv, String id){
        List<EventStateVo> eventStateVoList=(ArrayList <EventStateVo>)session.getAttribute("deviceState");
        mv.addObject("deviceState",eventStateVoList);
        MapEntity mapEntity=mapService.selectMapByID(id);
        List<MapEntityVo> list=new ArrayList<>();
        if(mapEntity!=null){
            String[] array = mapEntity.getPath().split("<end>");
            for (int i=0;i<array.length;i++){
                MapEntityVo mapEntityVo=new MapEntityVo();
                mapEntityVo.setMapImg(array[i].substring(array[i].indexOf("<mapImg>")+8,
                        array[i].indexOf("</mapImg>")));
                mapEntityVo.setDivTop(array[i].substring(array[i].indexOf("<divTop>")+8,
                        array[i].indexOf("</divTop>")));
                mapEntityVo.setDivLef(array[i].substring(array[i].indexOf("<divLeft>")+9,
                        array[i].indexOf("</divLeft>")));
                mapEntityVo.setEquipmentID(array[i].substring(array[i].indexOf("<stationID>")+11,
                        array[i].indexOf("</stationID>")));
                mapEntityVo.setEquipmentName(array[i].substring(array[i].indexOf("<stationName>")+13,
                        array[i].indexOf("</stationName>")));
                mapEntityVo.setLeft(array[i].substring(array[i].indexOf("<left>")+6,
                        array[i].indexOf("</left>")));
                mapEntityVo.setTop(array[i].substring(array[i].indexOf("<top>")+5,
                        array[i].indexOf("</top>")));
                list.add(mapEntityVo);
            }
        }
        mv.addObject("list",list);
        return mv.getModel();
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Map<String,Object> upload(ModelAndView mv, @RequestParam(value="file",required=false)MultipartFile[] multipartFile,
                                     @RequestParam(defaultValue = "0") String type) throws IOException {
        for (int i =0;i<multipartFile.length;i++) {
            if (multipartFile[i] != null) {
                //生成新文件名，防止文件名重复而导致文件覆盖
                //1、获取原文件后缀名 .img .jpg ....
                String originalFileName = multipartFile[i].getOriginalFilename();
                String suffix = originalFileName.substring(originalFileName.lastIndexOf('.'));
                //2、使用UUID生成新文件名
                String newFileName = UUID.randomUUID() + suffix;
                String uploadPath="D:\\ftp\\stationMapImage";
                if(type.equals("1")){
                    uploadPath="D:\\ftp\\deviceMapImage";
                }
                File localFile = new File(uploadPath,newFileName);
                if(!localFile.getParentFile().exists()){ //判断文件父目录是否存在
                    localFile.getParentFile().mkdir();
                }
                try {
                    multipartFile[i].transferTo(localFile);
                    mv.addObject("result","success");
                }catch (Exception e){
                    mv.addObject("result","error");
                }
            }
        }
        return mv.getModel();
    }

    @RequestMapping("/getMapImage")
    @ResponseBody
    public Map<String,Object> getMapImage(ModelAndView mv,@RequestParam(defaultValue = "0") Integer type){
        List<String> mapList=new ArrayList<>();
        String path="D:\\ftp\\stationMapImage";
        if(type==1){
            path="D:\\ftp\\deviceMapImage";
        }
        File file = new File(path);
        if(file.exists()) {
            File[] files = file.listFiles();
            if (files.length != 0) {
                for (int i = 0; i < files.length; i++){
                    mapList.add(files[i].getName());
                }
            }
        }
        mv.addObject("mapList",mapList);
        return mv.getModel();
    }
}
