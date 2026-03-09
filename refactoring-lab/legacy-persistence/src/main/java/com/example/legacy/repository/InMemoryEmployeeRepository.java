package com.example.legacy.repository;

import java.util.*;

import com.example.legacy.domain.Employee;

public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final List<Employee> store = new ArrayList<Employee>();

    public void save(Employee employee) {
        if (employee == null || employee.getId() == null) return;
        for (int i = 0; i < store.size(); i++) {
            if (employee.getId().equals(store.get(i).getId())) {
                store.set(i, employee);
                return;
            }
        }
        store.add(employee);
    }

    public Employee findById(String id) {
        if (id == null) return null;
        for (int i = 0; i < store.size(); i++) {
            Employee e = store.get(i);
            if (id.equals(e.getId())) return e;
        }
        return null;
    }

    public List<Employee> findAll() {
        return new ArrayList<Employee>(store);
    }
}
