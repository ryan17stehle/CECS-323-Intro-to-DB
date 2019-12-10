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
    
    public static Boolean Database(String user, String pass, int choice, Customer cust){//String[] args) {
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
            
            System.out.println(choice);
            if(choice == 0)
            {
                //initialize/execute DDL&DML
                DDL(conn);
                DML(conn);
            }
            else if(choice == -1)
            {
                //add customer
                AddCust(conn, cust);
            }
            else
                //do a select query
                Query(conn, choice);
            
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
    
    public static void DDL(Connection conn) {
        try {
            //PreparedStatement drop = conn.prepareStatement("DROP TABLE IF EXISTS Customers");
            //drop.executeUpdate();
            PreparedStatement create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Customers(" +
                    "cust_id VARCHAR(10) NOT NULL, " +
                    "cust_fname VARCHAR(15) NOT NULL, " +
                    "cust_lname VARCHAR(15) NOT NULL, " +
                    "cust_mid_ini VARCHAR(1), " +
                    "cust_ssn VARCHAR(12), " +
                    "cust_address VARCHAR(30) NOT NULL, " +
                    "zip_code VARCHAR(5), " +
                    "cust_email VARCHAR(30), " +
                    "CONSTRAINT pk_Customers PRIMARY KEY (cust_id))");
            create.executeUpdate();
            System.out.println("create completed");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Inventory");
            //drop.executeUpdate();
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

            //drop = conn.prepareStatement("DROP TABLE IF EXISTS PhoneNumbers");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS PhoneNumbers(" +
                    "phone_type VARCHAR(10) NOT NULL, " +
                    "phone_number VARCHAR(15) NOT NULL, " +
                    "cust_id VARCHAR(10) NOT NULL, " +
                    "CONSTRAINT pk_PhoneNumbers PRIMARY KEY (cust_id))");
            create.executeUpdate();
            System.out.println("create completed3");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS ZipLocations");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS ZipLocations(" +
                    "zip_code VARCHAR(5) NOT NULL, " +
                    "city_name VARCHAR(20), " +
                    "state_name VARCHAR(20), " +
                    "CONSTRAINT pk_ZipLocations PRIMARY KEY (zip_code))");
            create.executeUpdate();
            System.out.println("create completed4");
            
            //fix date format syntax, move foreign key adding to alter statements?
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Employees");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Employees(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "employee_fname VARCHAR(15) NOT NULL, " +
                    "employee_lname VARCHAR(15) NOT NULL, " +
                    "employee_mid_ini VARCHAR(1), " +
                    "employee_ssn VARCHAR(12), " +
                    "employee_address VARCHAR(30), " +
                    "zip_code VARCHAR(5), " +
                    "employee_email VARCHAR(30), " +
                    "department VARCHAR(15), " +
                    "current_position VARCHAR(15), " +
                    "hire_date DATE, " +
                    "unused_vacation_days INTEGER(1), " +
                    "benefits_premium VARCHAR(10), " +
                    "CONSTRAINT pl_Employees PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed5");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Dependents");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Dependents(" +
                    "dependent_fname VARCHAR(20) NOT NULL, " +
                    "dependent_lname VARCHAR(20) NOT NULL, " +
                    "relation VARCHAR(10), " +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "CONSTRAINT pk_Dependents PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed6");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS EmergencyContacts");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS EmergencyContacts(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "emer_cont_fname VARCHAR(20) NOT NULL, " +
                    "emer_cont_lname VARCHAR(20) NOT NULL, " +
                    "emer_cont_address VARCHAR(20), " +
                    "zip_code VARCHAR(5), " +
                    "CONSTRAINT pk_EmergencyContacts PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed7");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Colors");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Colors(" +
                    "car_color VARCHAR(15) NOT NULL, " +
                    "CONSTRAINT pk_Colors PRIMARY KEY (car_color))");
            create.executeUpdate();
            System.out.println("create completed8");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS BodyStyle");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS BodyStyles(" +
                    "body_style VARCHAR(15) NOT NULL, " +
                    "CONSTRAINT pk_BodyStyles PRIMARY KEY (body_style))");
            create.executeUpdate();
            System.out.println("create completed9");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Sunroof");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Sunroof(" +
                    "sun_roof VARCHAR(10) NOT NULL, " +
                    "CONSTRAINT pk_Sunroof PRIMARY KEY (sun_roof))");
            create.executeUpdate();
            System.out.println("create completed10");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS LeatherInterior");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS LeatherInterior(" +
                    "leather_interior VARCHAR(10) NOT NULL, " +
                    "CONSTRAINT pk_LeatherInterior PRIMARY KEY (leather_interior))");
            create.executeUpdate();
            System.out.println("create completed11");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS NewUsed");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS NewUsed(" +
                    "new_used VARCHAR(5) NOT NULL, " +
                    "CONSTRAINT pk_NewUsed PRIMARY KEY (new_used))");
            create.executeUpdate();
            System.out.println("create completed12");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS FuelType");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS FuelTypes(" +
                    "fuel_type VARCHAR(30) NOT NULL, " +
                    "CONSTRAINT pk_FuelTypes PRIMARY KEY (fuel_type))");
            create.executeUpdate();
            System.out.println("create completed13");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Makes");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Makes(" +
                    "car_make VARCHAR(15) NOT NULL, " +
                    "CONSTRAINT pk_Makes PRIMARY KEY (car_make))");
            create.executeUpdate();
            System.out.println("create completed14");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Models");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Models(" +
                    "car_model VARCHAR(15) NOT NULL, " +
                    "CONSTRAINT pk_Models PRIMARY KEY (car_model))");
            create.executeUpdate();
            System.out.println("create completed15");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Years");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Years(" +
                    "car_year VARCHAR(15) NOT NULL, " +
                    "CONSTRAINT pk_Years PRIMARY KEY (car_year))");
            create.executeUpdate();
            System.out.println("create completed16");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS States");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS States(" +
                    "state_name VARCHAR(20) NOT NULL, " +
                    "CONSTRAINT pk_States PRIMARY KEY (state_name))");
            create.executeUpdate();
            System.out.println("create completed17");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS SalariedEmployee");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS SalariedEmployees(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "salary Float NOT NULL, " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "CONSTRAINT pk_SalariedEmployees PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed18");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Managers");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Managers(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "salary Float, " +
                    "FOREIGN KEY (salary) REFERENCES SalariedEmployees (salary), " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "CONSTRAINT pk_Managers PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed19");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Technicians");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Technicians(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "salary Float, " +
                    "FOREIGN KEY (salary) REFERENCES SalariedEmployees (salary), " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "CONSTRAINT pk_Technicians PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed20");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Certificates");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Certificates(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "certificate_name VARCHAR(30) NOT NULL, " +
                    "CONSTRAINT pk_Certificates PRIMARY KEY (employee_id, certificate_name))");
            create.executeUpdate();
            System.out.println("create completed21");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS CommissionRateEmployee");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS CommissionRateEmployee(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "commission_rate FLOAT, " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "CONSTRAINT pk_CommissionRateEmployees PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed22");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Salespersons");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Salespersons(" +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "commission_rate FLOAT, " +
                    "FOREIGN KEY (commission_rate) REFERENCES CommissionRateEmployee (commission_rate), " +
                    "FOREIGN KEY (employee_id) REFERENCES Employees (employee_id), " +
                    "CONSTRAINT pk_Salespersons PRIMARY KEY (employee_id))");
            create.executeUpdate();
            System.out.println("create completed23");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS SalesTransactions");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS SalesTransactions(" +
                    "VIN VARCHAR(30) NOT NULL, " +
                    "employee_id VARCHAR(10) NOT NULL, " +
                    "cust_id VARCHAR (10), " +
                    "FOREIGN KEY (VIN) REFERENCES Inventory (VIN), " +
                    "FOREIGN KEY (employee_id) REFERENCES Salespersons (employee_id), " + 
                    "FOREIGN KEY (cust_id) REFERENCES Customers (cust_id), " +
                    "transaction_id VARCHAR(20) NOT NULL, " + 
                    "purchase_date DATE NOT NULL, " +
                    "sale_amt Float NOT NULL, " +
                    "CONSTRAINT pk_SalesTransactions PRIMARY KEY (transaction_id, purchase_date, sale_amt))");
            create.executeUpdate();
            System.out.println("create completed24");
            
            //drop = conn.prepareStatement("DROP TABLE IF EXISTS Financing");
            //drop.executeUpdate();
            create = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Financing(" +
                    "transaction_id VARCHAR(20), " +
                    "FOREIGN KEY (transaction_id) REFERENCES SalesTransactions (transaction_id), " +
                    "monthly_amt Float NOT NULL, " +
                    "date_of_last_payment DATE, " +
                    "CONSTRAINT pk_Financing PRIMARY KEY (transaction_id))");
            create.executeUpdate();
            System.out.println("create completed25");
            
            String sql;
            Statement alter;
            
            /*String sql = "ALTER TABLE Inventory " +
                    "ADD CONSTRAINT Inventory_Colors_fk " +
                    "FOREIGN KEY (car_color) " +
                    "REFERENCES Colors (car_color)";
            Statement alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed");
            
            sql = "ALTER TABLE Inventory " +
                    "ADD CONSTRAINT Inventory_BodyStyles_fk " +
                    "FOREIGN KEY (body_style) " +
                    "REFERENCES BodyStyles (body_style)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed2");*/
            
            sql = "ALTER TABLE Inventory " +
                    "ADD CONSTRAINT Inventory_Sunroof_fk " +
                    "FOREIGN KEY (sun_roof) " +
                    "REFERENCES Sunroof (sun_roof)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed3");
            
            sql = "ALTER TABLE Inventory " +
                    "ADD CONSTRAINT Inventory_LeatherInterior_fk " +
                    "FOREIGN KEY (leather_inerior) " +
                    "REFERENCES LeatherInterior (leather_interior)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed4");
            
            sql = "ALTER TABLE Inventory " +
                    "ADD CONSTRAINT Inventory_NewUsed_fk " +
                    "FOREIGN KEY (new_used) " +
                    "REFERENCES NewUsed (new_used)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed5");
            
            sql = "ALTER TABLE Inventory " + 
                    "ADD CONSTRAINT Inventory_FuelTypes_fk " +
                    "FOREIGN KEY (fuel_type) " +
                    "REFERENCES FuelTypes (fuel_type)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed6");
            
            sql = "ALTER TABLE Inventory " +
                    "ADD CONSTRAINT Inventory_Makes_fk " +
                    "FOREIGN KEY (car_make) " +
                    "REFERENCES Makes (car_make)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed7");
            
            sql = "ALTER TABLE Inventory " +
                    "ADD CONSTRAINT Inventory_Models_fk " +
                    "FOREIGN KEY (car_model) " +
                    "REFERENCES Models (car_model)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed8");
            
            sql = "ALTER TABLE Inventory " +
                    "ADD CONSTRAINT Inventory_Years_fk " +
                    "FOREIGN KEY (car_year) " +
                    "REFERENCES Years (car_year)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed9");
            
            sql = "ALTER TABLE ZipLocations " + 
                    "ADD CONSTRAINT ZipLocations_States_fk " + 
                    "FOREIGN KEY (state_name) " + 
                    "REFERENCES States (state_name)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed10");
            
            sql = "ALTER TABLE Dependents ADD CONSTRAINT Dependents_Employees_fk FOREIGN KEY (employee_id) REFERENCES Employees (employee_id)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed11");
            
            sql = "ALTER TABLE CUSTOMERS ADD CONSTRAINT Customers_ZipLocations_fk FOREIGN KEY (zip_code) " +
                    "REFERENCES ZipLocations (zip_code)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed12");
            
            sql = "ALTER TABLE Employees ADD CONSTRAINT Employees_ZipLocations_fk FOREIGN KEY (zip_code) " + 
                    "REFERENCES ZipLocations (zip_code)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed13");
            
            sql = "ALTER TABLE EmergencyContacts ADD CONSTRAINT EmergencyContacts_ZipLocations_fk FOREIGN KEY (zip_code) " +
                    "REFERENCES ZipLocations (zip_code)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed14");
            
            sql = "ALTER TABLE EmergencyContacts CONSTRAINT EmergencyContacts_Employees_fkn FOREIGN KEY (employee_id) " +
                    "REFERENCES Employees (employee_id)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed15");
           
            sql = "ALTER TABLE PhoneNumbers " +
                    "ADD CONSTRAINT PhoneNumbers_Customers_fk " +
                    "FOREIGN KEY (cust_id) " +
                    "REFERENCES Customers (cust_id)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed16");
            
            sql = "ALTER TABLE PhoneNumbers " +
                    "ADD CONSTRAINT PhoneNumbers_Employees_fk " +
                    "FOREIGN KEY (employee_id) " +
                    "REFERENCES Employees (employee_id)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed17");
            
            sql = "ALTER TABLE PhoneNumbers " +
                    "ADD CONSTRAINT PhoneNumbers_EmergencyContacts_fk " +
                    "FOREIGN KEY (emer_cont_id) " +
                    "REFERENCES EmergencyContacts (emer_cont_id)";
            alter = conn.createStatement();
            alter.execute(sql);
            System.out.println("alter completed18");
            
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
    
    public static void DML(Connection conn) {
        try {
            Statement insert = conn.createStatement();
            // customers
            insert.executeUpdate("INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_address) VALUES (cust_1, \"John\", \"Wayne\", \"123 Bird Lane\"");
            insert.executeUpdate("INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_address) VALUES (cust_2, \"Bruce\", \"Lee\", \"189 Santa Circle\"");
            insert.executeUpdate("INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_address) VALUES (cust_3, \"Andy\", \"Dalton\", \"416 Chevy Hill\")");
            insert.executeUpdate("INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_address) VALUES (cust_4, \"Rick\", \"James\", \"523 Blueberry Blvd\")");
            insert.executeUpdate("INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_address) VALUES (cust_5, \"Bobby\", \"Brown\", \"888 Sunshine Lane\")");
            System.out.println("customer inserted");
            
            // phone numbers
            insert.executeUpdate("INSERT INTO PhoneNumbers (cust_id, phone_type, phone_number) VALUES (cust_1, \"Cell\", 714-624-3888\")");
            insert.executeUpdate("INSERT INTO PhoneNumbers (cust_id, phone_type, phone_number) VALUES (cust_1, \"Home\", 714-624-9999\")");
            insert.executeUpdate("INSERT INTO PhoneNumbers (cust_id, phone_type, phone_number) VALUES (cust_2, \"Cell\", 714-981-3726\")");
            insert.executeUpdate("INSERT INTO PhoneNumbers (cust_id, phone_type, phone_number) VALUES (cust_2, \"Home\", 714-609-9124\")");
            insert.executeUpdate("INSERT INTO PhoneNumbers (cust_id, phone_type, phone_number) VALUES (cust_3, \"Cell\", 714-123-4527\")");
            insert.executeUpdate("INSERT INTO PhoneNumbers (cust_id, phone_type, phone_number) VALUES (cust_3, \"Home\", 714-624-0912\")");
            insert.executeUpdate("INSERT INTO PhoneNumbers (cust_id, phone_type, phone_number) VALUES (cust_4, \"Cell\", 714-518-6227\")");
            insert.executeUpdate("INSERT INTO PhoneNumbers (cust_id, phone_type, phone_number) VALUES (cust_4, \"Home\", 714-963-5422\")");
            insert.executeUpdate("INSERT INTO PhoneNumbers (cust_id, phone_type, phone_number) VALUES (cust_5, \"Cell\", 714-624-6437\")");
            insert.executeUpdate("INSERT INTO PhoneNumbers (cust_id, phone_type, phone_number) VALUES (cust_5, \"Home\", 714-961-5409\")");
            System.out.println("phone inserted");
            
            //inventory
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_1\", \"Ford\", \"Focus\", 2019, \"Electric\", \"Hatchback\", \"Black\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_2\", \"Acura\", \"Integra\", 2019, \"Hybrid\", \"4 Door\", \"White\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_3\", \"Toyota\", \"Highlander\", 2019, \"Gas\", \"4 Door\", \"Gray\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_4\", \"BMW\", \"330i\", 2018, \"Gas\", \"Coupe\", \"Red\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_5\", \"Mercedes-Benz\", \"CLA\", 2018, \"Gas\", \"Convertible\", \"Blue\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_6\", \"Mercedes-Benz\", \"GLA\", 2012, \"Gas\", \"Coupe\", \"Black\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_7\", \"Ford\", \"F-150\", 2012, \"Gas\", \"4 Door\", \"Gray\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_8\", \"BMW\", \"M3\", 2011, \"Compressed Natural Gas\", \"Convertible\", \"Red\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_9\", \"Chevy\", \"Volt\", 2019, \"Hydrogen Fuel Cell\", \"Hatchback\", \"White\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_10\", \"Toyota\", \"Tacoma\", 2013, \"Gas\", \"4 Door\", \"Blue\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_11\", \"Hyundai\", \"Elantra\", 2017, \"Hybrid\", \"Hatchback\", \"Black\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_12\", \"Nissan\", \"Sentra\", 2014, \"Solar\", \"4 Door\", \"Gray\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_13\", \"Dodge\", \"Charger\", 2011, \"Gas\" \"Convertible\", \"Red\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_14\", \"Ford\", \"Fusion\", 2019, \"Hybrid\", \"4 Door\", \"White\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_15\", \"Hyundai\", \"Elantra\", 2018, \"Hybrid\", \"Hatchback\", \"Blue\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_16\", \"Ford\", \"Focus\", 2019, \"Electric\", \"Hatchback\", \"Black\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_17\", \"Acura\", \"Integra\", 2019, \"Hybrid\", \"4 Door\", \"Gray\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_18\", \"Toyota\", \"Highlander\", 2019, \"Gas\", \"4 Door\", \"Red\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_19\", \"BMW\", \"330i\", 2018, \"Gas\", \"Coupe\", \"White\")");
            insert.executeUpdate("INSERT INTO Inventory (VIN, car_make, car_model, car_year, fuel_type, body_style, car_color) VALUES (\"VIN_20\", \"Mercedes-Benz\", \"CLA\", 2018, \"Gas\", \"Convertible\", \"Blue\")");
            System.out.println("inventory inserted");
            
            // salestranscation
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_1, emp_1, cust_1, trans_1, '2019-12-01', 24369.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_2, emp_2, cust_2, trans_2, '2019-11-03', 21569.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_3, emp_2, cust_3, trans_3, '2019-12-24', 23789.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_4, emp_3, cust_4, trans_4, '2018-10-22', 22812.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_5, emp_2, cust_5, trans_5, '2018-07-27', 19987.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_6, emp_1, cust_1, trans_6, '2012-01-18', 7512.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_7, emp_3, cust_2, trans_7, '2012-10-15', 12345.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_8, emp_3, cust_3, trans_8, '2011-03-13', 9985.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_9, emp_2, cust_4, trans_9, '2019-11-18', 25999.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_10, emp_1, cust_5, trans_10, '2013-10-18', 12785.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_11, emp_1, cust_1, trans_11, '2017-09-09', 20050.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_12, emp_3, cust_2, trans_12, '2014-02-01', 12850.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_13, emp_2, cust_3, trans_13, '2011-05-24', 11855.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_14, emp_2, cust_4, trans_14, '2019-11-22', 22699.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_15, emp_1, cust_5, trans_15, '2018-07-12', 27899.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_16, emp_1, cust_3, trans_16, '2019-12-01', 24399.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_17, emp_2, cust_4, trans_17, '2019-11-03', 21569.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_18, emp_2, cust_5, trans_18, '2019-12-24', 23789.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_19, emp_3, cust_1, trans_19, '2018-10-22', 22800.00)");
            insert.executeUpdate("INSERT INTO SalesTransactions (VIN, employee_id, cust_id, transaction_id, purchase_date, sale_amt) VALUES (VIN_20, emp_2, cust_2, trans_20, '2018-07-27', 19999.00)");
            System.out.println("salesTrans inserted");
            
            // employees
            insert.executeUpdate("INSERT INTO Employees (employee_id, employee_fname, employee_lname, unused_vacation_days, department) VALUES (emp_4, \"Stan\", \"Marsh\", 1, \"Customer Service\")");
            insert.executeUpdate("INSERT INTO Employees (employee_id, employee_fname, employee_lname, unused_vacation_days, department) VALUES (emp_5, \"Eric\", \"Cartman\", 4, \"Customer Service\")");
            insert.executeUpdate("INSERT INTO Employees (employee_id, employee_fname, employee_lname, unused_vacation_days, department) VALUES (emp_6, \"Kyle\", \"Broflovski\", 3, \"Customer Service\")");
            System.out.println("employees inserted");
            
            // technicians
            insert.executeUpdate("INSERT INTO Technicians (employee_id, employee_fname, employee_lname, salary) VALUES (emp_7, \"Kyle\", \"Romanov\", 60000.00)");
            insert.executeUpdate("INSERT INTO Technicians (employee_id, employee_fname, employee_lname, salary) VALUES (emp_8, \"Dan\", \"Brow\", 62500.00)");
            insert.executeUpdate("INSERT INTO Technicians (employee_id, employee_fname, employee_lname, salary) VALUES (emp_9, \"Mitch\", \"Rapp\", 85000.00)");
            System.out.println("technicians inserted");
            
            // certificates
            insert.executeUpdate("INSERT INTO Certificates (employee_id, certificate_name) VALUES (emp_7, \"Oil Change Certificate\")");
            insert.executeUpdate("INSERT INTO Certificates (employee_id, certificate_name) VALUES (emp_7, \"Engine Repair Certificate\")");
            insert.executeUpdate("INSERT INTO Certificates (employee_id, certificate_name) VALUES (emp_7, \"Tire Rotation Certificate\")");
            insert.executeUpdate("INSERT INTO Certificates (employee_id, certificate_name) VALUES (emp_8, \"Tire Rotation Certificate\")");
            insert.executeUpdate("INSERT INTO Certificates (employee_id, certificate_name) VALUES (emp_9, \"Tire Rotation Certificate\")");
            insert.executeUpdate("INSERT INTO Certificates (employee_id, certificate_name) VALUES (emp_9, \"Oil Change Certificate\")");
            System.out.println("certificates inserted");
            
            //salespersons
            insert.executeUpdate ("INSERT INTO Salespersons (employee_id, employee_fname, employee_lname) VALUES (emp_1, \"Thomas\", \"Stansfield\")");
            insert.executeUpdate ("INSERT INTO Salespersons (employee_id, employee_fname, employee_lname) VALUES (emp_2, \"Scott\", \"Coleman\")");
            insert.executeUpdate ("INSERT INTO Salespersons (employee_id, employee_fname, employee_lname) VALUES (emp_3, \"Mick\", \"Reavers\")");
            System.out.println("salespersons inserted");
            
            //colors
            insert.executeUpdate ("INSERT INTO Colors (car_color) VALUES (\"Black\")");
            insert.executeUpdate ("INSERT INTO Colors (car_color) VALUES (\"Gray\")");
            insert.executeUpdate ("INSERT INTO Colors (car_color) VALUES (\"Red\")");
            insert.executeUpdate ("INSERT INTO Colors (car_color) VALUES (\"Blue\")");
            insert.executeUpdate ("INSERT INTO Colors (car_color) VALUES (\"White\")");
            System.out.println("colors inserted");
            
            // fuel types
            insert.executeUpdate("INSERT INTO FuelTypes (fuel_type) VALUES (\"Gas\")");
            insert.executeUpdate("INSERT INTO FuelTypes (fuel_type) VALUES (\"Electric\")");
            insert.executeUpdate("INSERT INTO FuelTypes (fuel_type) VALUES (\"Hybrid\")");
            insert.executeUpdate("INSERT INTO FuelTypes (fuel_type) VALUES (\"Compressed Natural Gas\")");
            insert.executeUpdate("INSERT INTO FuelTypes (fuel_type) VALUES (\"Hydrogen Fuel Cell\")");
            insert.executeUpdate("INSERT INTO FuelTypes (fuel_type) VALUES (\"Solar\")");
            System.out.println("fuelTypes inserted");
            
            //body styles
            insert.executeUpdate ("INSERT INTO BodyStyles (body_style) VALUES (\"4 Door\")");
            insert.executeUpdate ("INSERT INTO BodyStyles (body_style) VALUES (\"Coupe\")");
            insert.executeUpdate ("INSERT INTO BodyStyles (body_style) VALUES (\"Station Wagon\")");
            insert.executeUpdate ("INSERT INTO BodyStyles (body_style) VALUES (\"Convertible\")");
            insert.executeUpdate ("INSERT INTO BodyStyles (body_style) VALUES (\"Hatchback\")");
            System.out.println("bodyStyles inserted");
            
            /*//String insert = "INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_mid_ini, cust_ssn, cust_address, zip_code, cust_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement posted = conn.prepareStatement("INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_mid_ini, cust_ssn, cust_address, zip_code, cust_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            posted.setString(1, "1234567890");
            posted.setString(2, "Michael");
            posted.setString(3, "Jee");
            posted.setString(4, "D");
            posted.setString(5, "123456789012");
            posted.setString(6, "123 lolipop rd");
            posted.setString(7, "12345");
            posted.setString(8, "mdj@gmail.com");
            /*final String one = "1234567890", two = "Michael", three = "Jee", four = "D", five = "123456789012", six = "123 lolipop rd", eight = "mdj@gmail.com";
            final int seven = 12345;
            PreparedStatement posted = conn.prepareStatement("INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_mid_ini, cust_ssn, cust_address, zip_code, cust_email) " +
                    "VALUES ('"+one+"', '"+two+"', '"+three+"', '"+four+"', '"+five+"', '"+six+"', '"+seven+"', '"+eight+"')");*/
            //posted.executeUpdate();
            System.out.println("insert completed");
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
    
    public static void Query(Connection conn, int choice) {
        try {
            switch(choice)
            {
                case 1:
                {
                    //query1
                    System.out.println("query1");
                    break;
                }
                case 2:
                {
                    //query2
                    System.out.println("query2");
                    break;
                }
                case 3:
                {
                    //query3
                    System.out.println("query3");
                    break;
                }
                case 4:
                {
                    //query4
                    System.out.println("query4");
                    break;
                }
                case 5:
                {
                    //query5
                    System.out.println("query5");
                    break;
                }
                case 6:
                {
                    //query6
                    System.out.println("query6");
                    break;
                }
                case 7: 
                {
                    //query7
                    System.out.println("query7");
                    break;
                }
                case 8:
                {
                    //query8
                    System.out.println("query8");
                    break;
                }
                case 9:
                {
                    //query9
                    System.out.println("query9");
                    break;
                }
                case 10:
                {
                    //query10
                    System.out.println("query10");
                    break;
                }
                case 11:
                {
                    //query11
                    System.out.println("query11");
                    break;
                }
                case 12:
                {
                    //query12
                    System.out.println("query12");
                    break;
                }
                case 13:
                {
                    //query13
                    System.out.println("query13");
                    break;
                }
                case 14:
                {
                    //query14
                    System.out.println("query14");
                    break;
                }
                case 15:
                {
                    //query15
                    System.out.println("query15");
                    break;
                }
                case 16:
                {
                    //query16
                    System.out.println("query16");
                    break;
                }
                case 17:
                {
                    //query17
                    System.out.println("query17");
                    break;
                }
                default:
                {
                    System.out.println("switch error");
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
                }
            }
            //posted.executeUpdate();
            System.out.println("select completed");
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
    
    public static void AddCust(Connection conn, Customer cust) {
        try {
            PreparedStatement posted = conn.prepareStatement("INSERT INTO Customers (cust_id, cust_fname, cust_lname, cust_mid_ini, cust_ssn, cust_address, zip_code, cust_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            posted.setString(1, cust.getId()); //generated custid);
            posted.setString(2, cust.getFirst());
            posted.setString(3, cust.getLast());
            posted.setString(4, cust.getMiddle());
            posted.setString(5, cust.getSocial());
            posted.setString(6, cust.getAddress());
            posted.setString(7, cust.getZip());
            posted.setString(8, cust.getEmail());
            posted.executeUpdate();
            
            System.out.println("added");
            } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
}//end FirstExample}
