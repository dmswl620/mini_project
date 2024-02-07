package jjjj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
   public static Connection conn = null;
   
   public static void DBconnection() {
      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
         
         conn = DriverManager.getConnection(
               "jdbc:mysql://222.119.100.89:3382/shopping",
               "minishop",
               "2m2w"
               );
      } catch (Exception e) {
         try {
            conn.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      }
      
   }

}