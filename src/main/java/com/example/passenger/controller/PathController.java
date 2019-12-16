package com.example.passenger.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Map;

@Controller
@RequestMapping("/path")
@EnableAutoConfiguration
public class PathController {

    /**
     * 首页
     * @return
     */
    @RequestMapping("/top")
    public String top (){
        return "top";
    }

    @RequestMapping("/left")
    public String left (){
        return "leftByTop/monitorLeft";
    }//leftByTop/monitorLeft

    @RequestMapping("/right")
    public String right (){
        return "index";
    }

    @RequestMapping("/footer")
    public String footer (){
        return "event";
    }

    @RequestMapping("/getDate")
    @ResponseBody
    public Map<String,Object> getDate(ModelAndView mv){
        Calendar calendar = Calendar.getInstance();
        mv.addObject("year",calendar.get(Calendar.YEAR));
        mv.addObject("month",calendar.get(Calendar.MONTH)+1);
        mv.addObject("date",calendar.get(Calendar.DATE));
        mv.addObject("day",calendar.get(Calendar.DAY_OF_WEEK)-1);
        mv.addObject("hour",calendar.get(Calendar.HOUR_OF_DAY));
        mv.addObject("minute",calendar.get(Calendar.MINUTE));
        mv.addObject("second",calendar.get(Calendar.SECOND));
        return mv.getModel();
    }



    /**
     * 左部菜单
     * @return
     */
    @RequestMapping("/monitorLeft")
    public String monitorLeft (){
        return "leftByTop/monitorLeft";
    }

    @RequestMapping("/messageLeft")
    public String messageLeft (){
        return "leftByTop/messageLeft";
    }

    @RequestMapping("/scmLeft")
    public String scmLeft (){
        return "leftByTop/scmLeft";
    }

    @RequestMapping("/journalLeft")
    public String journalLeft (){
        return "leftByTop/journalLeft";
    }

    @RequestMapping("/messageAuditLeft")
    public String messageAuditLeft (){
        return "leftByTop/messageAuditLeft";
    }

}
