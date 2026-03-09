package com.example.legacy.service;

import com.example.legacy.annotations.Column;
import com.example.legacy.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.*;

public class ReflectionCsvMapper {

    public <T> List<T> read(File file, Class<T> type) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String header = br.readLine();
            if (StringUtils.isBlank(header)) return Collections.emptyList();

            String[] headers = header.split(",");
            Map<String, Integer> indexByName = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                indexByName.put(headers[i].trim(), i);
            }

            List<T> result = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isBlank(line)) continue;

                String[] parts = line.split(",");
                T instance = type.getDeclaredConstructor().newInstance();

                for (Field field : type.getDeclaredFields()) {
                    Column c = field.getAnnotation(Column.class);
                    if (c == null) continue;

                    Integer idx = indexByName.get(c.name());
                    if (idx == null || idx >= parts.length) continue;

                    String raw = parts[idx].trim();
                    field.setAccessible(true);
                    setField(instance, field, raw);
                }

                result.add(instance);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read csv: " + file, e);
        }
    }

    private void setField(Object instance, Field field, String raw) throws IllegalAccessException {
        Class<?> t = field.getType();
        if (t.equals(String.class)) {
            field.set(instance, raw);
        } else if (t.equals(int.class) || t.equals(Integer.class)) {
            field.set(instance, StringUtils.isBlank(raw) ? 0 : Integer.parseInt(raw));
        } else if (t.equals(double.class) || t.equals(Double.class)) {
            field.set(instance, StringUtils.isBlank(raw) ? 0.0 : Double.parseDouble(raw));
        } else {
            field.set(instance, raw);
        }
    }
}