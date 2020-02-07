package service;

import dao.ClienteDao;
import dao.LibroDao;
import dao.PrenotazioneDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

public class ServizioBiblioteca {

    private static Logger log = LoggerFactory.getLogger(ServizioBiblioteca.class);

    private static ClienteDao cliDao = new ClienteDao();
    private static LibroDao liDao = new LibroDao();
    private static PrenotazioneDao preDao = new PrenotazioneDao();


    public void caricaCliente() throws SQLException {

        String file = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\clienti.txt";

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            int contatore = 0;

            while ((line = br.readLine()) != null) {
                String[] details = line.split(";");

                contatore++;

                if (details.length == 7) {

                    String nome = details[0];
                    String cognome = details[1];
                    String dataNascita = details[2];
                    Date data = new SimpleDateFormat("dd/MM/yyyy").parse(dataNascita);
                    java.sql.Date sDate = new java.sql.Date(data.getTime());
                    String luogoNascita = details[3];
                    String residenza = details[4];
                    String email = details[5];
                    String telefono = details[6];

                    Optional<Integer> idCliente = cliDao.getIdCliente(email);

                    if (idCliente.isPresent()) {
                        cliDao.updateCliente(nome,cognome,sDate,luogoNascita,residenza,email,telefono, idCliente.get());
                        log.info("Cliente aggiornato con successo");
                    } else {
                        cliDao.insertCliente(nome, cognome, sDate, luogoNascita, residenza, email, telefono);
                        log.info("Cliente inserito con successo");
                    }

                }


            }
        } catch (IOException | ParseException e) {
            log.error("IoException nel metodo caricaCliente  ", e);
        }
    }

    public void caricaLibro() throws SQLException {

        String file = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\libri.txt";

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                String[] details = line.split(";");

                if (details.length == 5) {

                    String titolo = details[0];
                    String autore = details[1];
                    String data = details[2];
                    String genere = details[3];
                    String disponibile = details[4];

                    Optional<Integer> idLibro = liDao.getIdLibro(titolo, autore);

                    if (idLibro.isPresent()) {
                        liDao.updateLibro(titolo,autore,data,genere,idLibro.get());  // disponibilità non la modifico per non influire su eventuali prestiti
                        log.info("Libro aggiornato con successo");
                    } else {
                        liDao.insertLibro(titolo, autore, data, genere, disponibile);
                        log.info("Libro inserito con successo");
                    }

                }


            }
        } catch (IOException e) {
            log.error("IoException nel metodo caricaCliente  ", e);
        }

    }


    public void addPrestito() throws SQLException {

        String file = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\prestito.txt";

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {

                String[] details = line.split(";");

                if (details.length == 3) {
                    String titolo = details[0];
                    String autore = details[1];
                    String email = details[2];


                    Optional<Integer> idCliente = cliDao.getIdCliente(email);
                    if (idCliente.isPresent()) {
                        String disponibile = "true";
                        Optional<Integer> idLibro = liDao.getIdLibroDisponibile(titolo, autore, disponibile);
                        if (idLibro.isPresent()) {
                            int idCliente2 = idCliente.get();
                            int idLibro2 = idLibro.get();
                            preDao.insertPrestito(idLibro2, idCliente2);
                            log.info("Prestito inserito");
                            String disponibilità = "false";
                            liDao.updateDisponibilitàLibro(autore, titolo, disponibilità);
                            log.info("Disponibilità aggiornata");
                        }
                    }

                }

            }

        } catch (IOException e) {
            log.error("IoException nel metodo caricaCliente  ", e);
        }

    }


    public void restituisciLibro() throws SQLException {

        String file = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\delete.txt";

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;


            while ((line = br.readLine()) != null) {

                String[] details = line.split(";");

                if (details.length == 3) {
                    String titolo = details[0];
                    String autore = details[1];
                    String email = details[2];

                    Optional<Integer> idCliente = cliDao.getIdCliente(email);
                    if (idCliente.isPresent()) {
                        String disponibile = "false";
                        Optional<Integer> idLibro = liDao.getIdLibroDisponibile(titolo, autore, disponibile);
                        if (idLibro.isPresent()) {
                            int idCliente2 = idCliente.get();
                            int idLibro2 = idLibro.get();
                            preDao.deletePrestito(idLibro2, idCliente2);
                            log.info("Prestito rimosso");
                            String disponibilità = "true";
                            liDao.updateDisponibilitàLibro(autore, titolo, disponibilità);
                            log.info("Disponibilità aggiornata");
                        }
                    }

                }

            }

        } catch (IOException e) {
            log.error("IoException nel metodo caricaCliente  ", e);
        }



    }















}


/*
    public static LocalDateTime controlloDate(String date, int contatore) {
        LocalDateTime localDate = null;
        try {
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            localDate = LocalDateTime.parse(date, formatter1);
        } catch (DateTimeParseException e) {
            log.error("C'è un errore alla con la data alla riga {}  Verrà sostituita con null", contatore, e);
        }

        return localDate;
    }
*/