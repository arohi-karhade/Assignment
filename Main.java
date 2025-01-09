import java.util.*;

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

    public void raiseSalary() {
        if (this instanceof Clerk) {
            salary += 2000; // Clerk gets an increment of 2000
        } else if (this instanceof Programmer) {
            salary += 5000; // Programmer gets an increment of 5000
        } else if (this instanceof Manager) {
            salary += 15000; // Manager gets an increment of 15000
        }
        System.out.println("Salary after increment: " + salary);
    }
}

class Clerk extends Employee {
    public Clerk(String name, int age, double salary) {
        super(name, age, salary, "Clerk");
    }
}

class Programmer extends Employee {
    public Programmer(String name, int age, double salary) {
        super(name, age, salary, "Programmer");
    }
}

class Manager extends Employee {
    public Manager(String name, int age, double salary) {
        super(name, age, salary, "Manager");
    }
}

class Main {
    static List<Employee> employees = new ArrayList<>();
    static int employeeCount = 0;  // Track the number of employees created

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n----------------------------");
            System.out.println("1. Create");
            System.out.println("2. Display");
            System.out.println("3. Raise Salary");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    createEmployee(sc);
                    break;
                case 2:
                    displayEmployees();
                    break;
                case 3:
                    raiseSalary(sc);
                    break;
                case 4:
                    System.out.println("Number of employees created: " + employeeCount);
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    private static void createEmployee(Scanner sc) {
        int subChoice;
        do {
            System.out.println("\nCreate Employee");
            System.out.println("1. Clerk");
            System.out.println("2. Programmer");
            System.out.println("3. Manager");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Enter your choice: ");
            subChoice = sc.nextInt();

            switch (subChoice) {
                case 1:
                    createClerk(sc);
                    break;
                case 2:
                    createProgrammer(sc);
                    break;
                case 3:
                    createManager(sc);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (subChoice != 4);
    }

    private static void createClerk(Scanner sc) {
        System.out.print("Enter name of the Clerk: ");
        String name = sc.next();
        System.out.print("Enter age of the Clerk: ");
        int age = sc.nextInt();
        employees.add(new Clerk(name, age, 20000));  // Predefined salary for Clerk
        employeeCount++;
        System.out.println("Clerk created successfully.");
    }

    private static void createProgrammer(Scanner sc) {
        System.out.print("Enter name of the Programmer: ");
        String name = sc.next();
        System.out.print("Enter age of the Programmer: ");
        int age = sc.nextInt();
        employees.add(new Programmer(name, age, 30000));  // Predefined salary for Programmer
        employeeCount++;
        System.out.println("Programmer created successfully.");
    }

    private static void createManager(Scanner sc) {
        System.out.print("Enter name of the Manager: ");
        String name = sc.next();
        System.out.print("Enter age of the Manager: ");
        int age = sc.nextInt();
        employees.add(new Manager(name, age, 100000));  // Predefined salary for Manager
        employeeCount++;
        System.out.println("Manager created successfully.");
    }

    private static void displayEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees to display.");
        } else {
            for (Employee employee : employees) {
                employee.display();
                System.out.println("--------------------------------");
            }
        }
    }

    private static void raiseSalary(Scanner sc) {
        if (employees.isEmpty()) {
            System.out.println("No employees to update salary.");
            return;
        }

        for (Employee employee : employees)
        {
        	employee.raiseSalary();
        }
    }
}
