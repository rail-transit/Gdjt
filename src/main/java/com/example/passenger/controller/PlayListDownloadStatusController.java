package com.example.passenger.controller;

import com.example.passenger.service.PlayListDownloadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/downloadStatus")
@Controller
public class PlayListDownloadStatusController {

    @Autowired
    PlayListDownloadStatusService playListDownloadStatusService;

}
