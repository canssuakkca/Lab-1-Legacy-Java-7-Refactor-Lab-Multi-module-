package com.example.legacy.service;

import java.io.*;
import java.util.*;

import com.example.legacy.domain.*;
import com.example.legacy.util.DateUtil;
import com.example.legacy.util.StringUtils;

public class EventParser {

    public List<Event> readEvents(File file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String header = br.readLine();
            if (header == null) return Collections.emptyList();
            List<Event> out = new ArrayList<Event>();
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isBlank(line)) continue;
                String[] p = line.split(",");
                if (p.length < 4) continue;

                String employeeId = p[0].trim();
                String type = p[1].trim();
                Date at = DateUtil.parseIsoDate(p[2].trim());
                String value = p[3].trim();

                if ("SALARY_CHANGE".equals(type)) {
                    out.add(new SalaryChangeEvent(employeeId, at, Double.parseDouble(value)));
                } else if ("DEPT_CHANGE".equals(type)) {
                    out.add(new DepartmentChangeEvent(employeeId, at, value));
                }
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read events: " + file, e);
        } finally {
            if (br != null) {
                try { br.close(); } catch (Exception ignored) {}
            }
        }
    }
}
