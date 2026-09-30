package banking;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/banking_db";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    private static Connection connection = null;

    private DatabaseConnection(){}

    public static Connection getConnection(){
        try{
            if(connection == null || connection.isClosed()){
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("-> Conexiunea la PostgreSQL a reusit cu succes!");
            }
        } catch(SQLException e){
            System.err.println("Eroare la conectare cu baza de date: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void closeConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("-> Conexiunea la baza de date a fost inchisa.");
            }
        } catch(SQLException e){
            System.err.println("Eroare la conectare cu baza de date: " + e.getMessage());
        }
    }
}