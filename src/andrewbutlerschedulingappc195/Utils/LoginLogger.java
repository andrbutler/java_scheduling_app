/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

/**
 *
 * @author andrb
 */
public class LoginLogger {
    private static String fileName = "." + File.separator + "logins.txt";
    private static String loginStatus;
    
    public LoginLogger(){}
    
    public static void log(String name, boolean success, Timestamp time) throws IOException{
        FileWriter fw = new FileWriter(fileName, true);
        if (success){
            loginStatus = "login succeeded";
        } else {
            loginStatus = "login failed";
        }
        fw.write(time + "\t" + name + "\t" + loginStatus + "\n");
        fw.close();
    }
    
}
