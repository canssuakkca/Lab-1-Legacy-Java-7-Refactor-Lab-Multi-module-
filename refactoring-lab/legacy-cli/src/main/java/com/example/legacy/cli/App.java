package com.example.legacy.cli;

import com.example.legacy.domain.Employee;
import com.example.legacy.domain.Event;
import com.example.legacy.repository.EmployeeRepository;
import com.example.legacy.repository.InMemoryEmployeeRepository;
import com.example.legacy.service.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class App {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            System.out.println("Usage: java -jar legacy-cli.jar <employees.csv> <events.csv>");
            System.exit(2);
        }

        final File employeesFile = new File(args[0]);
        final File eventsFile = new File(args[1]);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<List<Employee>> employeesFuture = pool.submit(new Callable<List<Employee>>() {
            public List<Employee> call() throws Exception {
                ReflectionCsvMapper mapper = new ReflectionCsvMapper();
                return mapper.read(employeesFile, Employee.class);
            }
        });

        Future<List<Event>> eventsFuture = pool.submit(new Callable<List<Event>>() {
            public List<Event> call() throws Exception {
                return new EventParser().readEvents(eventsFile);
            }
        });

        List<Employee> employees = employeesFuture.get();
        List<Event> events = eventsFuture.get();

        EmployeeRepository repo = new InMemoryEmployeeRepository();
        for (int i = 0; i < employees.size(); i++) {
            repo.save(employees.get(i));
        }

        new EmployeeService().applyEvents(employees, events);

        Map<String,Object> stats = new StatsService().buildStats(employees);

        System.out.println("Employees: " + stats.get("count"));
        System.out.println("Avg salary: " + stats.get("avgSalary"));
        System.out.println("By dept: " + stats.get("byDepartment"));

        File out = new File("report.xml");
        new ReportService().writeReport(out, employees, stats);
        System.out.println("Report written: " + out.getAbsolutePath());

        pool.shutdown();
        pool.awaitTermination(3, TimeUnit.SECONDS);
    }
}
