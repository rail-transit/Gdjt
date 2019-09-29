package com.example.passenger.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return "left";
    }//leftByTop/monitorLeft

    @RequestMapping("/right")
    public String right (){
        return "index";
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
