package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.StyleContentVo;
import com.example.passenger.service.*;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/playList")
@Controller
public class PlayListController {

    @Autowired
    PlayListService playListService;
    @Autowired
    MsgLevelService msgLevelService;
    @Autowired
    FodderService fodderService;
    @Autowired
    PlayStyleService playStyleService;
    @Autowired
    PlayListClientService playListClientService;
    @Autowired
    PlayListStyleService playListStyleService;
    @Autowired
    PlayListDownloadStatusService playListDownloadStatusService;
    @Autowired
    UserService userService;
    @Autowired
    StyleContentService styleContentService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/planManagement")
    public String planManagement(Model model){
        List<MsgLevel> msgLevelList=msgLevelService.selectMsgLevelAll();
        model.addAttribute("msgLevelList",msgLevelList);
        return "rightContent/messagePlan/plan";
    }


    @RequestMapping("/playReleaseManagement")
    public String playReleaseManagement(Model model){
        List<MsgLevel> msgLevelList=msgLevelService.selectMsgLevelAll();
        model.addAttribute("msgLevelList",msgLevelList);
        return "rightContent/messagePlan/playRelease";
    }

    @RequestMapping("/playState")
    public String playState(){
        return "rightContent/messagePlan/playState";
    }

    @RequestMapping("/playListManagement")
    public String playListManagement(){
        return "rightContent/playStyle/playList";
    }


    @RequestMapping("/releasePlayList")
    public String releasePlayList(Model model){
        return "rightContent/playStyle/releasePlayList";
    }

    @RequestMapping("/selectPlayList")
    @ResponseBody
    public Map<String,Object> selectPlayList(ModelAndView mv,HttpSession session,Integer type,
                                             @RequestParam(defaultValue = "1") Integer pageNum){
        List<Rights> rightsList=(List<Rights>) session.getAttribute("rightsList");
        PageUtil pageUtil=playListService.selectPaging(null,type,pageNum,15);
        mv.addObject("rightsList",rightsList);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }


    @RequestMapping("/getPlayList")
    @ResponseBody
    public Map<String,Object> getPlayList(ModelAndView mv,Integer id,
                                          @RequestParam(defaultValue = "0") Integer state){
        PlayList playList=playListService.selectPlayList(id);
        Users users=userService.findUserById(playList.getEditorID().toString());
        if(state==1){
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime startDateTime = LocalDateTime.parse(playList.getStartDate(), df);
            LocalDateTime endDateTime = LocalDateTime.parse(playList.getEndDate(), df);
            playList.setStartDate(startDateTime.toString());
            playList.setEndDate(endDateTime.toString());
        }
        mv.addObject("playList",playList);
        mv.addObject("users",users);
        return mv.getModel();
    }

