/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author andrb
 */
public class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";

    
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;
    

    

    public static void startConnection() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        FileInputStream fis = null; 
        try {
            fis = new FileInputStream("src/db.properties");
            props.load(fis);
            String userName = props.getProperty("db_user");
            String password = props.getProperty("db_pass");
            String jdbcURL = protocol + vendorName + props.getProperty("db_url");
            
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection Successful");
        }
        catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed");
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConn() {
        return conn;
    }

}
