package com.example.passenger.controller;

import com.example.passenger.entity.Rights;
import com.example.passenger.service.RightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/path")
@EnableAutoConfiguration
public class PathController {

    @Autowired
    RightsService rightsService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("/top")
    public String top() {
        return "top";
    }

    @RequestMapping("/left")
    public String left(HttpSession session) {
        List<Rights> rightsList = (ArrayList<Rights>) session.getAttribute("rightsList");
        String content = "leftByTop/monitorLeft";
        List<Integer> list = new ArrayList<>();
        for (Rights rights : rightsList) {
            if (rights.getId() == 1 || rights.getId() == 2 || rights.getId() == 3
                    || rights.getId() == 5 || rights.getId() == 6) {
                list.add(rights.getId());
            }
        }
        if (list.size() > 0) {
            if (list.get(0) == 1) {
                content = "leftByTop/monitorLeft";
            } else if (list.get(0) == 2) {
                content = "leftByTop/messageLeft";
            } else if (list.get(0) == 3) {
                content = "leftByTop/scmLeft";
            } else if (list.get(0) == 5) {
                content = "leftByTop/journalLeft";
            } else if (list.get(0) == 6) {
                content = "leftByTop/messageAuditLeft";
            }
        }
        return content;
    }

    @RequestMapping("/right")
    public String right() {
        return "index";
    }

    @RequestMapping("/footer")
    public String footer() {
        return "event";
    }

    @RequestMapping("/contractStatistics")
    public String contractStatistics() {
        return "rightContent/advertisement/contractStatistics";
    }

    @RequestMapping("/customerStatistics")
    public String customerStatistics() {
        return "rightContent/advertisement/customerStatistics";
    }

    @RequestMapping("/chargeFactor")
    public String chargeFactor() {
        return "rightContent/advertisement/chargeFactor";
    }

    @RequestMapping("/chargeStatistics")
    public String chargeStatistics() {
        return "rightContent/advertisement/chargeStatistics";
    }

    @RequestMapping("/chargeType")
    public String chargeType() {
        return "rightContent/advertisement/chargeType";
    }

    @RequestMapping("/customerInfor")
    public String customerInfor() {
        return "rightContent/advertisement/customerInfor";
    }

    @RequestMapping("/customerTemplate")
    public String customerTemplate() {
        return "rightContent/advertisement/customerTemplate";
    }

    @RequestMapping("/customerAttribute")
    public String customerAttribute() {
        return "rightContent/advertisement/customerAttribute";
    }

    @RequestMapping("/contractInfor")
    public String contractInfor() {
        return "rightContent/advertisement/contractInfor";
    }

    @RequestMapping("/contractTemplate")
    public String contractTemplate() {
        return "rightContent/advertisement/contractTemplate";
    }

    @RequestMapping("/contractAttribute")
    public String contractAttribute() {
        return "rightContent/advertisement/contractAttribute";
    }

    @RequestMapping("/getDate")
    @ResponseBody
    public Map<String, Object> getDate(ModelAndView mv) {
        Calendar calendar = Calendar.getInstance();
        mv.addObject("year", calendar.get(Calendar.YEAR));
        mv.addObject("month", calendar.get(Calendar.MONTH) + 1);
        mv.addObject("date", calendar.get(Calendar.DATE));
        mv.addObject("day", calendar.get(Calendar.DAY_OF_WEEK) - 1);
        mv.addObject("hour", calendar.get(Calendar.HOUR_OF_DAY));
        mv.addObject("minute", calendar.get(Calendar.MINUTE));
        mv.addObject("second", calendar.get(Calendar.SECOND));
        return mv.getModel();
    }

    /**
     * 左部菜单
     *
     * @return
     */
    @RequestMapping("/advertisement")
    public String advertisement() {
        return "leftByTop/advertisementLeft";
    }

    @RequestMapping("/monitorLeft")
    public String monitorLeft() {
        return "leftByTop/monitorLeft";
    }

    @RequestMapping("/messageLeft")
    public String messageLeft() {
        return "leftByTop/messageLeft";
    }

    @RequestMapping("/scmLeft")
    public String scmLeft() {
        return "leftByTop/scmLeft";
    }

    @RequestMapping("/journalLeft")
    public String journalLeft() {
        return "leftByTop/journalLeft";
    }

    @RequestMapping("/messageAuditLeft")
    public String messageAuditLeft() {
        return "leftByTop/messageAuditLeft";
    }

}
