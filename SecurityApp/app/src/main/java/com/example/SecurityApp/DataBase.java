package com.example.SecurityApp;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class DataBase implements Serializable {
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


    ArrayList<User> users;

    // Query Templates
    private final static String GET_USERS_QUERY = "SELECT name, id, scrabblePassword, patternPassword FROM USERS;";
    private final static String DELETE_USERS_QUERY = "DELETE FROM USERS;";
    private final static String INSERT_USER_QUERY = "INSERT INTO USERS VALUES (?, ?, ?, ?);";



    public DataBase() {
        users = getRemoteUsers();
    }

    public ArrayList<User> getUsers() {
        return users;
    }
    public ArrayList<String> getUserNames(){
        ArrayList<String> user_names = new ArrayList<>();
        for(User user:users){
            user_names.add(user.getName());
        }
        return user_names;
    }
    public boolean addUser(String name){
        boolean nameExists = false;
        for(User u:users){if(u.getName().equals(name)) nameExists = true;}
        if(!nameExists) {
            users.add(new User(name, users.size(), "password", "123456"));
            setRemoteUsers();
            return true;
        }
        return false;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        setRemoteUsers();
    }

    private void setRemoteAll(){

    }
    private ArrayList<User> getRemoteUsers(){

        ArrayList<User> users = new ArrayList<>();
        try
        {
            // Establish the connection.
            Connection connection = DriverManager.getConnection(URL);
            String sql = GET_USERS_QUERY;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next())
            {
                users.add(new User(resultSet.getString(1),resultSet.getInt(2), resultSet.getString(3),resultSet.getString(4)));
            }
            System.out.println("Got Users");
            System.out.println(users);
            connection.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return users;
    }
    private void setRemoteUsers(){
        try {
            // Establish the connection.
            Connection connection = DriverManager.getConnection(URL);
            Statement statement = connection.createStatement();

            // Delete all users for fresh start
            statement.executeUpdate(DELETE_USERS_QUERY);

            //Insert each user
            for (User user:users) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_QUERY);
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getScrabblePassword());
                preparedStatement.setString(4, user.getPatternPassword());
                preparedStatement.executeUpdate();
            }
            System.out.println("Inserted Users.");
            connection.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public User findUserByName(String name){
        System.out.println(String.format("Testing for %s",name));
        for(User u: users){

            System.out.println(u.getName());
            if(u.getName().equals(name)) return u;
        }
        final User o = null;
        return o;
    }
}
