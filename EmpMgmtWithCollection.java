import java.util.*;
import java.util.InputMismatchException;
import java.util.regex.*;

// CEO Singleton class
class CEO extends Employee {
    private static CEO instance = null;

    private CEO(int eid) {
        super(eid, 200000, "CEO");
    }

    public static CEO getInstance(int eid) {
        if (instance == null) {
            instance = new CEO(eid);
        }
        return instance;
    }

    public String getName() {
        return name;
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

    public Employee(int eid, double salary, String designation) {
        Scanner sc = new Scanner(System.in);

        this.eid = eid;

        String inputName;
        boolean isValidName;
        do {
            System.out.print("Enter name: ");
            inputName = sc.nextLine();
            isValidName = NameCheck.validate(inputName);
            if (!isValidName) {
                System.out.println("Invalid name format. Please ensure the first and last names start with capital letters.");
            }
        } while (!isValidName);
        this.name = inputName;

        int inputAge;
        boolean isValidAge;
        do {
            System.out.println("Enter age: ");
            inputAge = sc.nextInt();
            try {
                isValidAge = AgeCheck.validate(inputAge, 21, 60);
            } catch (AgeException e) {
                System.out.println(e.getMessage());
                isValidAge = false;
            }
        } while (!isValidAge);
        this.age = inputAge;

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

    public static void searchEmployee(Map<Integer, Employee> empMap, int id) {
        if (empMap.containsKey(id)) {
            empMap.get(id).display();
        } else {
            System.out.println("No employee found with ID " + id);
        }
    }

    public abstract void raiseSalary();
}

// Employee Types
final class Clerk extends Employee {
    private Clerk(int eid) {
        super(eid, 20000, "Clerk");
    }

    public static Clerk getClerk(int eid) {
        return new Clerk(eid);
    }

    public void raiseSalary() {
        salary += 2000;
    }
}

final class Programmer extends Employee {
    private Programmer(int eid) {
        super(eid, 30000, "Programmer");
    }

    public static Programmer getProgrammer(int eid) {
        return new Programmer(eid);
    }

    public void raiseSalary() {
        salary += 5000;
    }
}

final class Manager extends Employee {
    private Manager(int eid) {
        super(eid, 100000, "Manager");
    }

    public static Manager getManager(int eid) {
        return new Manager(eid);
    }

    public void raiseSalary() {
        salary += 15000;
    }
}

// Factory class to create Employees
abstract class EmployeeFactory {
    private static CEO ceoInstance = null;

    public static Employee createEmployee(int eid, String designation) throws Exception {
        if (designation.equals("CEO")) {
            if (ceoInstance != null) {
                System.out.println("CEO already exists!");
                return null;
            }
            ceoInstance = CEO.getInstance(eid);
            return ceoInstance;
        }

        if (ceoInstance == null && !designation.equals("CEO")) {
            throw new Exception("Cannot create " + designation + " as CEO does not exist.");
        }

        switch (designation) {
            case "Clerk":
                return Clerk.getClerk(eid);
            case "Programmer":
                return Programmer.getProgrammer(eid);
            case "Manager":
                return Manager.getManager(eid);
            default:
                throw new Exception("Invalid designation.");
        }
    }
}

public class EmpMgmtWithCollection {
    static Map<Integer, Employee> empMap = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int ch1, ch2;

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
                                Employee emp = EmployeeFactory.createEmployee(eid, employeeType);

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
                        Iterator<Employee> iterator = empMap.values().iterator();
                        while (iterator.hasNext()) {
                            Employee emp = iterator.next();
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
                    System.out.print("Enter the employee ID to search: ");
                    int searchId = sc.nextInt();
                    Employee.searchEmployee(empMap, searchId);
                    break;
                case 6:
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (ch1 != 6);
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
    public static boolean validate(int inputAge, int minAge, int maxAge) throws AgeException {
        if (inputAge < minAge || inputAge > maxAge) {
            throw new AgeException("Age needs to lie between " + minAge + " and " + maxAge);
        }
        return true; // If age is valid, return true
    }
}

class NameCheck {
    public static boolean validate(String name) {
        String nameRegex = "^[A-Z][a-z]* [A-Z][a-z]*$";
        Pattern p = Pattern.compile(nameRegex);
        Matcher m = p.matcher(name);

        return m.matches();
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
