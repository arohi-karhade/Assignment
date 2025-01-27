import java.util.*;
import java.util.InputMismatchException;
import java.util.regex.*;
import java.io.*;

// CEO Singleton class
class CEO extends Employee {
    private static CEO instance = null;

    private CEO(int eid, String name, int age, double salary) {
        super(eid, name, age, salary, "CEO");
    }

    public static CEO getInstance(int eid, String name, int age, double salary) {
        if (instance == null) {
            instance = new CEO(eid, name, age, salary);
        }
        return instance;
    }

    public void raiseSalary() {
        salary += 30000;
    }
}

// Abstract Employee Class
abstract class Employee {
    protected int eid;
    protected String name;
    protected int age;
    protected double salary;
    protected String designation;

    public Employee(int eid, String name, int age, double salary, String designation) {
        Scanner sc = new Scanner(System.in);

        this.eid = eid;
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.designation = designation;
    }

    public void display() {
        System.out.println("Employee Id: " + eid);
        System.out.println("Employee name: " + name);
        System.out.println("Employee age: " + age);
        System.out.println("Employee salary: " + salary);
        System.out.println("Employee designation: " + designation);
    }

    //Getter methods
    public int getEid() {
        return eid;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    public String getDesignation() {
        return designation;
    }

    public static void deleteEmp(Map<Integer, Employee> empMap, int id) {
        if (empMap.containsKey(id)) {
            empMap.get(id).display();
            System.out.print("Do you want to delete this employee? (Y/N): ");
            char ch = new Scanner(System.in).next().charAt(0);

            if (ch == 'Y' || ch == 'y') {
                empMap.remove(id);
                System.out.println("Employee with ID " + id + " deleted.");
            } else {
                System.out.println("Employee deletion canceled.");
            }
        } else {
            System.out.println("No employee found with ID " + id);
        }
    }

    public static void searchEmployee(Map<Integer, Employee> empMap) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nSearch Options:");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Designation");
        System.out.println("3. Search by Name");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter Employee ID: ");
                int id = sc.nextInt();
                if (empMap.containsKey(id)) {
                    empMap.get(id).display();
                } else {
                    System.out.println("No employee found with ID " + id);
                }
                break;
            case 2:
                System.out.print("Enter Designation: ");
                String designation = sc.nextLine();
                boolean found = false;
                for (Employee emp : empMap.values()) {
                    if (emp.designation.equalsIgnoreCase(designation)) {
                        emp.display();
                        System.out.println("--------------------------------");
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("No employees found with designation " + designation);
                }
                break;
            case 3:
                System.out.print("Enter Name: ");
                String name = sc.nextLine();
                found = false;
                for (Employee emp : empMap.values()) {
                    if (emp.name.equalsIgnoreCase(name)) {
                        emp.display();
                        System.out.println("--------------------------------");
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("No employees found with name " + name);
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public abstract void raiseSalary();
}

// Comparators for sorting
class IdSorter implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return Integer.compare(e1.eid, e2.eid);
    }
}

class NameSorter implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return e1.name.compareToIgnoreCase(e2.name);
    }
}

class AgeSorter implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return Integer.compare(e1.age, e2.age);
    }
}

class SalarySorter implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return Double.compare(e1.salary, e2.salary);
    }
}

class DesignationSorter implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return e1.designation.compareToIgnoreCase(e2.designation);
    }
}

// Employee Types
final class Clerk extends Employee {
    private Clerk(int eid, String name, int age, double salary) {
        super(eid, name, age, salary, "Clerk");
    }

    public static Clerk getClerk(int eid, String name, int age, double salary) {
        return new Clerk(eid, name, age, salary);
    }

    public void raiseSalary() {
        salary += 2000;
    }
}

final class Programmer extends Employee {
    private Programmer(int eid, String name, int age, double salary) {
        super(eid, name,  age, salary, "Programmer");
    }

    public static Programmer getProgrammer(int eid, String name, int age, double salary) {
        return new Programmer(eid, name, age, salary);
    }

    public void raiseSalary() {
        salary += 5000;
    }
}

final class Manager extends Employee {
    private Manager(int eid, String name, int age, double salary) {
        super(eid, name, age, salary, "Manager");
    }

