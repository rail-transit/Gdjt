package com.example.passenger.service;

import com.example.passenger.entity.Department;
import com.example.passenger.mapper.DepartmentMapper;
import com.example.passenger.utils.PageUtil;
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

    public List<Department> selectAllDepartment(){
        return departmentMapper.selectAllDepartment();
    }

    public Department selectDepartmentById(Integer id){
        return departmentMapper.selectDepartmentById(id);
    }

    public PageUtil selectDepartmentPaging(Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(departmentMapper.count());
        pageUtil.setPageData(departmentMapper.selectDepartmentPaging(pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addDepartment(Department department){
        try {
            return departmentMapper.addDepartment(department);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateDepartment(Department department){
        try {
            return departmentMapper.updateDepartment(department);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteDepartment(Integer id){
        try {
            return departmentMapper.deleteDepartment(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}
