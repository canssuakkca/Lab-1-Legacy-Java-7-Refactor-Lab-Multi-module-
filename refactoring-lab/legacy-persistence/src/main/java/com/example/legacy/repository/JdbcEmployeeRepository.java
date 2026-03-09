package com.example.legacy.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.example.legacy.domain.Employee;

public class JdbcEmployeeRepository implements EmployeeRepository {

    private final String url;
    private final Properties props;

    public JdbcEmployeeRepository(String url, Properties props) {
        this.url = url;
        this.props = props;
    }

    public void save(Employee employee) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = DriverManager.getConnection(url, props);
            ps = c.prepareStatement("INSERT INTO employees(id, first_name, last_name, department, salary, email, birth_year) VALUES(?,?,?,?,?,?,?)");
            ps.setString(1, employee.getId());
            ps.setString(2, employee.getFirstName());
            ps.setString(3, employee.getLastName());
            ps.setString(4, employee.getDepartment());
            ps.setDouble(5, employee.getSalary());
            ps.setString(6, employee.getEmail());
            ps.setInt(7, employee.getBirthYear());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
            if (c != null) try { c.close(); } catch (Exception ignored) {}
        }
    }

    public Employee findById(String id) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DriverManager.getConnection(url, props);
            ps = c.prepareStatement("SELECT id, first_name, last_name, department, salary, email, birth_year FROM employees WHERE id=?");
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (!rs.next()) return null;
            Employee e = new Employee();
            e.setId(rs.getString(1));
            e.setFirstName(rs.getString(2));
            e.setLastName(rs.getString(3));
            e.setDepartment(rs.getString(4));
            e.setSalary(rs.getDouble(5));
            e.setEmail(rs.getString(6));
            e.setBirthYear(rs.getInt(7));
            return e;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
            if (c != null) try { c.close(); } catch (Exception ignored) {}
        }
    }

    public List<Employee> findAll() {
        Connection c = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            c = DriverManager.getConnection(url, props);
            st = c.createStatement();
            rs = st.executeQuery("SELECT id, first_name, last_name, department, salary, email, birth_year FROM employees");
            List<Employee> out = new ArrayList<Employee>();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getString(1));
                e.setFirstName(rs.getString(2));
                e.setLastName(rs.getString(3));
                e.setDepartment(rs.getString(4));
                e.setSalary(rs.getDouble(5));
                e.setEmail(rs.getString(6));
                e.setBirthYear(rs.getInt(7));
                out.add(e);
            }
            return out;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (st != null) try { st.close(); } catch (Exception ignored) {}
            if (c != null) try { c.close(); } catch (Exception ignored) {}
        }
    }
}
