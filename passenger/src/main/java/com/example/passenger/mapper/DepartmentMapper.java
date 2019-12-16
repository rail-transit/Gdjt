package com.example.passenger.mapper;

import com.example.passenger.entity.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {

    Department selectDepartmentById(@Param("id") Integer id);

    List<Department> selectAllDepartment();

    Integer selectDepartment(@Param("name") String name,@Param("id") Integer id);

    Integer addDepartment(Department department);

    Integer updateDepartment(Department department);

    Integer deleteDepartment(@Param("id") Integer id);
}
