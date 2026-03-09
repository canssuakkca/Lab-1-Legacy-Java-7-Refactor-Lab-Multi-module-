package com.example.legacy.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.example.legacy.domain.*;

public class EmployeeService {

    public List<Employee> applyEvents(List<Employee> employees, List<Event> events) {

        Map<String, Employee> byId =
                employees.stream()
                        .filter(Objects::nonNull)
                        .filter(e -> e.getId() != null)
                        .collect(Collectors.toMap(Employee::getId, Function.identity()));

        events.stream()
                .filter(Objects::nonNull)
                .forEach(ev -> {
                    Employee target = byId.get(ev.getEmployeeId());
                    if (target == null) return;

                    switch (ev) {
                        case SalaryChangeEvent sc ->
                                target.setSalary(target.getSalary() + sc.getDelta());

                        case DepartmentChangeEvent dc ->
                                target.setDepartment(dc.getNewDepartment());
                    }
                });

        return employees;
    }
}