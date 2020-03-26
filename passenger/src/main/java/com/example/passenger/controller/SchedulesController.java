package com.example.passenger.controller;

import com.example.passenger.service.SchedulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequestMapping("/schedules")
@RestController
public class SchedulesController {
    private static final Logger logger = LoggerFactory.getLogger(SchedulesController.class);

    @Autowired
    SchedulesService schedulesService;

    @GetMapping(value = "/getScheduleById", produces = MediaType.APPLICATION_XML_VALUE)
    public String getScheduleById(Integer clientid, Integer idxs) {
        String content = null;
        try {
            content = schedulesService.getScheduleById(clientid, idxs);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return content;
    }

    @GetMapping(value = "/getProgramById", produces = MediaType.APPLICATION_XML_VALUE)
    public String getProgramById(Integer idxs) {
        String content = null;
        try {
            content = schedulesService.getProgramById(idxs);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return content;
    }

    /**
     * @param playStyleId 版式id
     * @author suxijian
     * 根据版式id获取相应xml数据
     */
    @RequestMapping("/getPlayStyleXMLById")
    @ResponseBody
    public Map<String, Object> getPlayStyleXMLById(ModelAndView mv, Integer playStyleId) {
        try {
            String content = schedulesService.getProgramById(playStyleId);
            mv.addObject("playStyleXMLStr", content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

}
