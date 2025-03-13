package ru.mephi.week4.lesson2.stream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SimpleStreamsExample {

    static class Employee {
        private String name;
        private int age;
        private String department;
        private double salary;
        private boolean isActive;

        public Employee(String name, int age, String department, double salary, boolean isActive) {
            this.name = name;
            this.age = age;
            this.department = department;
            this.salary = salary;
            this.isActive = isActive;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getDepartment() {
            return department;
        }

        public double getSalary() {
            return salary;
        }

        public boolean isActive() {
            return isActive;
        }

        @Override
        public String toString() {
            return name + " (" + age + " лет, " + department + ", $" + salary + ", активен: " + isActive + ")";
        }
    }

    static List<Employee> getActiveEmployees(List<Employee> employees) {

        return employees.stream()
                .filter(Employee::isActive)
                .collect(Collectors.toList());

    }

    static List<Employee> getOlderFromItEmployees(List<Employee> employees, int ageCutOff) {

        return employees.stream()
                .filter(it -> it.getAge() > ageCutOff && it.getDepartment().equalsIgnoreCase("it"))
                .toList();

    }

    static double getAverageSalaryForDepartment(List<Employee> employees, String department) {

        return employees.stream()
                .filter(it -> department.equalsIgnoreCase(it.getDepartment()))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElseThrow(() -> new RuntimeException("No workers from this department"));

    }

    static Map<String, List<Employee>> groupByDepartments(List<Employee> employees) {

        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));

    }


}
