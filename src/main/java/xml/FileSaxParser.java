package xml;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import service.FileService;
import util.ConnectionProvider;

class FileSaxParser extends DefaultHandler {

    private boolean name = false;
    private boolean surname = false;
    private boolean age = false;

    private boolean unknown = false;
    private boolean email = false;
    private boolean phone = false;
    private boolean jabber = false;

    private static String nameValue = "";
    private static String surnameValue = "";
    private static String ageValue = "";

    private static ArrayList<String> list = new ArrayList<>();
    FileService fileService = new FileService();

    //zapisanie użuytkowników do bazy danych
    private void importCustomersToDatabase() {
        try {
            try (PreparedStatement prepStm = ConnectionProvider.getConnection().prepareStatement(fileService.getCustomerQuery())) {
                prepStm.setString(1, nameValue);
                prepStm.setString(2, surnameValue);
                prepStm.setString(3, ageValue);
                prepStm.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Nie udało się wstawić danych do tabeli CUSTOMERS!");
        } finally {
            ConnectionProvider.closeConnection();
        }
    }

    //zapisanie informacji o kontaktach do bazy danych
    private void importContactsToDatabase(int id) {

        try {
            PreparedStatement prepStm = ConnectionProvider.getConnection().prepareStatement(fileService.getContactQuery());
            int length = list.size();
            for (int i = 0; i < length; i++) {
                prepStm.setInt(1, id);
                prepStm.setInt(2, fileService.getTypeOfContact(list, i));
                prepStm.setString(3, list.get(i));
                prepStm.executeUpdate();

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Nie udało się załadować danych do tabeli CONTACTS");
        } finally {

            ConnectionProvider.closeConnection();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase("name")) {
            name = true;
        }

        if (qName.equalsIgnoreCase("surname")) {
            surname = true;
            ageValue = "";
        }

        if (qName.equalsIgnoreCase("age")) {
            age = true;
        }

        if (qName.equalsIgnoreCase("icq")) {
            unknown = true;
        }
        if (qName.equalsIgnoreCase("email")) {
            email = true;
        }
        if (qName.equalsIgnoreCase("phone")) {
            phone = true;
        }
        if (qName.equalsIgnoreCase("jabber")) {
            jabber = true;
        }

    }

    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {
        if (qName.equals("contacts")) {
            importCustomersToDatabase();
            importContactsToDatabase(fileService.getID_Customer());
            list.clear();
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        if (name) {
            nameValue = new String(ch, start, length);

            name = false;
        }

        if (surname) {
            surnameValue = new String(ch, start, length);

            surname = false;
        }

        if (age) {

            ageValue = new String(ch, start, length);

            age = false;
        }

        if (unknown) {
            String unknownValue = new String(ch, start, length);
            list.add(unknownValue);
            unknown = false;
        }
        if (email) {
            String emailValue = new String(ch, start, length);
            list.add(emailValue);
            email = false;
        }
        if (phone) {
            String phoneValue = new String(ch, start, length);
            list.add(phoneValue);
            phone = false;
        }
        if (jabber) {
            String jabberValue = new String(ch, start, length);
            list.add(jabberValue);
            jabber = false;
        }

    }

};