    public static Manager getManager(int eid, String name, int age, double salary) {
        return new Manager(eid, name, age, salary);
    }

    public void raiseSalary() {
        salary += 15000;
    }
}

// Factory class to create Employees
abstract class EmployeeFactory {
    private static CEO ceoInstance = null;

    public static Employee createEmployee(int eid, String name, int age, double salary, String designation) throws RuntimeException {
        if (designation.equals("CEO")) {
            if (ceoInstance != null) {
                System.out.println("CEO already exists!");
                return null;
            }
            ceoInstance = CEO.getInstance(eid, name, age, salary);
            return ceoInstance;
        }

        if (ceoInstance == null && !designation.equals("CEO")) {
            throw new RuntimeException("Cannot create " + designation + " as CEO does not exist.");
        }

        switch (designation) {
            case "Clerk":
                return Clerk.getClerk(eid, name, age, salary);
            case "Programmer":
                return Programmer.getProgrammer(eid, name, age, salary);
            case "Manager":
                return Manager.getManager(eid, name, age, salary);
            default:
                throw new RuntimeException("Invalid designation.");
        }
    }
}

// Main class with updated Display logic
public class EmpMgmtFile {
    static Map<Integer, Employee> empMap = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int ch1, ch2;
	loadEmployeesFromFile();

