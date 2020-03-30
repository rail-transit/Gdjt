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
        String content = "leftMenu/monitorLeft";
        List<Integer> list = new ArrayList<>();
        for (Rights rights : rightsList) {
            if (rights.getId() == 1 || rights.getId() == 2 || rights.getId() == 3
                    || rights.getId() == 5 || rights.getId() == 6) {
                list.add(rights.getId());
            }
        }
        if (list.size() > 0) {
            if (list.get(0) == 1) {
                content = "leftMenu/monitorLeft";
            } else if (list.get(0) == 2) {
                content = "leftMenu/formatLeft";
            } else if (list.get(0) == 3) {
                content = "leftMenu/systemLeft";
            } else if (list.get(0) == 5) {
                content = "leftMenu/planLeft";
            } else if (list.get(0) == 6) {
                content = "leftMenu/reportLeft";
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
        return "rightContent/advertise/contractStatistics";
    }

    @RequestMapping("/customerStatistics")
    public String customerStatistics() {
        return "rightContent/advertise/customerStatistics";
    }

    @RequestMapping("/chargeFactor")
    public String chargeFactor() {
        return "rightContent/advertise/chargeFactor";
    }

    @RequestMapping("/chargeStatistics")
    public String chargeStatistics() {
        return "rightContent/advertise/chargeStatistics";
    }

    @RequestMapping("/chargeType")
    public String chargeType() {
        return "rightContent/advertise/chargeType";
    }

    @RequestMapping("/customerInfor")
    public String customerInfor() {
        return "rightContent/advertise/customerInfor";
    }

    @RequestMapping("/customerTemplate")
    public String customerTemplate() {
        return "rightContent/advertise/customerTemplate";
    }

    @RequestMapping("/customerAttribute")
    public String customerAttribute() {
        return "rightContent/advertise/customerAttribute";
    }

    @RequestMapping("/contractInfor")
    public String contractInfor() {
        return "rightContent/advertise/contractInfor";
    }

    @RequestMapping("/contractTemplate")
    public String contractTemplate() {
        return "rightContent/advertise/contractTemplate";
    }

    @RequestMapping("/contractAttribute")
    public String contractAttribute() {
        return "rightContent/advertise/contractAttribute";
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
    @RequestMapping("/advertise")
    public String advertise() {
        return "leftMenu/advertiseLeft";
    }

    @RequestMapping("/monitor")
    public String monitor() {
        return "leftMenu/monitorLeft";
    }

    @RequestMapping("/format")
    public String format() {
        return "leftMenu/formatLeft";
    }

    @RequestMapping("/system")
    public String system() {
        return "leftMenu/systemLeft";
    }

    @RequestMapping("/plan")
    public String plan() {
        return "leftMenu/planLeft";
    }

    @RequestMapping("/report")
    public String messageAuditLeft() {
        return "leftMenu/reportLeft";
    }

}
