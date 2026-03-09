package com.acme.legacy.service;

import org.junit.Assert;
import org.junit.Test;

import com.example.legacy.domain.Employee;
import com.example.legacy.service.StatsService;

import java.util.*;

public class StatsServiceTest {

    @Test
    public void buildsStats() {
        List<Employee> employees = new ArrayList<Employee>();
        Employee a = new Employee(); a.setId("1"); a.setFirstName("A"); a.setLastName("One"); a.setDepartment("AI"); a.setSalary(100);
        Employee b = new Employee(); b.setId("2"); b.setFirstName("B"); b.setLastName("Two"); b.setDepartment("AI"); b.setSalary(200);
        Employee c = new Employee(); c.setId("3"); c.setFirstName("C"); c.setLastName("Three"); c.setDepartment("DATA"); c.setSalary(300);
        employees.add(a); employees.add(b); employees.add(c);

        Map<String,Object> stats = new StatsService().buildStats(employees);
        Assert.assertEquals(3, ((Integer)stats.get("count")).intValue());
        Assert.assertEquals(200.0, ((Double)stats.get("avgSalary")).doubleValue(), 0.0001);
        Assert.assertNotNull(stats.get("byDepartment"));
    }
}
