
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.InputMismatchException;
import java.io.*;

// CEO Singleton class
class CEO extends Employee {
    private static CEO instance = null;

    private CEO(int eid, String name, int age, double salary, String department) {
        super(eid, name, age, salary, department, "CEO");
    }

    public static CEO getInstance(int eid, String name, int age, double salary, String department) {
        if (instance == null) {
            instance = new CEO(eid, name, age, salary, department);
        }
        return instance;
    }
}

// Abstract Employee Class
abstract class Employee {
    protected int eid;
    protected String name;
    protected int age;
    protected double salary;
    protected String department;
    protected String designation;

    public Employee(int eid, String name, int age, double salary, String department, String designation) {
        this.eid = eid;
        this.name = name;
        this.age = age;
        this.salary = salary;
	this.department = department;
        this.designation = designation;
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

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }
}

// Employee Types
final class Clerk extends Employee {
    private Clerk(int eid, String name, int age, double salary, String department) {
        super(eid, name, age, salary, department, "Clerk");
    }

    public static Clerk getClerk(int eid, String name, int age, double salary, String department) {
        return new Clerk(eid, name, age, salary, department);
    }
}

final class Programmer extends Employee {
    private Programmer(int eid, String name, int age, double salary, String dept) {
        super(eid, name,  age, salary, dept, "Programmer");
    }

    public static Programmer getProgrammer(int eid, String name, int age, double salary, String department) {
        return new Programmer(eid, name, age, salary, department);
    }
}

final class Manager extends Employee {
    private Manager(int eid, String name, int age, double salary, String department) {
        super(eid, name, age, salary, department, "Manager");
    }

    public static Manager getManager(int eid, String name, int age, double salary, String department) {
        return new Manager(eid, name, age, salary, department);
    }
}

final class Other extends Employee {
    private Other(int eid, String name, int age, double salary, String department) {
        super(eid, name, age, salary, department, "Other");
    }

    public static Other getOther(int eid, String name, int age, double salary, String department) {
        return new Other(eid, name, age, salary, department);
    }
}

// Factory class to create Employees
abstract class EmployeeFactory {
    private static CEO ceoInstance = null;

    public static Employee createEmployee(int eid, String name, int age, double salary, String department, String designation) throws RuntimeException {
        if (designation.equals("CEO")) {
            if (ceoInstance != null) {
                System.out.println("CEO already exists!");
                return null;
            }
            ceoInstance = CEO.getInstance(eid, name, age, salary, department);
            return ceoInstance;
        }

        if (ceoInstance == null && !designation.equals("CEO")) {
            throw new RuntimeException("Cannot create " + designation + " as CEO does not exist.");
        }

        switch (designation) {
            case "Clerk":
                return Clerk.getClerk(eid, name, age, salary, department);
            case "Programmer":
                return Programmer.getProgrammer(eid, name, age, salary, department);
            case "Manager":
                return Manager.getManager(eid, name, age, salary, department);
	    case "Other":
                return Other.getOther(eid, name, age, salary, department);
            default:
                throw new RuntimeException("Invalid designation.");
        }
    }
}

class DBConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "EmployeeDB";

    private static DBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    // Private constructor for singleton pattern
    private DBConnection() {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
            collection = database.getCollection("Employee");
            System.out.println("Connected to MongoDB successfully!");
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to MongoDB", e);
        }
    }

    // Public method to provide the single instance
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    // Method to get the database
    public MongoDatabase getDatabase() {
        return database;
    }
    
    public MongoCollection<Document> getCollection() {
        return collection;
    }

    // Method to close the connection
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }
}

interface EmpDAO
{
	public void loadEmployeesFromDB();
	public void insertEmployeeIntoDB(Employee emp);
	public void displayEmployeesFromDB(String sortBy);
	public void updateSalaries(int id, double appraisalVal);
	public void deleteEmployeeFromDB(int eid);
	public void searchEmployeeInDB(String column);
}

