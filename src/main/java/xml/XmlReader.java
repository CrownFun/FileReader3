package xml;

import java.io.IOException;
import java.sql.SQLException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import main.FileLoader;
import service.FileService;

/** Istnieje kilka mechanizmów do obsługi plików XML w Javie (JAXB,DOM). Ze względu na obsługę dużych plików najlepszym wyborem wydaję się uzycie klasy SAXParser.
 * W dokumentacji Oracle https://docs.oracle.com/javase/tutorial/jaxp/sax/when.html
 * można wyczytać  :
 *
 *      "SAX requires much less memory than DOM, because SAX does not construct an internal representation (tree structure) of the XML data, as a DOM does.
 *       Instead, SAX simply sends data to the application as it is read; your application can then do whatever it wants to do with the data it sees"
 *
 *      Klasa SaxParser implementuję interfejs ContentHandler. Jednak w praktyce wystarczy rozszerzyć klasę DefaultHandler oraz użyć metody startElement(),
 *      endElement() i characters().
 */


public class XmlReader {

    public  void importXmlToDatabase() throws SQLException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            FileSaxParser handler = new FileSaxParser();
            new FileService().autoIncrementReset();
            saxParser.parse(FileLoader.getFilePath(), handler);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            System.out.println("Nie można załadować pliku .xml, możliwa błędna składnia lub plik nie istnieje");
        }
    }
}