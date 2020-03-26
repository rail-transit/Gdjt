package com.example.passenger.controller;

import com.example.passenger.entity.OperationLog;
import com.example.passenger.entity.PlayListStyle;
import com.example.passenger.entity.PlayStyle;
import com.example.passenger.entity.Users;
import com.example.passenger.service.*;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/playStyle")
@Controller
public class PlayStyleController {
    private static final Logger logger = LoggerFactory.getLogger(PlayStyleController.class);

    @Autowired
    PlayStyleService playStyleService;
    @Autowired
    PlayListStyleService playListStyleService;
    @Autowired
    PlayListService playListService;
    @Autowired
    SchedulesService schedulesService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/playStyleManagement")
    public String playStyleManagement(Model model) {
        return "rightContent/playStyle/playStyle";
    }

    @RequestMapping("/releasePlayStyle")
    public String releasePlayStyle(Model model) {
        return "rightContent/playStyle/releasePlayStyle";
    }

    @RequestMapping("/selectPlayStyle")
    @ResponseBody
    public Map<String, Object> selectPlayStyle(ModelAndView mv, @RequestParam(defaultValue = "2") Integer state,
                                               String name, @RequestParam(defaultValue = "1") Integer pageNum,
                                               Integer isTemplate) {
        PageUtil pageUtil = playStyleService.selectPaging(state, name, isTemplate, pageNum, 10);
        mv.addObject("pageUtil", pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/auditPlayStyle")
    @ResponseBody
    public Map<String, Object> auditPlayStyle(ModelAndView mv, PlayStyle playStyle, HttpSession session) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PlayStyle playStyle1 = playStyleService.selectPlayStyle(playStyle.getId());
        Users users = (Users) session.getAttribute("user");
        try {
            PlayListStyle playListStyle = playListStyleService.selectPlayListByStyleID(playStyle.getId());
            if (playListStyle != null) {
                mv.addObject("result", "exit");
            } else {
                playStyle.setEditorID(users.getName());
                playStyle.setEditTime(sdf.format(new Date()));
                Integer i = playStyleService.updatePlayStyle(playStyle);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("版式管理");
                    if (playStyle.getState() == 1) {
                        operationLog.setContent("用户(" + users.getName() + ") 审核版式(" + playStyle1.getName() + ")审核通过!");
                    } else {
                        operationLog.setContent("用户(" + users.getName() + ") 审核版式(" + playStyle1.getName() + ")审核不通过!");
                    }
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/deletePlayStyle")
    @ResponseBody
    public Map<String, Object> deletePlayStyle(ModelAndView mv, PlayStyle playStyle, HttpSession session) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Users users = (Users) session.getAttribute("user");
        PlayStyle playStyle1 = playStyleService.selectPlayStyle(playStyle.getId());
        try {
            PlayListStyle playListStyle = playListStyleService.selectPlayListByStyleID(playStyle.getId());
            if (playListStyle != null) {
                mv.addObject("result", "exit");
            } else {
                playStyle.setEditorID(users.getName());
                playStyle.setEditTime(sdf.format(new Date()));
                Integer i = playStyleService.deletePlayStyle(playStyle);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("版式管理");
                    operationLog.setContent("用户(" + users.getName() + ") 删除版式(" + playStyle1.getName() + ")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/selectPlayListStyle")
    @ResponseBody
    public Map<String, Object> selectPlayListStyle(ModelAndView mv, Integer playListID,
                                                   @RequestParam(defaultValue = "1") Integer pageNum) {
        PageUtil pageUtil = playListStyleService.selectPaging(playListID, pageNum, 10);
        mv.addObject("pageUtil", pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/getPlayListStyle")
    @ResponseBody
    public Map<String, Object> getPlayListStyle(ModelAndView mv, Integer playListID) {
        List<PlayListStyle> listStyleList = playListStyleService.getPlayListStyle(playListID);
        mv.addObject("listStyleList", listStyleList);
        return mv.getModel();
    }

    @RequestMapping("/getWidthHeight")
    @ResponseBody
    public Map<String, Object> getWidthHeight(ModelAndView mv, Integer playID) {
        PlayListStyle playListStyle = playListStyleService.selectPlayListStyle(playID);
        PlayStyle playStyle = playStyleService.selectPlayStyle(playListStyle.getStyleID());
        String width = playStyle.getContentText().substring(
                playStyle.getContentText().indexOf("<width>") + 7, playStyle.getContentText().indexOf("</width>"));
        String height = playStyle.getContentText().substring(
                playStyle.getContentText().indexOf("<height>") + 8, playStyle.getContentText().indexOf("</height>"));
        mv.addObject("width", width);
        mv.addObject("height", height);
        return mv.getModel();
    }


    @RequestMapping("/addPlayListStyle")
    @ResponseBody
    public Map<String, Object> addPlayListStyle(ModelAndView mv, PlayListStyle playListStyle,
                                                HttpSession session) {
        playListStyle.setSequence(1);
        playListStyle.setPlayTimes(1);
        playListStyle.setPlayType("23:59:59");
        try {
            Integer i = playListStyleService.addPlayListStyle(playListStyle);
            if (i > 0) {
                //日志记录
                OperationLog operationLog = new OperationLog();
                Users users = (Users) session.getAttribute("user");
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("版式管理");
                operationLog.setContent("用户(" + users.getName() + ") 向播表添加版式!");
                operationLogService.addOperationLog(operationLog);
                playListService.updatePlayListByID(playListStyle.getPlaylistID(), 0, null);
                mv.addObject("result", "success");
            } else {
                mv.addObject("result", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/deletePlayListStyle")
    @ResponseBody
    public Map<String, Object> deletePlayListStyle(ModelAndView mv, Integer id, Integer playListID,
                                                   HttpSession session) {
        try {
            Integer i = playListStyleService.deletePlayListStyle(id);
            if (i > 0) {
                //日志记录
                OperationLog operationLog = new OperationLog();
                Users users = (Users) session.getAttribute("user");
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("版式管理");
                operationLog.setContent("用户(" + users.getName() + ") 删除播表的版式!");
                operationLogService.addOperationLog(operationLog);
                playListService.updatePlayListByID(playListID, 0, null);
                mv.addObject("result", "success");
            } else {
                mv.addObject("result", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }
}
