package com.example.passenger.mapper;

import com.example.passenger.entity.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {

    Department selectDepartmentById(@Param("id") Integer id);

    List<Department> selectAllDepartment();

    List<Department> selectDepartmentPaging(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);

    Integer count();

    Integer addDepartment(Department department);

    Integer updateDepartment(Department department);

    Integer deleteDepartment(@Param("id") Integer id);
}
