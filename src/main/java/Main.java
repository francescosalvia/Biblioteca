
import data.Cliente;
import data.Libro;
import data.Prestito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServizioBiblioteca;

import java.sql.SQLException;

import java.text.ParseException;

import java.util.*;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);
    private static ServizioBiblioteca s = new ServizioBiblioteca();

    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        try {
            String fileCliente = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\clienti.txt";
            s.caricaCliente(fileCliente);

            String fileLibro = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\libri.txt";
            s.caricaLibro(fileLibro);

            String filePrestito = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\prestito.txt";
            s.addPrestito(filePrestito);

            String fileRestituisci = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\restituisci.txt";
            s.restituisciLibro(fileRestituisci);

            String filePrestito2 = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\prestito2.txt";
           s.addPrestito(filePrestito2);

            /*
            System.out.println("Metodo modifica telefono e residenza");
            System.out.println("Inserire Telefono e Residenza. Se non si vuole modificare lasciare vuoto");

            System.out.println("Telefono: ");
            String telefono = scanner.nextLine();

            System.out.println("Residenza: ");
            String residenza = scanner.nextLine();

            */

            // s.modificaTelefonoResidenza(telefono, residenza);

            String fileCheckPrenotazioni = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\checkPrestiti.txt";
            s.checkPrenotazioniPerUtente(fileCheckPrenotazioni);

            List<Cliente> clienti = s.trovaClienti();

            for (Cliente c : clienti) {
                System.out.println(c.toString());
            }

            List<Libro> libri = s.trovaLibri();

            for (Libro l : libri) {
                System.out.println(l.toString());
            }

            List<Prestito> prestiti = s.trovaPrestiti();

            for (Prestito p : prestiti) {
                System.out.println(p.toString());
            }

            s.checkPrestitiScaduti();

/*
            System.out.println("Metodo get Libro Per Autore: ");
            System.out.println("Inserisci L'autore");

            String autore = scanner.nextLine();
            List<Libro> libroPerAutore = s.getLibroPerAutore(autore);

            for (Libro l : libroPerAutore) {
                System.out.println(l.toString());
            }

            System.out.println("----------------");


            System.out.println("Metodo get Clienti Per Autore: ");
            System.out.println("Inserisci L'autore");

            autore = scanner.nextLine();
            List<Cliente> clienti1 = s.getClientiPerAutore(autore);

            for (Cliente c : clienti1) {
                System.out.println(c.toString());
            }
*/

            Map<Libro, Integer> mapLibro = s.classificaLibri();

            mapLibro.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                    forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));

            System.out.println("---------------");

            Map<Cliente, Integer> mapClienti = s.classificaClienti();

            mapClienti.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                    forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));

            System.out.println("------------");


            Map<String,Integer> mapGenere = s.classificaGenereLibri();

            mapGenere.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                    forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));

            System.out.println("-----------------------");
            System.out.println("-----------------------");



            Map<String,Map<String, Integer>> map3 = s.classificaGenereLibriCliente();

            /*
            Map<String, Integer> innerMap = new HashMap<>();
            innerMap = map3.get();
            for(String innerKey : innerMap.keySet()){
                Integer innerValue = innerMap.get(innerKey);
            }

*/

        } catch (SQLException e) {
            log.error("SQLException errore ", e);
        }


    }


}
