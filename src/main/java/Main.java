import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServizioBiblioteca;

import java.sql.SQLException;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);
   private static   ServizioBiblioteca s = new ServizioBiblioteca();

    public static void main(String[] args) {

        try {
            s.caricaCliente();

            s.caricaLibro();

            s.addPrestito();

            s.restituisciLibro();

            s.modificaTelefonoResidenza();

            /** STAMPE **/




















        } catch (SQLException e) {
            log.error("SQLException errore ", e);
        }


    }


}
