package csv;

import main.FileLoader;
import util.ConnectionProvider;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import service.FileService;

public class CsvFileReader extends FileService{

    // załadowanie i sprawdzenie pliku csv
    private static BufferedReader loadCsvFile() {

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(FileLoader.getFilePath());
            bufferedReader = new BufferedReader(fileReader);

        } catch (HeadlessException | IOException ex) {
            ex.printStackTrace();
            System.out.println("Nie udało się załadować pliku .CSV!");
        }

        return bufferedReader;
    }

    //metoda zbiera wszytskie informacje o kontaktach do oddzielnej tabeli
    private static String[] getContactsFromArray(String[] tab) {

        String[] tab2 = new String[tab.length - 4];
        int j = 0;
        for (int i = 4; i < tab.length; i++) {
            tab2[j] = tab[i];
            j++;
        }
        return tab2;

    }

    //metoda zapisuje dane o użytkownikach do bazy danych
    private  void importCustomersToDatabase(String[] customers) throws SQLException {
        try (PreparedStatement prepStm = ConnectionProvider.getConnection().prepareStatement(getCustomerQuery())) {
            prepStm.setString(1, customers[0]);
            prepStm.setString(2, customers[1]);
            prepStm.setString(3, customers[2]);
            prepStm.executeUpdate();
        }
    }

    //metoda zapisuje dane o kontaktach do bazy danych
    private  void importContactsToDatabase(String[] customers, int id) throws SQLException {
        PreparedStatement prepStm = ConnectionProvider.getConnection().prepareStatement(getContactQuery());
        String tab[] = getContactsFromArray(customers);

        int length = tab.length;

        for (int i = 0; i < length; i++) {
            prepStm.setInt(1, id);
            prepStm.setInt(2, getTypeOfContact(new ArrayList<String>(Arrays.asList(tab)), i));
            prepStm.setString(3, tab[i]);
            prepStm.executeUpdate();
        }
    }

    /**
     * główna metda aplikacji, zapisze informacje do bazy danych. Z Uwagi na
     * architekturę plików CSV w których rekordy rozdzielane są znakiem nowej
     * linii, a wartości pól przecinkiem, zasadne jest użycie klasy
     * BufferedReader oraz metody readLine(). Nie wymaga zewnetrznych bibliotek.
     */
    public void importFileToDatabase() {

        try (BufferedReader bfReader = loadCsvFile()) {    // blok try-with-resources automatycznie zamknie strumień BufferedReader

            String textLine = bfReader.readLine();

            autoIncrementReset();

            do {

                String[] customers = textLine.split(",");

                importCustomersToDatabase(customers);

                importContactsToDatabase(customers,getID_Customer());

                textLine = bfReader.readLine();

            } while (textLine != null);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionProvider.closeConnection();
        }
    }

}