    @RequestMapping("/getReleasePlayList")
    @ResponseBody
    public Map<String,Object> getReleasePlayList(ModelAndView mv,@RequestParam(defaultValue = "1")
            Integer pageNum){
        PageUtil pageUtil=playListService.selectPaging(1,1,pageNum,20);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/addPlayList")
    @ResponseBody
    public Map<String,Object> addPlayList(HttpSession session,ModelAndView mv,PlayList playList,
                                          String startWeek,String endWeek){
        Users users=(Users)session.getAttribute("user");
        try {
            Integer count=playListService.selectCountByName(playList.getName(),1,
                    playList.getId());
            if(count>0){
                mv.addObject("result","exit");
            }else{
                Integer i=playListService.addPlayList(playList,users,startWeek,endWeek);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("版式管理");
                    operationLog.setContent("用户("+users.getName()+") 新建播表("+playList.getName()+")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/addPlan")
    @ResponseBody
    public Map<String,Object> addPlan(HttpSession session, ModelAndView mv, PlayList playList,
                                      String width,String height){
        Users users=(Users)session.getAttribute("user");
        try {
            Integer count=playListService.selectCountByName(playList.getName(),7,
                    playList.getId());
            if(count>0){
                mv.addObject("result","exit");
            }else{
                //执行操作
                Integer i=playListService.addMultimedia(playList,users,width,height);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("预案管理");
                    operationLog.setContent("用户("+users.getName()+") 新建预案("+playList.getName()+")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    @RequestMapping("/updatePlay")
    @ResponseBody
    public Map<String,Object> updatePlay(ModelAndView mv,HttpSession session,PlayList playList,
                                         Integer width,Integer height){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Users user=(Users) session.getAttribute("user");
        try {
            Integer count=playListService.selectCountByName(playList.getName(),7,
                    playList.getId());
            if(count>0){
                mv.addObject("result","exit");
            }else{
                PlayListStyle playListStyle=playListStyleService.selectPlayListStyle(playList.getId());
                PlayStyle playStyle=playStyleService.selectPlayStyle(playListStyle.getStyleID());
                PlayList playList1=playListService.selectPlayList(playList.getId());
                if(playListStyle!=null){
                    if(playStyle!=null){
                        if(!playList.getDescription().equals(playList1.getDescription())){
                            List<StyleContentVo> styleContentVoList=styleContentService.selectStyleContentVo(playStyle.getId());
                            if(styleContentVoList.size()>0){
                                mv.addObject("result","not");
                                return mv.getModel();
                            }
                        }
                        playList.setEditorID(user.getId());
                        playList.setEditTime(sdf.format(new Date()));
                        Integer i=playListService.updatePlayList(playList,null,null);
                        if(i>0){
                            playListService.updatePlayListByID(playList.getId(),0,null);
                            //修改分辨率
                            if(playListStyle!=null){
                                if(playStyle!=null){
                                    String contentOne=playStyle.getContentText().substring(
                                            playStyle.getContentText().indexOf("<width>"),playStyle.getContentText().indexOf("<fullimage"));
                                    String contentTwo="<width>"+width+"</width>\n" +
                                            "<height>"+height+"</height>";
                                    String contentNum=playStyle.getContentText().replace(contentOne,contentTwo);

                                    String contentThree=contentNum.substring( contentNum.indexOf("</layoutname>")+13,
                                            contentNum.indexOf("<x"));
                                    String content=contentNum.replace(contentThree,contentTwo);
                                    playStyleService.updatePlayStyleContent(playStyle.getId(),content);
                                }
                            }
                            //日志记录
                            OperationLog operationLog=new OperationLog();
                            operationLog.setOperator(user.getId().toString());
                            operationLog.setType("预案管理");
                            operationLog.setContent("用户("+user.getName()+") 修改预案("+playList.getName()+")!");
                            operationLogService.addOperationLog(operationLog);
                            mv.addObject("result","success");
                        }else{
                            mv.addObject("result","error");
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/updatePlayListStyleTime")
    @ResponseBody
    public Map<String,Object> updatePlayListStyleTime(ModelAndView mv,Integer id,String time){
        Integer i=playListStyleService.updateTime(id,time);
        if(i>0){
            mv.addObject("result","success");
        }else{
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/auditPlayList")
    @ResponseBody
    public Map<String,Object> auditPlayList(ModelAndView mv,PlayList playList,HttpSession session){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Users users=(Users) session.getAttribute("user");
        try {
            playList.setEditorID(users.getId());
            playList.setEditTime(sdf.format(new Date()));
            Integer i=playListService.auditPlayList(playList);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                PlayList playList1=playListService.selectPlayList(playList.getId());
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("版式管理");
                if(playList1.getType()==7){
                    operationLog.setContent("用户("+users.getName()+") 审核预案("+playList1.getName()+")!");
                }else{
                    operationLog.setContent("用户("+users.getName()+") 审核播表("+playList1.getName()+")!");
                }
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }


    @RequestMapping("/updatePlayList")
    @ResponseBody
    public Map<String,Object> updatePlayList(ModelAndView mv,PlayList playList,String startWeek,
                                             String endWeek,HttpSession session){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Users users=(Users) session.getAttribute("user");
        try {
            Integer count=playListService.selectCountByName(playList.getName(),1,
                    playList.getId());
            if(count>0){
                mv.addObject("result","exit");
            }else{
                playList.setEditorID(users.getId());
                playList.setEditTime(sdf.format(new Date()));
                Integer i=playListService.updatePlayList(playList,startWeek,endWeek);
                if(i>0){
                    //日志记录
                    OperationLog operationLog=new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("版式管理");
                    operationLog.setContent("用户("+users.getName()+") 修改播表!");
                    operationLogService.addOperationLog(operationLog);
                    playListService.updatePlayListByID(playList.getId(),0,null);
                    mv.addObject("result","success");
                }else{
                    mv.addObject("result","error");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

    @RequestMapping("/deletePlay")
    @ResponseBody
    public Map<String,Object> deletePlay(ModelAndView mv,PlayList playList,HttpSession session){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Users users=(Users) session.getAttribute("user");
        try {
            playList.setEditorID(users.getId());
            playList.setEditTime(sdf.format(new Date()));
            Integer i=playListService.deletePlayList(playList);
            if(i>0){
                //日志记录
                OperationLog operationLog=new OperationLog();
                PlayList playList1=playListService.selectPlayList(playList.getId());
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("版式管理");
                if(playList1.getType()==7){
                    operationLog.setContent("用户("+users.getName()+") 删除预案("+playList1.getName()+")!");
                }else{
                    operationLog.setContent("用户("+users.getName()+") 删除播表("+playList1.getName()+")!");
                }
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result","success");
            }else{
                mv.addObject("result","error");
            }
        }catch (Exception e){
            e.printStackTrace();
            mv.addObject("result","error");
        }
        return mv.getModel();
    }

}
