package service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import dao.ClienteDao;
import dao.LibroDao;
import dao.PrenotazioneDao;
import data.Cliente;
import data.Libro;
import data.Prestito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class ServizioBiblioteca {

    private static Logger log = LoggerFactory.getLogger(ServizioBiblioteca.class);

    private static ClienteDao cliDao = new ClienteDao();
    private static LibroDao liDao = new LibroDao();
    private static PrenotazioneDao preDao = new PrenotazioneDao();


    public boolean controlloEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    /**
     * PUNTO 2
     **/
    public void caricaCliente(String file) throws SQLException {

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;


            while ((line = br.readLine()) != null) {
                String[] details = line.split(";");

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

                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

                    Phonenumber.PhoneNumber number = phoneUtil.parse(telefono, "IT");

                    if (phoneUtil.isValidNumber(number)) {

                        telefono = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);

                        log.info("Telefono verificato!");

                        if (controlloEmail(email)) {
                            log.info("Email verificata!");
                            Optional<Integer> idCliente = cliDao.getIdCliente(email);

                            if (idCliente.isPresent()) {
                                cliDao.updateCliente(nome, cognome, sDate, luogoNascita, residenza, email, telefono, idCliente.get());
                                log.info("Cliente aggiornato con successo");
                            } else {
                                cliDao.insertCliente(nome, cognome, sDate, luogoNascita, residenza, email, telefono);
                                log.info("Cliente inserito con successo");
                            }

                        } // fine if controllo email
                    }
                } // fine if details.lenght
            } // fine while

        } catch (IOException | ParseException | NumberParseException e) {
            log.error(" errore nel metodo caricaCliente  ", e);
        }
    }


    /**
     * PUNTO 1
     **/

    public void caricaLibro(String file) throws SQLException {

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
                        liDao.updateLibro(titolo, autore, data, genere, idLibro.get());  // disponibilità non la modifico per non influire su eventuali prestiti
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

    /**
     * PUNTO 6
     **/
    public void addPrestito(String file) throws SQLException {

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

                            String restituito = "false";
                            Optional<Integer> conteggioLibri = preDao.getConteggio(idCliente.get(), restituito);

                            int conteggio = conteggioLibri.get();

                            if (conteggio < 3) {
                                int idCliente2 = idCliente.get();
                                int idLibro2 = idLibro.get();
                                 restituito = "false";
                                preDao.insertPrestito(idLibro2, idCliente2, restituito);
                                log.info("Prestito inserito");
                                String disponibilità = "false";
                                liDao.updateDisponibilitàLibro(autore, titolo, disponibilità);
                                log.info("Disponibilità aggiornata");
                            } else {
                                log.info("Non si possono prenotare piu di 3 libri");
                            }
                        }
                    }
                }

            }

    } catch(
    IOException e)

    {
        log.error("IoException nel metodo add prestito  ", e);
    }

}


    public void restituisciLibro(String file) throws SQLException {

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
                            String restituito = "true";
                            preDao.updatePrestito(idLibro2, idCliente2, restituito);
                            log.info("Prestito restituito");
                            String disponibilità = "true";
                            liDao.updateDisponibilitàLibro(autore, titolo, disponibilità);
                            log.info("Disponibilità aggiornata");
                        }
                    }

                }

            }

        } catch (IOException e) {
            log.error("IoException nel metodo restituisciLibro  ", e);
        }


    }

    /**
     * PUNTO 3-5
     **/
    public void modificaTelefonoResidenza(String telefono, String residenza) throws SQLException {

        try {
            if (!telefono.isBlank()) {

                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

                Phonenumber.PhoneNumber number = phoneUtil.parse(telefono, "IT");

                if (phoneUtil.isValidNumber(number)) {
                    log.info("Telefono verificato!");
                    telefono = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);

                    cliDao.updateTelefonoResidenza(telefono, "telefono");
                    log.info("modifica telefono eseguita");
                }
            }
            if (!residenza.isBlank()) {
                cliDao.updateTelefonoResidenza(residenza, "residenza");
                log.info("modifica residenza eseguita");
            }


        } catch (NumberParseException e) {
            log.error("IoException nel metodo caricaCliente  ", e);

        }

    }

    /**
     * PUNTO 8
     **/
    public void checkPrenotazioniPerUtente(String file) throws SQLException {

        LocalDate dataOggi = LocalDate.now();
        LocalDate dataPrestito = null;

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

                            Optional<Timestamp> dataFromGet = preDao.getDataPrestito(idLibro2, idCliente2);

                            if (dataFromGet.isPresent()) {
                                dataPrestito = dataFromGet.get().toLocalDateTime().toLocalDate();
                            }

                            if (dataPrestito != null) {
                                long days = ChronoUnit.DAYS.between(dataPrestito, dataOggi);

                                if (days > 30) {
                                    log.warn("Il prestito per l'utente con id : {} supera i 30 giorni", idCliente2);
                                } else {
                                    log.info("Il prestito non supera i 30 giorni");
                                }

                            }
                        }
                    }

                }

            }


        } catch (IOException e) {
            log.error("IoException nel metodo caricaCliente  ", e);
        }

    }

    public List<Cliente> trovaClienti() throws SQLException {
        log.info("Metodo trovaClienti");
        return cliDao.getClienti();
    }

    public List<Libro> trovaLibri() throws SQLException {
        log.info("Metodo trova libri");
        return liDao.getLibro();
    }

    public List<Prestito> trovaPrestiti() throws SQLException {
        log.info("Metodo trova prestiti");
        return preDao.getPrestito();
    }


    /**
     * PUNTO 7
     **/
    public void checkPrestitiScaduti() throws SQLException {

        List<Prestito> prestiti = preDao.getPrestito();
        LocalDate dataOggi = LocalDate.now();

        for (Prestito prestito : prestiti) {
            LocalDate dataPrestito = prestito.getDataPrestito();

            if (dataPrestito != null) {
                long days = ChronoUnit.DAYS.between(dataPrestito, dataOggi);

                if (days > 30) {
                    log.warn("Il prestito per l'utente con id : {} supera i 30 giorni", prestito.getIdUtente());
                } else {
                    log.info("Il prestito non supera i 30 giorni");
                }
            }
        }


    }

    /**
     * PUNTO 9
     **/
    public List<Libro> getLibroPerAutore(String autore) throws SQLException {

        return liDao.getLibroPerAutore(autore);
    }

    /**
     * PUNTO 10
     **/
    public List<Cliente> getClientiPerAutore(String autore) throws SQLException {

        List<Cliente> clienti = new ArrayList<>();
        List<Prestito> prestiti = new ArrayList<>();


        List<Libro> libri = getLibroPerAutore(autore);
        for (int i = 0; i < libri.size(); i++) {

            Optional<Prestito> prestito = preDao.getPrestitoPerIdLibro(libri.get(i).getIdLibro());
            if (prestito.isPresent()) {
                prestiti.add(prestito.get());
            }
        }

        for (int i = 0; i < prestiti.size(); i++) {

            Optional<Cliente> cliente = cliDao.getClientiPerId(prestiti.get(i).getIdUtente());
            if (cliente.isPresent()) {
                clienti.add(cliente.get());
            }
        }

        return clienti;
    }


    public  Map<Libro, Integer> classificaLibri() throws SQLException {

        List<Prestito> prestiti = preDao.getPrestito();

        Map<Libro, List<Prestito>> map = new HashMap<>();


        for (int i = 0; i < prestiti.size(); i++) {

            Libro libro;
            int idLibro = prestiti.get(i).getIdLibro();

            Optional<Libro> libro2 = liDao.getLibroPerID(idLibro);

            if (libro2.isPresent()) {

                libro = libro2.get();

                if (map.containsKey(libro)) {
                    List<Prestito> prestitoBis = map.get(libro);
                    prestitoBis.add(prestiti.get(i));
                } else {
                    List<Prestito> prestitoBis = new ArrayList<>();
                    prestitoBis.add(prestiti.get(i));
                    map.put(libro, prestitoBis);
                }
            }
        }

        Map<Libro, Integer> map2 = new HashMap<>();
        map.forEach((k, v) -> map2.put(k, v.size()));

        System.out.println("Elenco Libri + Numero elementi che hanno quel libro, ordinati in ordine decrescente: ");

            map2.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                    forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));

            log.info("Fine metodo classificaLibri ");

            return map2;
    }





    public Map<Cliente, Integer> classificaClienti() throws SQLException {

        List<Prestito> prestiti = preDao.getPrestito();

        Map<Cliente, List<Prestito>> map = new HashMap<>();


        for (int i = 0; i < prestiti.size(); i++) {

            Cliente cliente;
            int idCliente = prestiti.get(i).getIdUtente();

            Optional<Cliente> cliente2 = cliDao.getClientiPerId(idCliente);

            if (cliente2.isPresent()) {

                cliente = cliente2.get();

                if (map.containsKey(cliente)) {
                    List<Prestito> prestitoBis = map.get(cliente);
                    prestitoBis.add(prestiti.get(i));
                } else {
                    List<Prestito> prestitoBis = new ArrayList<>();
                    prestitoBis.add(prestiti.get(i));
                    map.put(cliente, prestitoBis);
                }
            }
        }

        Map<Cliente, Integer> map2 = new HashMap<>();
        map.forEach((k, v) -> map2.put(k, v.size()));

        System.out.println("Elenco Clienti + Numero libri letti, ordinati in ordine decrescente: ");

        map2.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));

        log.info("Fine metodo classificaClienti ");

        return map2;
    }



    public void classificaGenereLibriCliente() throws SQLException {



        List<Prestito> prestiti = preDao.getPrestito();
        List<Cliente> clienti = cliDao.getClienti();

        Map<String, List<Prestito>> map = null;


    for(int j = 0; j < clienti.size(); j++) {

        int idCliente = clienti.get(j).getIdCliente();
        map = new HashMap<>();
        for (int i = 0; i < prestiti.size(); i++) {


            if (idCliente == prestiti.get(i).getIdUtente()) {



                String genere;
                int idLibro = prestiti.get(i).getIdLibro();

                Optional<String> genere2 = liDao.getGenerePerId(idLibro);

                if (genere2.isPresent()) {

                    genere = genere2.get();

                    if (map.containsKey(genere)) {
                        List<Prestito> prestitoBis = map.get(genere);
                        prestitoBis.add(prestiti.get(i));
                    } else {
                        List<Prestito> prestitoBis = new ArrayList<>();
                        prestitoBis.add(prestiti.get(i));
                        map.put(genere, prestitoBis);
                    }
                }
            }
        }


            Map<String, Integer> map2 = new HashMap<>();
            map.forEach((k, v) -> map2.put(k, v.size()));


            System.out.println(clienti.get(j).toString());
            if (map.size() == 0){
                System.out.println("Nessun libro letto");
            } else {
                System.out.println("genere + libri letti per genere");
                map2.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                        forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));
            }
        }


        log.info("Fine metodo classificaGenereLibriCliente ");

    }



    public Map<String, Integer> classificaGenereLibri() throws SQLException {

        List<Prestito> prestiti = preDao.getPrestito();

        Map<String, List<Prestito>> map = new HashMap<>();


        for (int i = 0; i < prestiti.size(); i++) {

            String  genere;
            int idLibro = prestiti.get(i).getIdLibro();

            Optional<String> genere2 = liDao.getGenerePerId(idLibro);

            if (genere2.isPresent()) {

                genere = genere2.get();

                if (map.containsKey(genere)) {
                    List<Prestito> prestitoBis = map.get(genere);
                    prestitoBis.add(prestiti.get(i));
                } else {
                    List<Prestito> prestitoBis = new ArrayList<>();
                    prestitoBis.add(prestiti.get(i));
                    map.put(genere, prestitoBis);
                }
            }
        }

        Map<String, Integer> map2 = new HashMap<>();
        map.forEach((k, v) -> map2.put(k, v.size()));

        System.out.println("Elenco genere libro + Numero di libri con quel genere, ordinati in ordine decrescente: ");

        log.info("Fine metodo classificaGenereLibri ");

        return map2;
    }



}








