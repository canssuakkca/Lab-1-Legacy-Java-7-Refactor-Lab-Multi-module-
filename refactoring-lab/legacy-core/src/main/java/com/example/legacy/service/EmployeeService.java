package com.example.legacy.service;

import java.util.*;

import com.example.legacy.domain.*;

public class EmployeeService {

    public List<Employee> applyEvents(List<Employee> employees, List<Event> events) {
        Map<String, Employee> byId = new HashMap<String, Employee>();
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e != null && e.getId() != null) {
                byId.put(e.getId(), e);
            }
        }

        for (int j = 0; j < events.size(); j++) {
            Event ev = events.get(j);
            if (ev == null) continue;
            Employee target = byId.get(ev.getEmployeeId());
            if (target == null) continue;

            if (ev instanceof SalaryChangeEvent) {
                SalaryChangeEvent sc = (SalaryChangeEvent) ev;
                target.setSalary(target.getSalary() + sc.getDelta());
            } else if (ev instanceof DepartmentChangeEvent) {
                DepartmentChangeEvent dc = (DepartmentChangeEvent) ev;
                target.setDepartment(dc.getNewDepartment());
            }
        }
        return employees;
    }
}
