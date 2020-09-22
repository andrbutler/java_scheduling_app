/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author andrb
 */
public class DBQuery {
    private static Connection conn;
    private static String sql;
    private static PreparedStatement ps;

    public static void setConn(Connection conn) {
        DBQuery.conn = conn;
    }

    public static void setSql(String sql) {
        DBQuery.sql = sql;
    }



    public static PreparedStatement getPs() throws SQLException {
        ps = conn.prepareStatement(sql);
        return ps;
    }

    
    
}
