
package com.uat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author louis
 */
public class DBOperation {
    
    public static Statement state;
    public static Connection conn;
    public static ResultSet rs;

    public static Connection getConnection() {
        
        // set Driver
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        try {
            Class.forName(driver);
            System.out.println("Driver連線成功!!");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver連線失敗...");
            System.out.println(e);
        }

        // set connectionUrl
        String connectionUrl = "jdbc:sqlserver://LOUISJHUANG\\MSSQLSERVER2017;DatabaseName=JEPUN_LAND";
        String username = "sa";
        String password = "1";
        conn = null;
        // connect to DB
        try {
            conn = DriverManager.getConnection(connectionUrl, username, password);
            System.out.println("SQL連線成功!!");
        } catch (SQLException e) {
            System.err.println("SQL連線失敗...");
            System.out.println(e);
        }
        
        /*
        // Close DB connect
        try {
            conn.close();
            System.err.println("SQL連線已關閉!!");
        } catch (SQLException e) {
            System.err.println("SQL連線未關閉...");
            System.out.println(e);
        }
        */
        
        return conn;
    }
    
    public static ArrayList<String> selectRowFromDB(){    
        
        ArrayList<String> data = new ArrayList<>();
        String sql = String.format("SELECT TOP 10 * FROM [dbo].[AppUsers] ");
        try {
            state = getConnection().createStatement();
            rs = state.executeQuery(sql);
            while (rs.next()) {
                data.add(rs.getString("CName") );
            }
            
            int num = data.size();   //知道ArrayList 大小
            System.out.println("共使用 "+num+" 個陣列"+"\n");  //看使用幾個陣列
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return data;
    }
    
    
    
    
    public static void main(String[] args) throws SQLException {
        
        System.out.println(DBOperation.getConnection());
        System.out.println(DBOperation.selectRowFromDB());
        
    }
    
}
