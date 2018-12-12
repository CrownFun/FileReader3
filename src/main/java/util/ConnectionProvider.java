package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// klasa nawiązucję połączenie JDBC z bazą danych. Wzorzec Singleton tworzy instancje Connection i dostarcza do aplikacji
public class ConnectionProvider {

    private static Connection connection;
    private static final String DB_PATH = "jdbc:mysql://localhost/file_xml_csv?useUnicode=yes&characterEncoding=UTF-8";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private ConnectionProvider(){
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_PATH, DB_USER, DB_PASS);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Nie udało sie nawiązać połączenia!");
        }
        return connection;
    }

    //zamykanie połączenia
    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
