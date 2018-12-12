package main;

// klasa startowa
public class MainReader {

    public static void main(String[] args) {

        String text = "jakis text";

        long t1 = System.currentTimeMillis();

        FileLoader load = new FileLoader();
        load.loadFile();

        long t2 = System.currentTimeMillis();
        System.out.println("Czas wykonania programu :"+(t2-t1)/1000.0 + " sekund");       // czas wykonania programu w sekundach
    }
}
