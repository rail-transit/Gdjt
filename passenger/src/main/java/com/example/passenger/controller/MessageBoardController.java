package com.example.passenger.controller;

import com.example.passenger.entity.MessageBoard;
import com.example.passenger.service.MessageBoardService;
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

import java.util.Map;

@RequestMapping("/messageBoard")
@Controller
public class MessageBoardController {
    private static final Logger logger = LoggerFactory.getLogger(MessageBoardController.class);

    @Autowired
    MessageBoardService messageBoardService;

    @RequestMapping("/messageBoardManagement")
    public String messageBoardManagement(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageUtil pageUtil = messageBoardService.messageBoardPaging(pageNum, 10);
        model.addAttribute("pageUtil", pageUtil);
        return "messageBoard";
    }


    @RequestMapping("/addMessageBoard")
    @ResponseBody
    public Map<String, Object> addMessageBoard(ModelAndView mv, MessageBoard messageBoard) {
        try {
            Integer i = messageBoardService.addMessageBoard(messageBoard);
            if (i > 0) {
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
