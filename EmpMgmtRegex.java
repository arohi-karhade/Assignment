import java.util.*;
import java.util.InputMismatchException;
import java.util.regex.*;

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
	do
	{	
		System.out.print("Enter name: ");
		inputName = sc.nextLine();
		isValidName = ValidateName.validate(inputName);
		if (!isValidName) {
                	System.out.println("Invalid name format. Please ensure the first and last names start with capital letters.");
            	}
	}while(!isValidName);
	
        this.name = inputName;
	
	boolean isValidAge = false;
        while (!isValidAge) {
            System.out.print("Enter age (between 21 and 60): ");
            String inputAge = sc.nextLine();

            isValidAge = ValidateAge.validate(inputAge);
            if (isValidAge) {
                this.age = Integer.parseInt(inputAge);
            } else {
                System.out.println("Invalid age! Age must be a number between 21 and 60.");
            }
        }
	
        

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
	    System.out.println("Enter the employee id of the employee to be deleted:");
	    int id = sc.nextInt();

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

	    System.out.println("Employee with ID " + id + " not found.");
	    return size;
    }


    public abstract void raiseSalary();
}

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

class EmpMgmtRegex {
    static Employee[] emp = new Employee[100];  
    static int pt = 0;  
    static int employeeCount = 0;  
    static int nextEid = 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ch1, ch2;
        
        do {
        System.out.println("\n----------------------------");
        System.out.println("1. Create");
        System.out.println("2. Display");
        System.out.println("3. Raise Salary");
        System.out.println("4. Delete");
        System.out.println("5. Exit");
                    
        ch1 = Menu.readChoice(5);
                 
	   
            switch (ch1) {
                case 1:
                    do {
                    System.out.println("\n----------------------------");
                    System.out.println("1. Clerk");
                    System.out.println("2. Programmer");
                    System.out.println("3. Manager");
                    System.out.println("4. Exit");
                            
                    ch2 = Menu.readChoice(4);

		   
                       	switch (ch2) {
                            case 1:
                                emp[pt++] = new Clerk(nextEid++);
                                employeeCount++;
                                break;
                            case 2:
                                emp[pt++] = new Programmer(nextEid++);
                                employeeCount++;
                                break;
                            case 3:
                                emp[pt++] = new Manager(nextEid++);
                                employeeCount++;
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }

                    } while (ch2 != 4);
                    break;
                case 2:
                    if (employeeCount == 0) {
                        System.out.println("No employees to display.");
                    } else {
                        for (int i = 0; i < pt; i++) {
                            emp[i].display();
                            System.out.println("--------------------------------");
                        }
                    }
                    break;
                case 3:
                    if (employeeCount == 0) {
                        System.out.println("No employees to update salary.");
                    } else {
                        for (int i = 0; i < pt; i++) {
                            emp[i].raiseSalary();
                            emp[i].display();
                            System.out.println("--------------------------------");
                        }
                    }
                    break;
		case 4:
		    int empCtOld = employeeCount;
		    employeeCount = Employee.deleteEmp(emp, employeeCount);
		    if(empCtOld != employeeCount)
		    {
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

class Menu
{
	private static int maxChoice;
	public static int readChoice(int mc)
	{
		Menu.maxChoice = mc;
		while(true)
		{
			System.out.println("Enter choice :- ");
			try
			{
				int choice = new Scanner(System.in).nextInt();
				if(choice<1 || choice>maxChoice)
					throw new InvalidChoiceException();
				return choice;
			}
			catch(InputMismatchException e)
			{	
				System.out.println("Please enter number only!");
			}
			catch(InvalidChoiceException e)
			{	
				e.displayMessage(maxChoice);
			}
		}
	}
}


class AgeException extends Exception
{
	public AgeException()
	{
		super();
	}
	public AgeException(String msg)
	{
		super(msg);
	}
}

class InvalidChoiceException extends RuntimeException
{
	public InvalidChoiceException()
	{
		super();
	}
	public InvalidChoiceException(String msg)
	{
		super(msg);
	}
	public void displayMessage(int maxChoice)
	{
		System.out.println("Please enter choice between 1 and "+maxChoice);
	}
}

class ValidateName
{
	public static boolean validate(String name)
	{
		String nameRegex = "^[A-Z][a-z]* [A-Z][a-z]*$";
		Pattern p = Pattern.compile(nameRegex);
		Matcher m = p.matcher(name);

		if (m.matches()) {
            		return true;
        	} else {
            		return false;
        	}
	}
}

class ValidateAge {
    	public static boolean validate(String age) 
	{
        	String ageRegex = "^(2[1-9]|[3-5][0-9]|60)$";
        	Pattern p = Pattern.compile(ageRegex);
        	Matcher m = p.matcher(age);

        	return m.matches();
    	}
}
