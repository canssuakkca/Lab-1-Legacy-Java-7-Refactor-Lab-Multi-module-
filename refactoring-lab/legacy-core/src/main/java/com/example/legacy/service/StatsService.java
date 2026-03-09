package com.example.legacy.service;

import java.util.*;
import java.util.stream.*;

import com.example.legacy.domain.Employee;
import com.example.legacy.util.StringUtils;

public class StatsService {

    public Map<String, Object> buildStats(List<Employee> employees) {
        Map<String, Object> stats = new HashMap<>();

        List<Employee> validEmployees = employees.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        double totalSalary = validEmployees.stream()
                .mapToDouble(Employee::getSalary)
                .sum();

        double avgSalary = validEmployees.isEmpty() ? 0 : totalSalary / validEmployees.size();

        Employee maxSalaryEmployee = validEmployees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary))
                .orElse(null);

        Map<String, Long> byDepartment = validEmployees.stream()
                .collect(Collectors.groupingBy(
                        e -> StringUtils.isBlank(e.getDepartment()) ? "UNKNOWN" : e.getDepartment(),
                        Collectors.counting()
                ));

        stats.put("count", validEmployees.size());
        stats.put("totalSalary", totalSalary);
        stats.put("avgSalary", avgSalary);
        stats.put("maxSalaryEmployee", maxSalaryEmployee);
        stats.put("byDepartment", byDepartment);

        return stats;
    }
}