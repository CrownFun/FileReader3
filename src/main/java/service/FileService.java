package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import util.ConnectionProvider;

// klasa pomocnicza przechowuje metody wspólne dla obu typu plików.
public class FileService {

    private final String INSERT_CUSTOMERS = "INSERT INTO customers(ID,NAME,SURNAME,AGE) VALUES(null,?,?,?)";
    private final String INSERT_CONTACTS = "INSERT INTO contacts(ID,ID_CUSTOMER,TYPE,CONTACT) VALUES(null,?,?,?)";

    public String getCustomerQuery() {
        return INSERT_CUSTOMERS;
    }

    public String getContactQuery() {
        return INSERT_CONTACTS;
    }

    //metoda rozpoznaje rodzaj kontaktu (Stream, filter?)
    public int getTypeOfContact(ArrayList<String> list, int i) {

        int type = 0;
        if (list.get(i).contains("@")) {
            type = 1;
        } else if (list.get(i).equals("jbr")) {
            type = 3;
        } else if (list.get(i).replaceAll("\\s+", "").matches("[0-9]+") && list.get(i).length() >= 9) {
            type = 2;
        } else {
            type = 0;
        }
        return type;
    }

    // metoda zresetuje id w bazie danych do 1
    public void autoIncrementReset() throws SQLException {

        try {
            try (Statement statement = ConnectionProvider.getConnection().createStatement()) {
                statement.execute("ALTER TABLE customers AUTO_INCREMENT =1");
                statement.execute("ALTER TABLE contacts AUTO_INCREMENT =1");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Nie można ustawić ID");
        } finally {
            ConnectionProvider.closeConnection();
        }

    }

    // metoda pobiera ID_CUSTOMER
    public int getID_Customer() {
        int id = 0;
        try {
            ResultSet result;
            try (Statement statement = ConnectionProvider.getConnection().createStatement()) {
                result = statement.executeQuery("SELECT ID FROM customers");
                while (result.next()) {
                    id = result.getInt("id");
                }
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Błąd nie można pobrać ID_CUSTOMER");
        } finally {
            ConnectionProvider.closeConnection();
        }
        return id;
    }

}
