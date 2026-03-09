package com.example.legacy.domain;

import java.util.Date;

public class DepartmentChangeEvent extends Event {
    private String newDepartment;

    public DepartmentChangeEvent(String employeeId, Date at, String newDepartment) {
        super(employeeId, at);
        this.newDepartment = newDepartment;
    }

    public String getNewDepartment() { return newDepartment; }

    public String getType() { return "DEPT_CHANGE"; }
}
