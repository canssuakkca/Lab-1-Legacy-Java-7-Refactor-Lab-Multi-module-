package com.example.legacy.service;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import com.example.legacy.domain.*;
import com.example.legacy.util.DateUtil;
import com.example.legacy.util.StringUtils;

public class EventParser {

    public List<Event> readEvents(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String header = br.readLine();
            if (header == null) return Collections.emptyList();

            return br.lines()
                    .filter(line -> !StringUtils.isBlank(line))
                    .map(line -> parseLine(line))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to read events: " + file, e);
        }
    }

    private Event parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) return null;

        String employeeId = parts[0].trim();
        String type = parts[1].trim();
        Date at = DateUtil.parseIsoDate(parts[2].trim());
        String value = parts[3].trim();

        return switch (type) {
            case "SALARY_CHANGE" -> new SalaryChangeEvent(employeeId, at, Double.parseDouble(value));
            case "DEPT_CHANGE" -> new DepartmentChangeEvent(employeeId, at, value);
            default -> null;
        };
    }
}