class EmployeeDbDAO implements EmpDAO {
    //private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public EmployeeDbDAO() {
        //this.database = DBConnection.getInstance().getDatabase();
        this.collection = DBConnection.getInstance().getCollection();
    }

    // Method to get employee documents from MongoDB based on query parameters
    private List<Document> getDocuments(Bson filter) {
        FindIterable<Document> iterable = (filter != null) ? collection.find(filter) : collection.find();

        List<Document> documents = new ArrayList<>();
        for (Document doc : iterable) {
            documents.add(doc);
        }
        return documents;
    }

    // Load all employees from MongoDB
    public void loadEmployeesFromDB() {
        List<Document> employees = getDocuments(null); // Null filter means fetch all
        for (Document doc : employees) {
            Employee emp = EmployeeFactory.createEmployee(
                doc.getInteger("eid"),
                doc.getString("name"),
                doc.getInteger("age"),
                doc.getDouble("salary"),
                doc.getString("department"),
                doc.getString("designation")
            );
        }
        System.out.println("Employees loaded from MongoDB.");
    }

    // Insert Employee into Database
    public void insertEmployeeIntoDB(Employee emp) {
        try {
            Document doc = new Document()
                .append("eid", emp.getEid())
                .append("name", emp.getName())
                .append("age", emp.getAge())
                .append("salary", emp.getSalary())
                .append("department", emp.getDepartment())
                .append("designation", emp.getDesignation());

            collection.insertOne(doc);
            System.out.println("Employee inserted into MongoDB.");
        } catch (Exception e) {
            System.err.println("Error inserting employee: " + e.getMessage());
        }
    }

    // Display Employees from Database
    public void displayEmployeesFromDB(String sortBy) {
        try (MongoCursor<Document> cursor = collection.find().sort(Sorts.ascending(sortBy)).iterator()) {
            if (!cursor.hasNext()) {
                System.out.println("No employees to display.");
                return;
            }

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                System.out.println("ID: " + doc.getInteger("eid") + ", Name: " + doc.getString("name") +
                        ", Age: " + doc.getInteger("age") + ", Salary: " + doc.getDouble("salary") +
                        ", Designation: " + doc.getString("designation") + ", Department: " + doc.getString("department"));
                System.out.println("---------------------------------------------------------------------------------------------------------------------------");
            }
        } catch (Exception e) {
            System.err.println("Error displaying employees: " + e.getMessage());
        }
    }

    // Update Salaries
    public void updateSalaries(int id, double appraisalVal) {
        // Find the employee by ID
        Document employee = collection.find(Filters.eq("eid", id)).first();

        if (employee != null) {
            double currentSalary = employee.getDouble("salary");
            double newSalary = currentSalary * (1 + (appraisalVal / 100));

            // Update salary in MongoDB
            Bson update = Updates.set("salary", newSalary);
            collection.updateOne(Filters.eq("eid", id), update);

            System.out.println("Salary updated for employee ID: " + id);
        } else {
            System.out.println("Employee ID not found.");
        }
    }

    // Delete Employee from Database
    public void deleteEmployeeFromDB(int eid) {
        // Try deleting the employee by ID
        long deletedCount = collection.deleteOne(Filters.eq("eid", eid)).getDeletedCount();

        if (deletedCount > 0) {
            System.out.println("Employee deleted successfully.");
        } else {
            System.out.println("Employee ID not found.");
        }
    }

    // Search Employee by Column
    public void searchEmployeeInDB(String column) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Enter " + column + ": ");
            String input = br.readLine();

            // Determine data type and create filter
            Object filterValue;
            if (column.equals("eid") || column.equals("age")) {
                filterValue = Integer.parseInt(input);
            } else if (column.equals("salary")) {
                filterValue = Double.parseDouble(input);
            } else {
                filterValue = input;
            }

            // Find matching employees
            FindIterable<Document> results = collection.find(Filters.eq(column, filterValue));

            // Check if employees exist
            boolean found = false;
            for (Document doc : results) {
                found = true;
                System.out.println("------------------------------------------------");
                System.out.println("ID: " + doc.getInteger("eid"));
                System.out.println("Name: " + doc.getString("name"));
                System.out.println("Age: " + doc.getInteger("age"));
                System.out.println("Salary: " + doc.getDouble("salary"));
                System.out.println("Department: " + doc.getString("department"));
                System.out.println("Designation: " + doc.getString("designation"));
                System.out.println("------------------------------------------------");
            }

            if (!found) {
                System.out.println("No employee found.");
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error searching employee: " + e.getMessage());
        }
    }
}

