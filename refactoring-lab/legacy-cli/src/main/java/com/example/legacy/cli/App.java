package com.example.legacy.cli;

import com.example.legacy.domain.Employee;
import com.example.legacy.domain.Event;
import com.example.legacy.repository.EmployeeRepository;
import com.example.legacy.repository.InMemoryEmployeeRepository;
import com.example.legacy.service.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class App {

    public static void main(String[] args) throws Exception {

        if (args == null || args.length < 2) {
            System.out.println("Usage: java -jar legacy-cli.jar <employees.csv> <events.csv>");
            System.exit(2);
        }

        File employeesFile = new File(args[0]);
        File eventsFile = new File(args[1]);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        ReflectionCsvMapper mapper = new ReflectionCsvMapper();
        EventParser eventParser = new EventParser();
        EmployeeService employeeService = new EmployeeService();
        StatsService statsService = new StatsService();
        ReportService reportService = new ReportService();

        Future<List<Employee>> employeesFuture =
                pool.submit(() -> mapper.read(employeesFile, Employee.class));

        Future<List<Event>> eventsFuture =
                pool.submit(() -> eventParser.readEvents(eventsFile));

        List<Employee> employees = employeesFuture.get();
        List<Event> events = eventsFuture.get();

        EmployeeRepository repo = new InMemoryEmployeeRepository();
        employees.forEach(repo::save);

        employeeService.applyEvents(employees, events);

        Map<String, Object> stats = statsService.buildStats(employees);

        System.out.println("Employees: " + stats.get("count"));
        System.out.println("Avg salary: " + stats.get("avgSalary"));
        System.out.println("By dept: " + stats.get("byDepartment"));

        File out = new File("report.xml");
        reportService.writeReport(out, employees, stats);

        System.out.println("Report written: " + out.getAbsolutePath());

        pool.shutdown();
        pool.awaitTermination(3, TimeUnit.SECONDS);
    }
}