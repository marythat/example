package com.newsupplytech.projectapplication.modules.vo;

import com.newsupplytech.projectapplication.modules.entity.Department;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 部门表 封装树结构的部门的名称的实体类
 * <p>
 *
 * @Author Steve
 * @Since 2019-01-22
 *
 */
@Data
public class DepartmentModel {

    private static final long serialVersionUID = 1L;

    // 主键ID
    private Long key;

    // 部门名称
    private String title;

    List<DepartmentModel> children = new ArrayList<>();

    public DepartmentModel convert(Department department) {
        this.key = department.getDptid();
        this.title = department.getDptname();
        return this;
    }
}
