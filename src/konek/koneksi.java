package konek;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.cj.jdbc.Driver;
import java.sql.*;

public class koneksi {
    
       public static Connection koneksi;
    public static Statement st;
    
    public static Connection GetConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/perpustakaan";
        String user = "root";
        String pass = "kashoes";
        koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/perpustakaan", "root", "kashoes");
        return koneksi;
    }
}

