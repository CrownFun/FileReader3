package main;

import java.sql.SQLException;
import csv.CsvFileReader;
import xml.XmlReader;

//klasa odpowiedzialna za załadowanie plku. Jedyne miejsce w którym należy podać ściężkę.
public class FileLoader {

    private static final String FILE_PATH = "dane.xml";   // ścieżka do pliku

    public static String getFilePath() {
        return FILE_PATH;
    }

    public void loadFile() {

        try {
            if (FILE_PATH.contains(".csv") || FILE_PATH.contains(".txt")) {
                new CsvFileReader().importFileToDatabase();
            } else if (FILE_PATH.contains(".xml")) {
                new XmlReader().importXmlToDatabase();
            } else {
                System.out.println("niepoprawny format pliku!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nie można załadować pliku!");

        }
    }

}