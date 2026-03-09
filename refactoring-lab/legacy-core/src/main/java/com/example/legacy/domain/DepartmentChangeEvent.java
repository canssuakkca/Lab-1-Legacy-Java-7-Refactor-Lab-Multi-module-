package com.example.legacy.domain;

import java.util.Date;

public final class DepartmentChangeEvent extends Event {

    private final String newDepartment;

    public DepartmentChangeEvent(String employeeId, Date at, String newDepartment) {
        super(employeeId, at);
        this.newDepartment = newDepartment;
    }

    public String getNewDepartment() {
        return newDepartment;
    }

    @Override
    public String getType() {
        return "DEPT_CHANGE";
    }
}