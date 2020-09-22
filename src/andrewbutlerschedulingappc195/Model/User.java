/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.Model;

/**
 *
 * @author andrb
 */
public class User {
    private static String userName;
    private static int userId;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        User.userId = userId;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }

    public static String getUserName() {
        return userName;
    }
    
}
