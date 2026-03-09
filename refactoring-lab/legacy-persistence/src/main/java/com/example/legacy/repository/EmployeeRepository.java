package com.example.legacy.repository;

import java.util.List;

import com.example.legacy.domain.Employee;

public interface EmployeeRepository {
    void save(Employee employee);
    Employee findById(String id);
    List<Employee> findAll();
}
