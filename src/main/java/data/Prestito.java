package data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Prestito {

    private String idUtente;
    private String idLibro;
    private LocalDate dataPrestito;


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

    public LocalDate getDataPrestito() {
        return dataPrestito;
    }

    public void setDataPrestito(LocalDate dataPrestito) {
        this.dataPrestito = dataPrestito;
    }

    @Override
    public String toString() {
        return "Prestito { " +
                "idUtente='" + idUtente + '\'' +
                ", idLibro='" + idLibro + '\'' +
                ", dataPrestito=" + dataPrestito +
                " } ";
    }
}
