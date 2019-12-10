package group6;

import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 *
 * @author Mimi Opkins with some tweaking from Dave Brown
 */
public class Database {
    //  Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;
    //This is the specification for the printout that I'm doing:
    //each % denotes the start of a new field.
    //The - denotes left justification.
    //The number indicates how wide to make the field.
    //The "s" denotes that it's a string.  All of our output in this test are
    //strings, but that won't always be the case.
    static final String displayFormat="%-5s%-15s%-15s%-15s\n";
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static String DB_URL = "jdbc:mysql://cecs-db01.coe.csulb.edu:3306/cecs323sec5g6"; //?zeroDateTimeBehavior=convertToNull";
//            + "testdb;user=";s
/**
 * Takes the input string and outputs "N/A" if the string is empty or null.
 * @param input The string to be mapped.
 * @return  Either the input string or "N/A" as appropriate.
 */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    public static Boolean login(String user, String pass){//String[] args) {
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to
        //remove that from the connection string.
    //    Scanner in = new Scanner(System.in);
        //System.out.print("Name of the database (not the user account): ");
        //DBNAME = in.nextLine();
    //    System.out.print("Database user name: ");
        USER = user;//in.nextLine();
    //    System.out.print("Database password: ");
        PASS = pass;//in.nextLine();
        //Constructing the database URL connection string
        //DB_URL = DB_URL + DBNAME + "?user="+ USER + "&password=" + PASS;
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER).newInstance();
            

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            //conn = DriverManager.getConnection(DB_URL);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            //conn.setAutoCommit(true);
            
            initialize(conn);
            
