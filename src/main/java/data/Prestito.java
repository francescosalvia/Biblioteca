package data;

import java.time.LocalDateTime;

public class Prestito {

    private String idUtente;
    private String idLibro;
    private LocalDateTime dataPrestito;


    public String getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(String idUtente) {
        this.idUtente = idUtente;
    }

    public String getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
    }

    public LocalDateTime getDataPrestito() {
        return dataPrestito;
    }

    public void setDataPrestito(LocalDateTime dataPrestito) {
        this.dataPrestito = dataPrestito;
    }
}
