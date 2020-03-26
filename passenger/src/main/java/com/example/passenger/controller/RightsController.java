package com.example.passenger.controller;

import com.example.passenger.entity.Rights;
import com.example.passenger.service.RightsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/rights")
@Controller
public class RightsController {
    private static final Logger logger = LoggerFactory.getLogger(RightsController.class);

    @Autowired
    RightsService rightsService;

    @RequestMapping("/selectRights")
    @ResponseBody
    public Map<String, Object> selectRights(ModelAndView mv) {
        List<Rights> firstRight = rightsService.selectFirstRight(0);
        List<Rights> secondRights = new ArrayList<>();
        List<Rights> thirdRights = new ArrayList<>();
        for (Rights rights : firstRight) {
            List<Rights> rightsList = rightsService.selectFirstRight(rights.getId());
            for (Rights rights1 : rightsList) {
                secondRights.add(rights1);
                List<Rights> rightsList1 = rightsService.selectFirstRight(rights1.getId());
                for (Rights rights2 : rightsList1) {
                    thirdRights.add(rights2);
                }
            }
        }
        mv.addObject("firstRight", firstRight);
        mv.addObject("secondRights", secondRights);
        mv.addObject("thirdRights", thirdRights);
        return mv.getModel();
    }
}
