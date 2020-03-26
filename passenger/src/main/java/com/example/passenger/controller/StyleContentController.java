package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.entity.vo.StyleContentVo;
import com.example.passenger.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RequestMapping("/styleContent")
@Controller
public class StyleContentController {
    private static final Logger logger = LoggerFactory.getLogger(StyleContentController.class);

    @Autowired
    StyleContentService styleContentService;
    @Autowired
    PlayListStyleService playListStyleService;
    @Autowired
    PlayListService playListService;
    @Autowired
    PlayStyleService playStyleService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/getStyleContent")
    @ResponseBody
    public Map<String, Object> getStyleContent(ModelAndView mv, Integer playID) {
        PlayListStyle playListStyle = playListStyleService.selectPlayListStyle(playID);
        List<StyleContentVo> styleContentVoList = styleContentService.selectStyleContentVo(playListStyle.getStyleID());
        mv.addObject("styleContentVoList", styleContentVoList);
        return mv.getModel();
    }

    @RequestMapping("/addStyleContent")
    @ResponseBody
    public Map<String, Object> addStyleContent(ModelAndView mv, HttpSession session,
                                               StyleContent styleContent, String time) {
        try {
            String[] a = time.split("\\.");
            String[] b = a[0].split(":");
            styleContent.setTimeLength(Integer.valueOf(b[0]) * 60 * 60 + Integer.valueOf(b[1]) * 60 + Integer.valueOf(b[2]));
            PlayListStyle playListStyle = playListStyleService.selectPlayListStyle(styleContent.getStyleID());
            if (playListStyle != null) {
                styleContent.setStyleID(playListStyle.getStyleID());
                playListService.updatePlayListByID(styleContent.getStyleID(), 0, null);
                Integer i = styleContentService.addStyleContent(styleContent);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    Users user = (Users) session.getAttribute("user");
                    PlayStyle playStyle = playStyleService.selectPlayStyle(styleContent.getStyleID());
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("预案管理");
                    operationLog.setContent("用户(" + user.getName() + ") 添加媒体(" + playStyle.getName() + ")成功!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            } else {
                mv.addObject("result", "notExit");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/updateStyleContent")
    @ResponseBody
    public Map<String, Object> updateStyleContent(ModelAndView mv, HttpSession session, StyleContent styleContent,
                                                  String time, Integer playListID) {
        PlayList playList = playListService.selectPlayList(playListID);
        Users user = (Users) session.getAttribute("user");
        try {
            //处理播放时长
            String[] a = time.split("\\.");
            String[] b = a[0].split(":");
            styleContent.setTimeLength(Integer.valueOf(b[0]) * 60 * 60 + Integer.valueOf(b[1]) * 60 + Integer.valueOf(b[2]));
            //修改素材
            Integer i = styleContentService.updateStyleContent(styleContent);
            if (i > 0) {
                playListService.updatePlayListByID(playListID, 0, null);
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(user.getId().toString());
                operationLog.setType("预案管理");
                operationLog.setContent("用户(" + user.getName() + ") 修改预案(" + playList.getName() + ")的媒体信息成功!");
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result", "success");
            } else {
                mv.addObject("result", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteStyleContent")
    @ResponseBody
    public Map<String, Object> deleteStyleContent(ModelAndView mv, HttpSession session,
                                                  Integer id, Integer playListID) {
        Users user = (Users) session.getAttribute("user");
        StyleContent styleContent = styleContentService.selectContentById(id);
        PlayStyle playStyle = playStyleService.selectPlayStyle(styleContent.getStyleID());
        try {
            Integer i = styleContentService.deleteStyleContent(id);
            if (i > 0) {
                playListService.updatePlayListByID(playListID, 0, null);
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(user.getId().toString());
                operationLog.setType("预案管理");
                operationLog.setContent("用户(" + user.getName() + ") 删除媒体(" + playStyle.getName() + ")成功!");
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result", "success");
            } else {
                mv.addObject("result", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }
}
