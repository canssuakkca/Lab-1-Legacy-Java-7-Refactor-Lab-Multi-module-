package com.example.legacy.repository;

import java.util.List;
import java.util.Optional;
import com.example.legacy.domain.Employee;

public interface EmployeeRepository {
    void save(Employee employee);
    Optional<Employee> findById(String id);
    List<Employee> findAll();
}