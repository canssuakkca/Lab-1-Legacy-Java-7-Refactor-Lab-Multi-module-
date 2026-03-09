package com.example.legacy.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.*;

import com.example.legacy.annotations.Column;
import com.example.legacy.util.StringUtils;

public class ReflectionCsvMapper {

    public <T> List<T> read(File file, Class<T> type) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String header = br.readLine();
            if (header == null) return Collections.emptyList();
            String[] headers = header.split(",");
            Map indexByName = new HashMap(); 
            for (int i = 0; i < headers.length; i++) {
                indexByName.put(headers[i].trim(), Integer.valueOf(i));
            }

            List<T> result = new ArrayList<T>();
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isBlank(line)) continue;
                String[] parts = line.split(",");
                T instance = type.newInstance();
                Field[] fields = type.getDeclaredFields();
                for (int j = 0; j < fields.length; j++) {
                    Field f = fields[j];
                    Column c = f.getAnnotation(Column.class);
                    if (c == null) continue;
                    Object idxObj = indexByName.get(c.name());
                    if (idxObj == null) continue;
                    int idx = ((Integer) idxObj).intValue();
                    if (idx >= parts.length) continue;
                    String raw = parts[idx].trim();
                    f.setAccessible(true);
                    setField(instance, f, raw);
                }
                result.add(instance);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read csv: " + file, e);
        } finally {
            if (br != null) {
                try { br.close(); } catch (Exception ignored) {}
            }
        }
    }

    private void setField(Object instance, Field f, String raw) throws IllegalAccessException {
        Class<?> t = f.getType();
        if (t.equals(String.class)) {
            f.set(instance, raw);
        } else if (t.equals(int.class) || t.equals(Integer.class)) {
            if (raw == null || raw.length() == 0) f.set(instance, Integer.valueOf(0));
            else f.set(instance, Integer.valueOf(Integer.parseInt(raw)));
        } else if (t.equals(double.class) || t.equals(Double.class)) {
            if (raw == null || raw.length() == 0) f.set(instance, Double.valueOf(0));
            else f.set(instance, Double.valueOf(Double.parseDouble(raw)));
        } else {
            f.set(instance, raw);
        }
    }
}
