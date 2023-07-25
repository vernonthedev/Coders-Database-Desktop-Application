package com.veike.crud;
import java.sql.*;

public class Database {
    public static Connection connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect =  DriverManager.getConnection("jdbc:mysql://localhost/coderslounge","root", "Codezilla@21");
            return connect;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
