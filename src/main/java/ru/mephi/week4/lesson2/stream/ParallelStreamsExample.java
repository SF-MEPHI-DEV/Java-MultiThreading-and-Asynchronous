package ru.mephi.week4.lesson2.stream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParallelStreamsExample {

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
        return employees.parallelStream()
                .filter(Employee::isActive)
                .collect(Collectors.toList());
    }

    static List<Employee> getOlderFromItEmployees(List<Employee> employees, int ageCutOff) {
        return employees.parallelStream()
                .filter(it -> it.getAge() > ageCutOff && it.getDepartment().equalsIgnoreCase("it"))
                .toList();
    }

    static double getAverageSalaryForDepartment(List<Employee> employees, String department) {
        return employees.parallelStream()
                .filter(it -> department.equalsIgnoreCase(it.getDepartment()))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElseThrow(() -> new RuntimeException("No workers from this department"));
    }

    static Map<String, List<Employee>> groupByDepartments(List<Employee> employees) {
        return employees.parallelStream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee("Иван", 30, "IT", 1000, true),
                new Employee("Мария", 28, "HR", 1100, true),
                new Employee("Андрей", 45, "IT", 1500, false),
                new Employee("Сергей", 50, "Finance", 2000, true),
                new Employee("Анна", 35, "IT", 1300, true),
                new Employee("Петр", 40, "HR", 1200, false)
        );

        System.out.println("Активные сотрудники:");
        getActiveEmployees(employees).forEach(System.out::println);

        System.out.println("\nIT-сотрудники старше 30 лет:");
        getOlderFromItEmployees(employees, 30).forEach(System.out::println);

        System.out.println("\nСредняя зарплата в IT-отделе:");
        System.out.println(getAverageSalaryForDepartment(employees, "IT"));

        System.out.println("\nГруппировка по отделам:");
        groupByDepartments(employees).forEach((dept, list) -> {
            System.out.println(dept + ": " + list);
        });
    }


}
