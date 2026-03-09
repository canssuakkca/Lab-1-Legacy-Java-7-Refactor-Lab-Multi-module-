package com.example.legacy.service;

import java.util.*;

import com.example.legacy.domain.Employee;
import com.example.legacy.util.StringUtils;

public class StatsService {

    public Map<String, Object> buildStats(List<Employee> employees) {
        Map<String, Object> stats = new HashMap<String, Object>();

        int count = 0;
        double totalSalary = 0;
        Employee max = null;

        Map<String, Integer> byDept = new HashMap<String, Integer>();

        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e == null) continue;
            count++;
            totalSalary += e.getSalary();

            if (max == null || e.getSalary() > max.getSalary()) {
                max = e;
            }

            String dept = e.getDepartment();
            if (StringUtils.isBlank(dept)) dept = "UNKNOWN";
            Integer current = byDept.get(dept);
            if (current == null) current = Integer.valueOf(0);
            byDept.put(dept, Integer.valueOf(current.intValue() + 1));
        }

        stats.put("count", Integer.valueOf(count));
        stats.put("totalSalary", Double.valueOf(totalSalary));
        stats.put("avgSalary", count == 0 ? Double.valueOf(0) : Double.valueOf(totalSalary / count));
        stats.put("maxSalaryEmployee", max);
        stats.put("byDepartment", byDept);

        return stats;
    }
}
