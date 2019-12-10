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
            PreparedStatement drop = conn.prepareStatement("DROP TABLE Customers");
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
                    "CONSTRAINT pk_customers PRIMARY KEY (cust_id))");
            create.executeUpdate();
            System.out.println("create completed");

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
            System.out.println("insert completed1");
            posted.executeUpdate();
            System.out.println("insert completed2");
            
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
            System.out.println("successful");
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
    
}//end FirstExample}
