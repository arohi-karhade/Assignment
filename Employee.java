package com.assignment;

import java.util.Scanner;

abstract class Employee {
	String name;
	int age;
	double salary;
	String designation;
	
	public Employee(String name, int age, double salary, String designation) {
		this.name = name;
		this.age = age;
		this.salary = salary;
		this.designation = designation;
	}
	
	public void display() {
		System.out.println("Employee name: " + name);
		System.out.println("Employee age: " + age);
		System.out.println("Employee salary: " + salary);
		System.out.println("Employee designation: " + designation);
	}
	
	public void raiseSalary(double percentage) {
		salary += salary*percentage;
		System.out.println("Salary after increase is " + salary);
	}
}

class Clerk extends Employee{
	public Clerk(String name, int age, double salary) {
        super(name, age, salary, "Clerk");
    }
}

class Programmer extends Employee{
	public Programmer(String name, int age, double salary) {
        super(name, age, salary, "Programmer");
    }
}

class Manager extends Employee{
	public Manager(String name, int age, double salary) {
        super(name, age, salary, "Manager");
    }
}


class Main{
	public static void main(String[] args) {
		Employee e1 = new Clerk("Rohit Das", 30, 1500000);
		Employee e2 = new Programmer("Isha Joshi", 37, 1800000);
		Employee e3 = new Manager("Amit Kulkarni", 46, 2000000);
		
		System.out.println("--------------------------------");
		
		System.out.println("Clerk Details");
		e1.display();
		
		System.out.println("--------------------------------");
		
		System.out.println("Programmer Details");
		e2.display();
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter an integer: ");
	    int num = sc.nextInt();
		e2.raiseSalary(num);
		
		System.out.println("--------------------------------");
		
		System.out.println("Manager Details");
		e3.display();
		
		System.out.println("--------------------------------");
	}
}

