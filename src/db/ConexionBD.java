package db;

import java.sql.*;

public class ConexionBD {
    public static Connection getConexion() {
        Connection conexion = null;
        String userName = "root";
        String password = "root";
        String url = "jdbc:mysql://localhost:3306/proyecto_carts?characterEncoding=UTF-8";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conexion = DriverManager.getConnection(url, userName, password);
            System.out.println("Conexión a MySQL exitosa");

        } catch (ClassNotFoundException e) {
            System.out.println("Error: no se encontró el Driver JDBC");
        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL: " + e.getMessage());
        }

        return conexion;
    }

    /* Método para probar la conexión
    public static void main(String[] args) {
        Connection connection = getConexion();
        if (connection != null) {
            System.out.println("Conexión exitosa a la base de datos");
        } else {
            System.out.println("No se pudo establecer la conexión a la base de datos");
        }
    }
     */
}
