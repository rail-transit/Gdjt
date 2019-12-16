package com.example.passenger.controller;

import com.example.passenger.service.SchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/schedules")
@RestController
public class SchedulesController {
    @Autowired
    SchedulesService schedulesService;

    @GetMapping(value = "/getScheduleById",produces = MediaType.APPLICATION_XML_VALUE)
    public String getScheduleById(Integer clientid, Integer idxs){
        String content=null;
        try {
            content=schedulesService.getScheduleById(idxs);
        }catch (Exception e){
            e.printStackTrace();
        }
        return content;
    }

    @GetMapping(value = "/getProgramById",produces = MediaType.APPLICATION_XML_VALUE)
    public String getProgramById(Integer idxs){
        String content=null;
        try {
            content=schedulesService.getProgramById(idxs);
        }catch (Exception e){
            e.printStackTrace();
        }
        return content;
    }

}
