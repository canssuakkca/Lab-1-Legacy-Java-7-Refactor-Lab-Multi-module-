package com.acme.legacy.service;

import org.junit.Assert;
import org.junit.Test;

import com.example.legacy.domain.*;
import com.example.legacy.service.EmployeeService;

import java.util.*;

public class EmployeeServiceTest {

    @Test
    public void appliesSalaryChange() {
        Employee e = new Employee();
        e.setId("10");
        e.setSalary(100);

        List<Employee> employees = Arrays.asList(e);
        List<Event> events = Arrays.<Event>asList(new SalaryChangeEvent("10", new Date(), 25.5));

        new EmployeeService().applyEvents(employees, events);
        Assert.assertEquals(125.5, e.getSalary(), 0.0001);
    }
}
