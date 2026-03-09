package com.example.legacy.domain;

import java.util.Date;

public class SalaryChangeEvent extends Event {
    private double delta;

    public SalaryChangeEvent(String employeeId, Date at, double delta) {
        super(employeeId, at);
        this.delta = delta;
    }

    public double getDelta() { return delta; }

    public String getType() { return "SALARY_CHANGE"; }
}
