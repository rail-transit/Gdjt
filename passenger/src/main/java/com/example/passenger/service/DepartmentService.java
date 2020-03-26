package com.example.passenger.service;

import com.example.passenger.entity.Department;
import com.example.passenger.mapper.DepartmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    DepartmentMapper departmentMapper;

    public List<Department> selectAllDepartment() {
        return departmentMapper.selectAllDepartment();
    }

    public Department selectDepartmentById(Integer id) {
        return departmentMapper.selectDepartmentById(id);
    }

    public Integer selectDepartment(String name, Integer id) {
        return departmentMapper.selectDepartment(name, id);
    }

    @Transactional
    public Integer addDepartment(Department department) {
        try {
            if (department.getParentID() == 1) {
                department.setLevel(1);
            } else {
                Department department1 = departmentMapper.selectDepartmentById(department.getParentID());
                if (department1 != null) {
                    if (department1.getLevel() == 5) {
                        return -2;
                    } else {
                        department.setLevel(department1.getLevel() + 1);
                    }
                }
            }
            return departmentMapper.addDepartment(department);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateDepartment(Department department) {
        try {
            return departmentMapper.updateDepartment(department);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteDepartment(Integer id) {
        try {
            return departmentMapper.deleteDepartment(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}
