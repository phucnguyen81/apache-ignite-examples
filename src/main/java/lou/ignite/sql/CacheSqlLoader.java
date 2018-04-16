package lou.ignite.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.ignite.IgniteJdbcDriver;

import lou.ignite.util.Base;

public class CacheSqlLoader extends Base {

    public static void main(String[] args) throws Exception {
        println("JDBC example started.");

        // Register JDBC driver
        new IgniteJdbcDriver();
        // Class.forName("org.apache.ignite.IgniteJdbcDriver");

        // Open JDBC connection
        Connection conn = DriverManager.getConnection(
            "jdbc:ignite:thin://127.0.0.1/");

        // Get data
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(
                "SELECT p.name, c.name " +
                " FROM person p, city c " +
                " WHERE p.city_id = c.id")) {
                println("Query results:");
                while (rs.next())
                    println(">>>    " + rs.getString(1) + ", " + rs.getString(2));
            }
        }

        conn.close();
    }
}