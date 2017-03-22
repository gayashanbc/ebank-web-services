package ebank.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author - Gayashan Bombuwala facilitate singleton design pattern
 */
public class DB_Connection {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet rs;

    // prevent instantiating the class from the outside
    private DB_Connection() {
    }

    /**
     * ensure one instance is used through out the application
     *
     * @return - a Connection instance to the remote DB
     */
    public static synchronized Connection getInstance() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("\n\tPlease wait.....");
                connection = DriverManager.getConnection("jdbc:mysql://146.185.16.120/cybertec_common_DB",
                        "cybertec_basic", "ID961160367");
            } catch (SQLException e) {
                System.out.println(e);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DB_Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }

    public static ResultSet fetchData(String query) {
        Connection conn = getInstance();

        try {
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery(query);
            rs.next();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public static boolean runQuery(PreparedStatement pst) {
        try {
            pst.execute();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
