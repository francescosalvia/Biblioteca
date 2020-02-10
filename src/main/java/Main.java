
import data.Cliente;
import data.Libro;
import data.Prestito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServizioBiblioteca;

import java.sql.SQLException;

import java.text.ParseException;

import java.util.List;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);
   private static   ServizioBiblioteca s = new ServizioBiblioteca();

    public static void main(String[] args) throws ParseException {

        try {
            s.caricaCliente();

            s.caricaLibro();

            s.addPrestito();

            // s.restituisciLibro();

            // s.modificaTelefonoResidenza();

            s.checkPrenotazioniPerUtente();

            List<Cliente> clienti = s.trovaClienti();

            for (Cliente c : clienti){
                System.out.println(c.toString());
            }

            List<Libro> libri = s.trovaLibri();

            for (Libro l : libri){
                System.out.println(l.toString());
            }

            List<Prestito> prestiti = s.trovaPrestiti();

            for (Prestito p : prestiti){
                System.out.println(p.toString());
            }

            s.checkPrestitiScaduti();

            List<Libro> libroPerAutore = s.getLibroPerAutore();

            for (Libro l : libroPerAutore){
                System.out.println(l.toString());
            }

            System.out.println("----------------");


            List<Cliente> clienti1 = s.getClientiPerAutore();

            for (Cliente c : clienti1){
                System.out.println(c.toString());
            }

        } catch (SQLException e) {
            log.error("SQLException errore ", e);
        }




    }





}
