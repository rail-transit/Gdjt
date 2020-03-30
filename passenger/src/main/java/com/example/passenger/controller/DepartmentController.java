package com.example.passenger.controller;

import com.example.passenger.entity.Department;
import com.example.passenger.entity.OperationLog;
import com.example.passenger.entity.Users;
import com.example.passenger.service.DepartmentService;
import com.example.passenger.service.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/department")
@Controller
public class DepartmentController {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    DepartmentService departmentService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/departmentManage")
    public String departmentManagement() {
        return "rightContent/system/departmentManage";
    }

    @RequestMapping("/getDepartment")
    @ResponseBody
    public Map<String, Object> getDepartment(ModelAndView mv) {
        List<Department> departmentList = departmentService.selectAllDepartment();
        mv.addObject("departmentList", departmentList);
        return mv.getModel();
    }

    @RequestMapping("/addDepartment")
    @ResponseBody
    public Map<String, Object> addDepartment(ModelAndView mv, Department department, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        try {
            Integer count = departmentService.selectDepartment(department.getName(), null);
            if (count > 0) {
                mv.addObject("result", "exist");
            } else {
                Integer i = departmentService.addDepartment(department);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + user.getName() + ") 添加部门(" + department.getName() + ")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else if (i == -2) {
                    mv.addObject("result", "beyond");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/updateDepartment")
    @ResponseBody
    public Map<String, Object> updateDepartment(ModelAndView mv, Department department, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        try {
            Integer count = departmentService.selectDepartment(department.getName(), department.getId());
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = departmentService.updateDepartment(department);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + user.getName() + ") 修改部门(" + department.getName() + ")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteDepartment")
    @ResponseBody
    public Map<String, Object> deleteDepartment(ModelAndView mv, HttpSession session, @RequestBody ArrayList<Integer> list) {
        try {
            for (Integer id : list) {
                Department department = departmentService.selectDepartmentById(id);
                Integer i = departmentService.deleteDepartment(id);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    Users user = (Users) session.getAttribute("user");
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + user.getName() + ") 删除部门(" + department.getName() + ")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }
}
