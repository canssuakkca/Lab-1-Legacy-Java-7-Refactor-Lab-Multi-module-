package com.example.legacy.repository;

import java.sql.*;
import java.util.*;
import com.example.legacy.domain.Employee;

public class JdbcEmployeeRepository implements EmployeeRepository {

    private final String url;
    private final Properties props;

    public JdbcEmployeeRepository(String url, Properties props) {
        this.url = url;
        this.props = props;
    }

    @Override
    public void save(Employee employee) {
        if (employee == null || employee.getId() == null) return;

        String sql = "INSERT INTO employees(id, first_name, last_name, department, salary, email, birth_year) " +
                "VALUES(?,?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE " +
                "first_name=VALUES(first_name), last_name=VALUES(last_name), " +
                "department=VALUES(department), salary=VALUES(salary), " +
                "email=VALUES(email), birth_year=VALUES(birth_year)";

        try (Connection conn = DriverManager.getConnection(url, props);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employee.getId());
            ps.setString(2, employee.getFirstName());
            ps.setString(3, employee.getLastName());
            ps.setString(4, employee.getDepartment());
            ps.setDouble(5, employee.getSalary());
            ps.setString(6, employee.getEmail());
            ps.setInt(7, employee.getBirthYear());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Failed to save employee: " + employee, ex);
        }
    }

    @Override
    public Optional<Employee> findById(String id) {
        if (id == null) return Optional.empty();

        String sql = "SELECT id, first_name, last_name, department, salary, email, birth_year " +
                "FROM employees WHERE id=?";

        try (Connection conn = DriverManager.getConnection(url, props);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                Employee e = mapRow(rs);
                return Optional.of(e);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Failed to find employee with id: " + id, ex);
        }
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT id, first_name, last_name, department, salary, email, birth_year FROM employees";

        try (Connection conn = DriverManager.getConnection(url, props);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Employee> out = new ArrayList<>();
            while (rs.next()) {
                out.add(mapRow(rs));
            }
            return Collections.unmodifiableList(out);

        } catch (SQLException ex) {
            throw new RuntimeException("Failed to find all employees", ex);
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getString("id"));
        e.setFirstName(rs.getString("first_name"));
        e.setLastName(rs.getString("last_name"));
        e.setDepartment(rs.getString("department"));
        e.setSalary(rs.getDouble("salary"));
        e.setEmail(rs.getString("email"));
        e.setBirthYear(rs.getInt("birth_year"));
        return e;
    }
}