// Main class with updated Display logic
public class EmpMgmtMongoDB {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static EmployeeDbDAO emp = new EmployeeDbDAO();
   
    public static void main(String[] args) {
        int ch1, ch2;
        emp.loadEmployeesFromDB();  

        do {
            System.out.println("\n----------------------------");
            System.out.println("1. Create Employee");
            System.out.println("2. Display Employees");
            System.out.println("3. Raise Salary");
            System.out.println("4. Delete Employee");
            System.out.println("5. Search Employee");
            System.out.println("6. Exit");

            ch1 = Menu.readChoice(6, br);

            switch (ch1) {
                case 1:
                    do {
                        System.out.println("\n----------------------------");
                        System.out.println("1. Clerk");
                        System.out.println("2. Programmer");
                        System.out.println("3. Manager");
                        System.out.println("4. CEO");
                        System.out.println("5. Other");
                        System.out.println("6. Exit");

                        ch2 = Menu.readChoice(6, br);

                        try {
                            String[] employeeTypes = {"Clerk", "Programmer", "Manager", "CEO", "Other"};

                            if (ch2 >= 1 && ch2 <= 5) {
                                int eid = CheckEid.readEid(br);
                                String employeeType = employeeTypes[ch2 - 1];
                                String name = NameCheck.read();
                                int age = AgeCheck.read(21, 60);
                                double salary = SalaryCheck.read();
                                String department = DeptCheck.read();
                                
                                Employee employee = EmployeeFactory.createEmployee(eid, name, age, salary, department, employeeType);

                                if (employee != null) {
                                    emp.insertEmployeeIntoDB(employee);
                                    System.out.println("Employee created successfully.");
                                }
                            } else if (ch2 == 6) {
                                break;
                            } else {
                                System.out.println("Invalid choice. Please try again.");
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    } while (ch2 != 6);
                    break;
                case 2:
	        	    int choice = 0;
	
	        	    try {
	            		do {
	                		System.out.println("Select an option:");
	                		System.out.println("1. By ID");
	                		System.out.println("2. By Name");
	               			System.out.println("3. By Age");
	                		System.out.println("4. By Salary");
	                		System.out.println("5. By Department");
	                		System.out.println("6. By Designation");
	                		System.out.println("7. Exit");
	                		choice = Integer.parseInt(br.readLine());
	
	                		switch (choice) {
	                		    case 1:
	                        		emp.displayEmployeesFromDB("eid");
	                    	   		break;
	                    		    case 2:
	                        		emp.displayEmployeesFromDB("name");
	                        		break;
	                    		    case 3:
	                        		emp.displayEmployeesFromDB("age");
	                        		break;
	                    		    case 4:
	                        		emp.displayEmployeesFromDB("salary");
	                        		break;
	                    		    case 5:
	                        		emp.displayEmployeesFromDB("department");
	                        		break;
	                    		    case 6:
	                        		emp.displayEmployeesFromDB("designation");
	                        		break;
	                    		    case 7:
	                        		System.out.println("Exiting...");
	                        		break;
	                    		    default:
	                        		System.out.println("Invalid choice. Please try again.");
	                		}
	            		} while (choice != 7);
	        	    } catch (IOException | NumberFormatException e) {
	            			System.err.println("Error reading input: " + e.getMessage());
	        	    }
	                    break;
                case 3:
	                try {
		        		double appraisalVal;
		        		int eid;
		        		System.out.println("Enter the ID of the employee whose salary has to be appraised: ");
		        		eid = Integer.parseInt(br.readLine());
		        		System.out.println("Enter the percentage of appraisal: ");
		        		appraisalVal = Double.parseDouble(br.readLine());
		
		        		emp.updateSalaries(eid, appraisalVal);
	    		    } catch (IOException | NumberFormatException e) {
	    		    	System.err.println("Error reading input: " + e.getMessage());
	    		    }
	                break;
                case 4:
                    System.out.print("Enter the employee ID to delete: ");
                    int deleteId = -1;
                    BufferedReader br = null;
				    try {
				        br = new BufferedReader(new InputStreamReader(System.in));
				        deleteId = Integer.parseInt(br.readLine());
				    } catch (IOException e) {
				        System.err.println("Error reading input: " + e.getMessage());
				    }
                    emp.deleteEmployeeFromDB(deleteId);
                    break;
                case 5:
			    int searchChoice = 0;
			    br = new BufferedReader(new InputStreamReader(System.in));
			    try {
			        do {
			            System.out.println("\nSearch Employee By:");
			            System.out.println("1. ID");
	        		    System.out.println("2. Name");
	        		    System.out.println("3. Age");
	        		    System.out.println("4. Salary");
	        		    System.out.println("5. Department");
	        		    System.out.println("6. Designation");
	        		    System.out.println("7. Exit");
	        		    System.out.print("Enter your choice: ");
	
	        		    searchChoice = Integer.parseInt(br.readLine()); // Handle IOException
	
	        		    switch (searchChoice) {
	        		        case 1:
	        		            emp.searchEmployeeInDB("eid");
	        		            break;
	        		        case 2:
	        		            emp.searchEmployeeInDB("name");
	        		            break;
	                		case 3:
	                		    emp.searchEmployeeInDB("age");
	                		    break;
	                		case 4:
	                		    emp.searchEmployeeInDB("salary");
	                		    break;
	                		case 5:
	                		    emp.searchEmployeeInDB("department");
	                		    break;
	                		case 6:
	                		    emp.searchEmployeeInDB("designation");
	                		    break;
	                		case 7:
	                		    System.out.println("Exiting search menu...");
	                		    break;
	                		default:
	                		    System.out.println("Invalid choice. Please try again.");
	            		    }
	        		} while (searchChoice != 7);
	    		} catch (IOException | NumberFormatException e) {
	        		System.err.println("Error reading input: " + e.getMessage());
	    		}
	    		break;
                case 6:
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (ch1 != 6);

	DBConnection.getInstance().closeConnection();
    }
}

class Menu {
    private static int maxChoice;

    public static int readChoice(int mc, BufferedReader br) {
        Menu.maxChoice = mc;
        while (true) {
            System.out.println("Enter choice :- ");
            try {
                int choice = Integer.parseInt(br.readLine());
                if (choice < 1 || choice > maxChoice)
                    throw new InvalidChoiceException();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Please enter number only!");
            } catch (InvalidChoiceException e) {
                e.displayMessage(maxChoice);
            } catch (IOException e) {
                System.err.println("Error taking input: " + e.getMessage());
            }
        }
    }
}

class CheckEid {
    public static int readEid(BufferedReader br) {
        int eid = -1;
        
        try {
            while (true) {
                System.out.print("Enter Employee ID: ");
                String input = br.readLine();
                
                try {
                    eid = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a valid numeric Employee ID.");
                    continue;  
                }

                if (!eidExists(eid)) {
                    break; 
                } else {
                    System.out.println("Error: Employee ID already exists. Please enter a different ID.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return eid;
    }

    private static boolean eidExists(int eid) {
    	MongoCollection<Document> collection = DBConnection.getInstance().getCollection();
        try {
            long count = collection.countDocuments(Filters.eq("eid", eid));
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

class DeptCheck {
    public static String read() {
        Scanner scanner = new Scanner(System.in);
        List<String> departments = Arrays.asList("HR", "QA", "IT", "Sales", "Marketing");
        String dept;

        while (true) {
            System.out.print("Enter Department: ");
            dept = scanner.nextLine().trim(); // Trim to remove extra spaces

            if (departments.contains(dept)) {
                return dept;
            } else {
                System.out.println("Invalid department. Please enter one of: " + departments);
            }
        }
    }
}

class AgeException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AgeException() {
        super();
    }

    public AgeException(String msg) {
        super(msg);
    }
}

class InvalidChoiceException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

/*
create an interface EmpDAO (DAO- Data Access Object)
EmpDB implements EmpDAO
Write all the insert, display, search, remove, appraisal methods inside the EmpDB
Create only 1 object of EmpDB in main method and call all methods on that object
Basically in main method it should be very simple and hide all implementation details
Have a class for starting and closing connection to db.
For creating connection have only a singleton object so only one connection is created and close
Call start method in each method of EmpDB class to use the con object
*/