        do {
            System.out.println("\n----------------------------");
            System.out.println("1. Create Employee");
            System.out.println("2. Display Employees");
            System.out.println("3. Raise Salary");
            System.out.println("4. Delete Employee");
            System.out.println("5. Search Employee");
            System.out.println("6. Exit");

            ch1 = Menu.readChoice(6);

            switch (ch1) {
                case 1:
                    do {
                        System.out.println("\n----------------------------");
                        System.out.println("1. Clerk");
                        System.out.println("2. Programmer");
                        System.out.println("3. Manager");
                        System.out.println("4. CEO");
                        System.out.println("5. Exit");

                        ch2 = Menu.readChoice(5);

                        try {
                            String[] employeeTypes = {"Clerk", "Programmer", "Manager", "CEO"};

                            if (ch2 >= 1 && ch2 <= 4) {
                                System.out.print("Enter Employee ID: ");
                                int eid = sc.nextInt();

                                if (empMap.containsKey(eid)) {
                                    System.out.println("Employee ID already exists!");
                                    continue;
                                }

                                String employeeType = employeeTypes[ch2 - 1];
				String name = NameCheck.read();
				int age = AgeCheck.read(21,60);
				double salary = SalaryCheck.read();
                                Employee emp = EmployeeFactory.createEmployee(eid, name, age, salary, employeeType);

                                if (emp != null) {
                                    empMap.put(eid, emp);
                                    System.out.println("Employee created successfully.");
                                }
                            } else if (ch2 == 5) {
                                break;
                            } else {
                                System.out.println("Invalid choice. Please try again.");
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    } while (ch2 != 5);
                    break;
                case 2:
                    if (empMap.isEmpty()) {
                        System.out.println("No employees to display.");
                    } else {
                        System.out.println("\nDisplay Options:");
                        System.out.println("1. Sort by ID");
                        System.out.println("2. Sort by Name");
                        System.out.println("3. Sort by Age");
                        System.out.println("4. Sort by Salary");
                        System.out.println("5. Sort by Designation");

                        int sortChoice = Menu.readChoice(5);

                        List<Employee> empList = new ArrayList<>(empMap.values());

                        switch (sortChoice) {
                            case 1:
                                empList.sort(new IdSorter());
                                break;
                            case 2:
                                empList.sort(new NameSorter());
                                break;
                            case 3:
                                empList.sort(new AgeSorter());
                                break;
                            case 4:
                                empList.sort(new SalarySorter());
                                break;
                            case 5:
                                empList.sort(new DesignationSorter());
                                break;
                            default:
                                System.out.println("Invalid choice.");
                                continue;
                        }

                        for (Employee emp : empList) {
                            emp.display();
                            System.out.println("--------------------------------");
                        }
                    }
                    break;
                case 3:
                    if (empMap.isEmpty()) {
                        System.out.println("No employees to update salary.");
                    } else {
                        Iterator<Employee> iterator = empMap.values().iterator();
                        while (iterator.hasNext()) {
                            Employee emp = iterator.next();
                            emp.raiseSalary();
                            emp.display();
                            System.out.println("--------------------------------");
                        }
                    }
                    break;
                case 4:
                    System.out.print("Enter the employee ID to delete: ");
                    int deleteId = sc.nextInt();
                    Employee.deleteEmp(empMap, deleteId);
                    break;
                case 5:
                    Employee.searchEmployee(empMap);
                    break;
                case 6:
		    saveEmployees();
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (ch1 != 6);
    }

    //File functions
    private static void loadEmployeesFromFile() {
        String filePath = "employee.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Split the line by comma
                int eid = Integer.parseInt(parts[0]);
                String name = parts[1];
                int age = Integer.parseInt(parts[2]);
                double salary = Double.parseDouble(parts[3]);
                String designation = parts[4];

                try {
                	// Create an Employee object
                	Employee emp = EmployeeFactory.createEmployee(eid, name, age, salary, designation);

                	// Add the Employee object to the HashMap
                	empMap.put(eid, emp);
            	} catch (Exception ex) {
            	    System.err.println("Error creating employee: " + ex.getMessage());
            	}
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static void saveEmployees() {
	String filePath = "employee.csv";

	try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<Integer, Employee> entry : empMap.entrySet()) {
                Employee emp = entry.getValue();
                String line = emp.getEid() + "," + emp.getName() + "," + emp.getAge() + "," + emp.getSalary() + "," + emp.getDesignation();
                bw.write(line);
                bw.newLine(); 
            }
            System.out.println("Data written to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}

class Menu {
    private static int maxChoice;

    public static int readChoice(int mc) {
        Menu.maxChoice = mc;
        while (true) {
            System.out.println("Enter choice :- ");
            try {
                int choice = new Scanner(System.in).nextInt();
                if (choice < 1 || choice > maxChoice)
                    throw new InvalidChoiceException();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Please enter number only!");
            } catch (InvalidChoiceException e) {
                e.displayMessage(maxChoice);
            }
        }
    }
}

class AgeCheck {
    public static int read(int minAge, int maxAge) throws AgeException {
	Scanner scanner = new Scanner(System.in);
	int age;
	while(true)
	{
		try
		{
			System.out.print("Enter Age: ");
            		age = scanner.nextInt();
		
			if (age < minAge || age > maxAge) {
            			throw new AgeException("Age needs to lie between " + minAge + " and " + maxAge);
        		}
			else
				break;
		}
		catch (InputMismatchException e) {
                	System.out.println("Please enter a valid number.");
                	scanner.nextLine();
            	} catch (AgeException e) {
                	System.out.println(e.getMessage());
            	}
	}
        return age;
    }
}

class NameCheck {
    public static String read() {
	Scanner scanner = new Scanner(System.in);
        String name;
	String nameRegex = "^[A-Z][a-z]* [A-Z][a-z]*$";
	while (true) {
            System.out.print("Enter Name: ");
            name = scanner.nextLine();
		
            if (name.matches(nameRegex)) {
                break;
            } else {
                System.out.println("Invalid name. Please enter two words, each starting with a capital letter.");
            }
	}

        return name;
    }
}

class SalaryCheck {
    public static double read(){
	Scanner scanner = new Scanner(System.in);
	double sal;
	while(true)
	{
		try
		{
			System.out.print("Enter Salary: ");
            		sal = scanner.nextDouble();
		
			if (sal<0) {
            			throw new NullPointerException("Salary cannot be less than 0");
        		}
			else
				break;
		}
		catch (NullPointerException e) {
                	System.out.println(e.getMessage());
            	}
	}
        return sal;
    }
}

class AgeException extends Exception {
    public AgeException() {
        super();
    }

    public AgeException(String msg) {
        super(msg);
    }
}

class InvalidChoiceException extends RuntimeException {
    public InvalidChoiceException() {
        super();
    }

    public InvalidChoiceException(String msg) {
        super(msg);
    }

    public void displayMessage(int maxChoice) {
        System.out.println("Enter valid choice between 1 and " + maxChoice);
    }
}