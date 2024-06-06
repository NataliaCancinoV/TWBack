package mx.uv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static String url = "jdbc:mysql://localhost/dbpastel00";
    private static String driverName = "com.mysql.cj.jdbc.Driver"; 
    private static String username = "pastel00";
    private static String password = "pastel00";
    // variable de conexion
    private static Connection connection = null;

    public static Connection getConnection(){
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("conexion exitosa");
        } catch (SQLException e) {
            System.out.println(" SQL:" + e);
        } catch (ClassNotFoundException e){
            System.out.println("Driver:" + e);
        }
        
        return connection;
    }
}