package com.example.legacy.domain;

import java.util.Date;

public abstract class Event {
    private String employeeId;
    private Date at;

    protected Event(String employeeId, Date at) {
        this.employeeId = employeeId;
        this.at = at;
    }

    public String getEmployeeId() { return employeeId; }
    public Date getAt() { return at; }
    public abstract String getType();
}
