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

    //public void display() {
    //    System.out.println("Employee Id: " + eid);
    //    System.out.println("Employee designation: " + designation);
    //    System.out.println("Employee salary: " + salary);
    //}
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

        int inputAge, minAge = 21, maxAge = 60;
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

    public static int deleteEmp(Employee emp[], int size) {
	    Scanner sc = new Scanner(System.in);
	    int id = IdCheck.validateId(emp, size);	    

	    for (int i = 0; i < size; i++) {
	        if (emp[i].eid == id) {
	            emp[i].display();  
	            System.out.print("Do you want to delete this employee? (Y/N): ");
	            char ch = sc.next().charAt(0);

	            if (ch == 'Y' || ch == 'y') {
	                for (int j = i; j < size - 1; j++) {
	                    emp[j] = emp[j + 1];  
	                }
	
	                emp[size - 1] = null;  
	                System.out.println("Employee with ID " + id + " deleted.");
	                return size - 1;
	            } else {
	                System.out.println("Employee deletion canceled.");
	                return size;  
	            }
	        }
	    }
	    return size;
    }

    public abstract void raiseSalary();
}

// Employee Types
final class Clerk extends Employee {
    public Clerk(int eid) {
        super(eid, 20000, "Clerk");
    }

    public void raiseSalary() {
        salary += 2000;
    }
}

final class Programmer extends Employee {
    public Programmer(int eid) {
        super(eid, 30000, "Programmer");
    }

    public void raiseSalary() {
        salary += 5000;
    }
}

final class Manager extends Employee {
    public Manager(int eid) {
        super(eid, 100000, "Manager");
    }

    public void raiseSalary() {
        salary += 15000;
    }
}

// Factory class to create Employees
class EmployeeFactory {
    private static CEO ceoInstance = null;

    // Factory method to create Employees
    public static Employee createEmployee(int eid, String designation) throws Exception {
	if (designation.equals("CEO")) {
            if (ceoInstance != null) {
                System.out.println("CEO already exists!");
                return null; // Don't create a new CEO, return null
            }
            ceoInstance = CEO.getInstance(eid);
            return ceoInstance;  // Return the existing or newly created CEO
        }

        if (ceoInstance == null && !designation.equals("CEO")) {
            throw new Exception("Cannot create " + designation + " as CEO does not exist.");
        }

        switch (designation) {
            case "Clerk":
                return new Clerk(eid);
            case "Programmer":
                return new Programmer(eid);
            case "Manager":
                return new Manager(eid);
            default:
                throw new Exception("Invalid designation.");
        }
    }
}

// Main Application
public class DesignPatterns {
    static Employee[] emp = new Employee[100];
    static int pt = 0;
    static int employeeCount = 0;
    static int nextEid = 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ch1, ch2;

        do {
            System.out.println("\n----------------------------");
            System.out.println("1. Create Employee");
            System.out.println("2. Display Employees");
            System.out.println("3. Raise Salary");
            System.out.println("4. Delete Employee");
            System.out.println("5. Exit");

            ch1 = Menu.readChoice(5);

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
			
			    if (pt == 0 && ch2 == 4) {
                                emp[pt] = EmployeeFactory.createEmployee(nextEid, "CEO");
                                nextEid++;
                                pt++;
                                employeeCount++;
                            }
			    else if (ch2 >= 1 && ch2 <= 3) {
			        String employeeType = employeeTypes[ch2 - 1];  
			        emp[pt] = EmployeeFactory.createEmployee(nextEid, employeeType);  
			        nextEid++;  
			        pt++;  
			        employeeCount++;  
			    } 	else if(ch2 == 4) {
				System.out.println("CEO already exists!");
			    }	else if (ch2 == 5) {
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
                    if (employeeCount == 0) {
	        	System.out.println("No employees to display.");
		    } 
		    else {
        		for (int i = 0; i < pt; i++) {
        	    		if (emp[i] != null) {
        	        		emp[i].display();
        	        		System.out.println("--------------------------------");
        	    		}
        		}
    		    }
                    break;
                case 3:
                    if (employeeCount == 0) {
                        System.out.println("No employees to update salary.");
                    } else {
                        for (int i = 0; i < pt; i++) {
			    if(emp[i] != null) {
                            	emp[i].raiseSalary();
                            	emp[i].display();
                            	System.out.println("--------------------------------");
			    }
                        }
                    }
                    break;
                case 4:
                    int empCtOld = employeeCount;
                    employeeCount = Employee.deleteEmp(emp, employeeCount);
                    if (empCtOld != employeeCount) {
                        pt--;
                    }
                    break;
                case 5:
                    System.out.println("Number of employees created: " + employeeCount);
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (ch1 != 5);

        sc.close();
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

class IdCheck{
	public static int validateId(Employee emp[], int size){
	    Scanner sc = new Scanner(System.in);
	    int id = -1;
	    while (true) {
	    	System.out.println("Enter the employee id of the employee to be deleted:");
	        id = sc.nextInt();
        
	        boolean idExists = false;
	        for (int i = 0; i < size; i++) {	
	            if (emp[i] != null && emp[i].eid == id) {
			idExists = true;
	                break;
	            }
	        }

	        if (!idExists) {
	            System.out.println("Not valid id. Please enter a valid employee ID.");
	        } else {
	            break;
	        }
	    }
	    return id;
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
        System.out.println("Please enter choice between 1 and " + maxChoice);
    }
}