            return true;
            //conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
          }
            finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return false;
    }
    
        public static void initialize(Connection conn) {
        try {
            PreparedStatement drop = conn.prepareStatement("DROP TABLE IF EXISTS Customers");
            drop.executeUpdate();
            PreparedStatement create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Customers(" +
                    "cust_id VARCHAR(10) NOT NULL, " +
                    "cust_fname VARCHAR(15) NOT NULL, " +
                    "cust_lname VARCHAR(15) NOT NULL, " +
                    "cust_mid_ini VARCHAR(1), " +
                    "cust_ssn VARCHAR(12), " +
                    "cust_address VARCHAR(30) NOT NULL, " +
                    "zip_code INTEGER(5), " +
                    "cust_email VARCHAR(30), " +
                    "CONSTRAINT pk_Customers PRIMARY KEY (cust_id))");
            create.executeUpdate();
            System.out.println("create completed");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Inventory");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Inventory(" +
                    "VIN VARCHAR(30) NOT NULL, " +
                    "invoice_price VARCHAR(15), " +
                    "retail_price VARCHAR(15), " +
                    "engine_size VARCHAR(5), " +
                    "driving_range INTEGER(12), " +
                    "odometer INTEGER(20), " +
                    "license_plate VARCHAR(7), " +
                    "warranty VARCHAR(30), " +
                    "car_color VARCHAR(15), " +
                    "body_style VARCHAR(15), " +
                    "sunroof VARCHAR(10), " +
                    "leather_interior VARCHAR(10), " +
                    "new_used VARCHAR(3), " +
                    "fuel_type VARCHAR(30), " +
                    "car_make VARCHAR(20), " +
                    "car_model VARCHAR(20), " +
                    "car_year INTEGER(4), " +
                    "CONSTRAINT pk_Inventory PRIMARY KEY (VIN))");
            create.executeUpdate();
            System.out.println("create completed2");

            drop = conn.prepareStatement("DROP TABLE IF EXISTS PhoneNumbers");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS PhoneNumbers(" +
                    "phone_type VARCHAR(10) NOT NULL, " +
                    "phone_number VARCHAR(15) NOT NULL, " +
                    "cust_id VARCHAR(10) NOT NULL, " +
                    "CONSTRAINT pk_PhoneNumbers PRIMARY KEY (cust_id))");
            create.executeUpdate();
            System.out.println("create completed3");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS ZipLocations");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS ZipLocations(" +
                    "zip_code INTEGER(5) NOT NULL, " +
                    "city_name VARCHAR(20), " +
                    "state_name VARCHAR(20), " +
                    "CONSTRAINT pk_ZipLocations PRIMARY KEY (zip_code))");
            create.executeUpdate();
            System.out.println("create completed4");
            
            //fix date format syntax, move foreign key adding to alter statements?
            /*drop = conn.prepareStatement("DROP TABLE IF EXISTS Employees");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Employees(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "employee_fname VARCHAR(15) NOT NULL, " +
                    "employee_lname VARCHAR(15) NOT NULL, " +
                    "employee_mid_ini VARCHAR(1), " +
                    "employee_ssn VARCHAR(12), " +
                    "employee_address VARCHAR(30), " +
                    "zip_code INTEGER(5), " +
                    "employee_email VARCHAR(30), " +
                    "department VARCHAR(15), " +
                    "current_position VARCHAR(15), " +
                    "hire_date DATE(dd.mm.yyyy), " +
                    "unused_vacation_days INTEGER(1), " +
                    "benefits_premium VARCHAR(10), " +
                    "CONSTRAINT pl_Employees PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed5");*/
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Dependents");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Dependents(" +
                    "dependent_fname VARCHAR(20) NOT NULL, " +
                    "dependent_lname VARCHAR(20) NOT NULL, " +
                    "relation VARCHAR(10), " +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "CONSTRAINT pk_Dependents PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed6");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS EmergencyContacts");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS EmergencyContacts(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "emer_cont_fname VARCHAR(20) NOT NULL, " +
                    "emer_cont_lname VARCHAR(20) NOT NULL, " +
                    "emer_cont_address VARCHAR(20), " +
                    "zip_code INTEGER(5), " +
                    "CONSTRAINT pk_EmergencyContacts PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed7");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Colors");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Colors(" +
                    "car_color VARCHAR(15) NOT NULL, " +
                    "CONSTRAINT pk_Colors PRIMARY KEY (car_color))");
            create.executeUpdate();
            System.out.println("create completed8");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS BodyStyle");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS BodyStyles(" +
                    "body_style VARCHAR(15) NOT NULL, " +
                    "CONSTRAINT pk_BodyStyles PRIMARY KEY (body_style))");
            create.executeUpdate();
            System.out.println("create completed9");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Sunroof");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Sunroof(" +
                    "sun_roof VARCHAR(10) NOT NULL, " +
                    "CONSTRAINT pk_Sunroof PRIMARY KEY (sun_roof))");
            create.executeUpdate();
            System.out.println("create completed10");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS LeatherInterior");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS LeatherInterior(" +
                    "leather_interior VARCHAR(10) NOT NULL, " +
                    "CONSTRAINT pk_LeatherInterior PRIMARY KEY (leather_interior))");
            create.executeUpdate();
            System.out.println("create completed11");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS NewUsed");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS NewUsed(" +
                    "new_used VARCHAR(5) NOT NULL, " +
                    "CONSTRAINT pk_NewUsed PRIMARY KEY (new_used))");
            create.executeUpdate();
            System.out.println("create completed12");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS FuelType");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS FuelTypes(" +
                    "fuel_type VARCHAR(30) NOT NULL, " +
                    "CONSTRAINT pk_FuelTypes PRIMARY KEY (fuel_type))");
            create.executeUpdate();
            System.out.println("create completed13");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Makes");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Makes(" +
                    "car_make VARCHAR(15) NOT NULL, " +
                    "CONSTRAINT pk_Makes PRIMARY KEY (car_make))");
            create.executeUpdate();
            System.out.println("create completed14");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Models");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Models(" +
                    "car_model VARCHAR(15) NOT NULL, " +
                    "CONSTRAINT pk_Models PRIMARY KEY (car_model))");
            create.executeUpdate();
            System.out.println("create completed15");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Years");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Years(" +
                    "car_year VARCHAR(15) NOT NULL, " +
                    "CONSTRAINT pk_Years PRIMARY KEY (car_year))");
            create.executeUpdate();
            System.out.println("create completed16");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS States");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS States(" +
                    "state_name VARCHAR(20) NOT NULL, " +
                    "CONSTRAINT pk_States PRIMARY KEY (state_name))");
            create.executeUpdate();
            System.out.println("create completed17");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS SalariedEmployee");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS SalariedEmployees(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "salary Float, " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "CONSTRAINT pk_SalariedEmployees PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed18");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Managers");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Managers(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "salary Float, " +
                    "FOREIGN KEY (salary) REFERENCES SalariedEmployee (salary), " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "CONSTRAINT pk_Managers PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed19");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Technicians");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Technicians(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "salary Float, " +
                    "FOREIGN KEY (salary) REFERENCES SalariedEmployee (salary), " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "CONSTRAINT pk_Technicians PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed20");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Certificates");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Certificates(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "certificate_name VARCHAR(30) NOT NULL, " +
                    "CONSTRAINT pk_Certificates PRIMARY KEY (employee_id, certificate_name))");
            create.executeUpdate();
            System.out.println("create completed21");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS CommissionRateEmployee");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS CommissionRateEmployee(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "commission_rate FLOAT, " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "CONSTRAINT pk_CommissionRateEmployees PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed22");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Salespersons");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Salespersons(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "commission_rate FLOAT, " +
                    "FOREIGN KEY (commission_rate) REFERENCES CommissionRateEmployee (commission_rate), " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "CONSTRAINT pk_Salespersons PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed23");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS SalesTransactions");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS SalesTransactions(" +
                    "VIN VARCHAR(30) NOT NULL, " +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "cust_id VARCHAR (10) NOT NULL, " +
                    "FOREIGN KEY (VIN) REFERENCES Inventory (VIN), " +
                    "FOREIGN KEY (employee_id) REFERENCES Salespersons (employee_id), " + 
                    "FOREIGN KEY (cust_id) REFERENCES Customers (cust_id), " +
                    "transaction_id VARCHAR(20) NOT NULL, " + 
                    "purchase_date DATE() NOT NULL, " +
                    "sale_amt FLOAT NOT NULL, " +
                    "CONSTRAINT pk_SalesTransactions PRIMARY KEY (transaction_id, purchase_date, sale_amt))");
            create.executeUpdate();
            System.out.println("create completed24");
            
            drop = conn.prepareStatement("DROP TABLE IF EXISTS Financing");
            drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Financing(" +
                    "transaction_id VARCHAR(20), " +
                    "FOREIGN KEY (transaction_id) REFERENCES SalesTransactions (transaction_id), " +
                    "monthly_amt FLOAT NOT NULL, " +
                    "date_of_last_payment DATE()" +
                    "CONSTRAINT pk_Financing PRIMARY KEY (transaction_id))");
            create.executeUpdate();
            System.out.println("create completed25");
            
            //String insert = "INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_mid_ini, cust_ssn, cust_address, zip_code, cust_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement posted = conn.prepareStatement("INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_mid_ini, cust_ssn, cust_address, zip_code, cust_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            posted.setString(1, "1234567890");
            posted.setString(2, "Michael");
            posted.setString(3, "Jee");
            posted.setString(4, "D");
            posted.setString(5, "123456789012");
            posted.setString(6, "123 lolipop rd");
            posted.setInt(7, 12345);
            posted.setString(8, "mdj@gmail.com");
            /*final String one = "1234567890", two = "Michael", three = "Jee", four = "D", five = "123456789012", six = "123 lolipop rd", eight = "mdj@gmail.com";
            final int seven = 12345;
            PreparedStatement posted = conn.prepareStatement("INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_mid_ini, cust_ssn, cust_address, zip_code, cust_email) " +
                    "VALUES ('"+one+"', '"+two+"', '"+three+"', '"+four+"', '"+five+"', '"+six+"', '"+seven+"', '"+eight+"')");*/
            posted.executeUpdate();
            System.out.println("insert completed1");
            
            String query = "SELECT * FROM Customers";
            // create the java statement
            Statement st = conn.createStatement();
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            // iterate through the java resultset
            while (rs.next()) {
                String cust_id = rs.getString("cust_id");
                //String firstName = rs.getString("first_name");
                //String lastName = rs.getString("last_name");
                //Date dateCreated = rs.getDate("date_created");
                //boolean isAdmin = rs.getBoolean("is_admin");
                //int numPoints = rs.getInt("num_points");

                // print the results
                System.out.format("%s\n", cust_id);
                //System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints);
            }
            System.out.println("select completed1");
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
    
}//end FirstExample}
