package com.example.passenger.controller;

import com.example.passenger.service.PlayListDownloadStatusService;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequestMapping("/downloadStatus")
@Controller
public class PlayListDownloadStatusController {

    @Autowired
    PlayListDownloadStatusService playListDownloadStatusService;

    @RequestMapping("/selectDownload")
    @ResponseBody
    public Map<String,Object> selectDownload(ModelAndView mv, @RequestParam(defaultValue = "1")
            Integer pageNum, Integer playListID){
        PageUtil pageUtil=playListDownloadStatusService.selectPaging(playListID,pageNum,2);
        mv.addObject("pageUtil",pageUtil);
        return mv.getModel();
    }
}
