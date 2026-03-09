package com.example.legacy.domain;

import com.example.legacy.annotations.Column;

public class Employee {

    @Column(name = "id")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "department")
    private String department;

    @Column(name = "salary")
    private double salary;

    @Column(name = "email")
    private String email;

    @Column(name = "birth_year")
    private int birthYear;

    public Employee() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getBirthYear() { return birthYear; }
    public void setBirthYear(int birthYear) { this.birthYear = birthYear; }

    public String getFullName() {
        String fn = firstName == null ? "" : firstName;
        String ln = lastName == null ? "" : lastName;
        return (fn + " " + ln).trim();
    }

    public String toString() {
        return "Employee{id='" + id + "', name='" + getFullName() + "', dept='" + department + "', salary=" + salary + "}";
    }
}
