/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author th
 */
public class DatabaseHandler
{
    private static final String HOST_STRING = "jdbc:derby://localhost:1527/Client1";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    Connection con;
    public DatabaseHandler() throws SQLException
    {
        con = DriverManager.getConnection(HOST_STRING,USERNAME,PASSWORD);
    }
    
    
}
