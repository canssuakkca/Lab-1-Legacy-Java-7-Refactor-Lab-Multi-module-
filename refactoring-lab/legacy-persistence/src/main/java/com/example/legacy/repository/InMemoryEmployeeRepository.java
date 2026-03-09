package com.example.legacy.repository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import com.example.legacy.domain.Employee;

public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final List<Employee> store = new CopyOnWriteArrayList<>();

    @Override
    public void save(Employee employee) {
        if (employee == null || employee.getId() == null) return;

        store.removeIf(e -> employee.getId().equals(e.getId()));
        store.add(employee);
    }

    @Override
    public Optional<Employee> findById(String id) {
        if (id == null) return Optional.empty();
        return store.stream()
                .filter(e -> id.equals(e.getId()))
                .findFirst();
    }

    @Override
    public List<Employee> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(store));
    }
}