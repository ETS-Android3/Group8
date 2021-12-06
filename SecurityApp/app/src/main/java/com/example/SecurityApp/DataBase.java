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


    private ArrayList<User> users;
    private ArrayList<Test> tests;
    private ArrayList<Attempt> attempts;

    // Query Templates
    private final static String GET_USERS_QUERY = "SELECT name, id, scrabblePassword, patternPassword FROM USERS;";
    private final static String GET_TESTS_QUERY = "SELECT tid, uid FROM TEST;";
    private final static String GET_ATTEMPTS_QUERY = "SELECT aid, attemptTime, results, lockType, unlockPattern, rotation, isRandomized, tid FROM ATTEMPT;";
    private final static String DELETE_USERS_QUERY = "DELETE FROM USERS;";
    private final static String DELETE_TESTS_QUERY = "DELETE FROM TEST;";
    private final static String DELETE_ATTEMPTS_QUERY = "DELETE FROM ATTEMPT;";
    private final static String EDIT_USER_QUERY = "UPDATE USERS SET scrabblePassword = ?, patternPassword = ? WHERE id = ?;";
    private final static String INSERT_USER_QUERY = "INSERT INTO USERS VALUES (?, ?, ?, ?);";
    private final static String INSERT_TEST_QUERY = "INSERT INTO TEST VALUES (?, ?);";
    private final static String INSERT_ATTEMPT_QUERY = "INSERT INTO ATTEMPT VALUES (?, ?, ?, ?, ?, ?, ?, ?);";



    public DataBase() {
        users = getRemoteUsers();
        tests = getRemoteTests();
        attempts = getRemoteAttempts();
    }

    public int newAttempt(int uid, double attemptTime, String lockType, boolean unlockSuccess, String unlockPattern, int rotation, boolean randomized){
        //If the last test is not complete, add new attempt to it, otherwise start new test
        Attempt a = new Attempt(attempts.size(), attemptTime, unlockSuccess, lockType, unlockPattern, rotation, randomized);
        System.out.print("ADDING NEW ATTEMPT ID: ");
        System.out.println(a.getId());
        attempts.add(a);
        Test t;
        //If no tests exists, create a new test
        if(tests.size() == 0){

            t = new Test(tests.size(),uid);
            System.out.print("ADDING NEW TEST ID: ");
            System.out.println(t.getId());
            tests.add(t);
            t.addAttempt(a);
        }
        //Otherwise get the last test
        else {
            t = tests.get((tests.size() - 1));
            //If the test is complete, create new test and add it to the test
            if (t.testComplete() == 1) {
                t = new Test(tests.size(), uid);
                System.out.print("ADDING NEW TEST ID: ");
                System.out.println(t.getId());
                tests.add(t);
                t.addAttempt(a);
            }
            //Otherwise, if the test isn't complete, add the attempt
            else {
                t.addAttempt(a);
            }
        }
        //If this is the last attempt in the test, add it to the remote database
        if(unlockSuccess) addRemoteTest(t);
        return t.getId();
    }
    //PASSWORD GETTERS AND SETTERS
    public void setScrabblePasswordById(int id,String password){
        User user = users.get(id);
        user.setScrabblePassword(password);
        editRemoteUser(user);
    }
    public String getScrabblePasswordById(int id){
        return users.get(id).getScrabblePassword();
    }
    public void setPatternPasswordById(int id,String password){
        User user = users.get(id);
        user.setPatternPassword(password);
        editRemoteUser(user);
    }
    public String getPatternPasswordById(int id){
        return users.get(id).getPatternPassword();
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
            User user = new User(name, users.size(), "PASS", "123456");
            users.add(user);
            addRemoteUser(user);
            return true;
        }
        return false;
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
    private ArrayList<Test> getRemoteTests(){

        ArrayList<Test> tests = new ArrayList<>();
        try
        {
            // Establish the connection.
            Connection connection = DriverManager.getConnection(URL);
            String sql = GET_TESTS_QUERY;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next())
            {
                tests.add(new Test(resultSet.getInt(1),resultSet.getInt(2)));
            }
            System.out.println("Got Tests");
            connection.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return tests;
    }
    private ArrayList<Attempt> getRemoteAttempts(){

        ArrayList<Attempt> attempts = new ArrayList<>();
        try
        {
            // Establish the connection.
            Connection connection = DriverManager.getConnection(URL);
            String sql = GET_ATTEMPTS_QUERY;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next())
            {
                Attempt a = new Attempt(
                        resultSet.getInt(1),
                        resultSet.getDouble(2),
                        resultSet.getBoolean(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getBoolean(7));
                attempts.add(a);
                tests.get(resultSet.getInt(8)).addAttempt(a);
            }

            System.out.println("Got Attempts");
            connection.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return attempts;
    }
    private void addRemoteUser(User user){
        try {
            // Establish the connection.
            Connection connection = DriverManager.getConnection(URL);
            Statement statement = connection.createStatement();

            // Delete all users for fresh start
            //statement.executeUpdate(DELETE_USERS_QUERY);

            //Insert each user

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_QUERY);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getScrabblePassword());
            preparedStatement.setString(4, user.getPatternPassword());
            preparedStatement.executeUpdate();

            System.out.println("Inserted Users.");
            connection.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void editRemoteUser(User user){
        try {
            // Establish the connection.
            Connection connection = DriverManager.getConnection(URL);
            Statement statement = connection.createStatement();

            // Delete all users for fresh start
            //statement.executeUpdate(DELETE_USERS_QUERY);

            //Insert each user

            PreparedStatement preparedStatement = connection.prepareStatement(EDIT_USER_QUERY);
            preparedStatement.setString(1, user.getScrabblePassword());
            preparedStatement.setString(2, user.getPatternPassword());
            preparedStatement.setInt(3, user.getId());
            preparedStatement.executeUpdate();

            System.out.println("Inserted Users.");
            connection.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void addRemoteTest(Test test){
        try {
            // Establish the connection.
            Connection connection = DriverManager.getConnection(URL);
            Statement statement = connection.createStatement();

            // Delete all tests and attempts for a fresh start
            //statement.executeUpdate(DELETE_TESTS_QUERY);
            //statement.executeUpdate(DELETE_ATTEMPTS_QUERY);

            //Insert new test
            //Insert each new Attempt

            for (Attempt attempt : test.getAttempts()) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ATTEMPT_QUERY);
                preparedStatement.setInt(1, attempt.getId());
                preparedStatement.setDouble(2, attempt.getAttemptTime());
                preparedStatement.setBoolean(3, attempt.isUnlockSuccess());
                preparedStatement.setString(4, attempt.getLockType());
                preparedStatement.setString(5, attempt.getUnlockPattern());
                preparedStatement.setInt(6, attempt.getRotation());
                preparedStatement.setBoolean(7, attempt.isRandomized());
                preparedStatement.setInt(8, test.getId());
                preparedStatement.executeUpdate();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TEST_QUERY);
            preparedStatement.setInt(1, test.getId());
            preparedStatement.setInt(2, test.getUid());

            preparedStatement.executeUpdate();

            System.out.println("Inserted Tests and Attempts.");
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
