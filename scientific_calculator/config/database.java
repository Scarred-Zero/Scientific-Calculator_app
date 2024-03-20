//// Driver class: The driver class for the mysql database is com.mysql.jdbc.Driver.
//
//// Connection URL: The connection URL for the mysql database is jdbc:mysql://localhost:3306/banking_app_db
//// where jdbc is the API,
//// mysql is the database,
//// localhost is the server name on which mysql is running,
//// 3306 is the port number and sonoo is the database name.
//// We may use any database, in such case, we need to replace the banking_app_db with our database name.
//
//// Username: The default username for the mysql database is "root".
//
//// Password: It is the password given by the user at the time of installing the mysql database.
//// Sha for now, you guys can use "root" as the password.
//
//import java.sql.*;
//class MysqlCon{
//    public static void main(String args[]){
//        try{
//// Java program is loading jdbc driver to establish database connection.
//            Class.forName("com.mysql.jdbc.Driver");
//// The getConnection() method of DriverManager class is used to establish connection with the database.
//// Here "banking_app_db" is database name, "root" is username and password.
//            Connection con = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/banking_app_db","root","root");
//// The createStatement() method is used to create statement.
//// The object of statement is responsible to execute queries with the database.
//            Statement stmt = con.createStatement();
//// The executeQuery() method is used to execute queries to the database.
//// This method returns the object of ResultSet that can be used to get all the records of a table.
//            ResultSet rs = stmt.executeQuery("select * from emp");
//            while(rs.next())
//                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
//// By closing connection object statement and ResultSet will be closed automatically.
//// The close() method of Connection interface is used to close the connection.
//            con.close();
//        }catch(Exception e){ System.out.println(e);}
//    }
//}
//// You guys need to create a database first.
//// But you lot need to create a table in the mysql database,
//// For the table (table name could be "admin" or "users", depending on what you lot are creating),