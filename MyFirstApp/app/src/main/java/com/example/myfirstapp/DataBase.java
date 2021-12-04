package com.example.myfirstapp;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DataBase{
    // Database credentials
    final static String HOSTNAME = "sanb4019-sql-server.database.windows.net";

    final static String DBNAME = "security_project";

    final static String USERNAME = "sanb4019";

    final static String PASSWORD = "5VEXue6kJzUN7jZ";

    // Database connection string
    final static String URL = String.format(
            "jdbc:jtds:sqlserver://%s:1433/%s;user=%s;password=%s;encrypt=true;"
                    + "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
            ,HOSTNAME, DBNAME, USERNAME, PASSWORD);
    Connection connection = null;  // For making the connection
    Statement statement = null;    // For the SQL statement
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;    // For the result set, if applicable

    public DataBase() {

    }

    public ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();
        try
        {

            // Ensure the SQL Server driver class is available.
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Establish the connection.

            connection = DriverManager.getConnection(URL);
            System.out.println("Connected to DB!!!!");
            String filename = "SVPoster.jpg";
            String sql = "SELECT name,id FROM USERS;";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if(resultSet.next())
            {
                users.add(new User(resultSet.getString(2),resultSet.getInt(1)));
            }
            System.out.println("Got Users");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return users;
    }
    public void setUsers(ArrayList<User> users){
        
        try
        {

            // Ensure the SQL Server driver class is available.
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Establish the connection.
            connection = DriverManager.getConnection(URL);
            String filename = "SVPoster.jpg";
            String sql_delete = "DELETE FROM USERS;";
            String sql_insert = "INSERT INTO USERS VALUES (?, ?);";
            statement = connection.createStatement();

            statement.executeUpdate(sql_delete);

            for (User user:users) {
                preparedStatement = connection.prepareStatement(sql_insert);
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setString(2, user.getName());
                preparedStatement.executeUpdate();
            }

            System.out.println("Inserted Users.");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
