package com.example.passenger.controller;

import com.example.passenger.service.OperationLogService;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/operationLog")
public class OperationLogController {
    private static final Logger logger = LoggerFactory.getLogger(OperationLogController.class);

    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/journalStatistics")
    public String journalStatistics() {
        return "rightContent/report/journal";
    }

    @RequestMapping("/selectStatistics")
    @ResponseBody
    public Map<String, Object> selectStatistics(@RequestParam(defaultValue = "1") Integer pageNum,
                                                ModelAndView mv, String startTime, String endTime, String type) {
        try {
            PageUtil pageUtil = operationLogService.selectPaging(startTime, endTime, type, pageNum, 12);
            mv.addObject("pageUtil", pageUtil);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/getJournal")
    @ResponseBody
    public Map<String, Object> getJournal(ModelAndView mv, String startTime,
                                          String endTime, String type) {
        try {
            List<Map<String, String>> operationLogList = operationLogService.getJournal(startTime,
                    endTime, type);
            mv.addObject("operationLogList", operationLogList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }
}
