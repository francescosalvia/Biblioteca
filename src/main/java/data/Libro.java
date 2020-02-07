package data;

import java.time.LocalDateTime;
import java.util.Date;

public class Libro {

    private String titolo;
    private String autore;
    private Date anno;
    private String genere;
    private String disponibile;
    private String idLibro;


    public Libro(String titolo, String autore, Date anno, String genere, String disponibile) {
        this.titolo = titolo;
        this.autore = autore;
        this.anno = anno;
        this.genere = genere;
        this.disponibile = disponibile;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }


    public Date getAnno() {
        return anno;
    }

    public void setAnno(Date anno) {
        this.anno = anno;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
    }

    public String getDisponibile() {
        return disponibile;
    }

    public void setDisponibile(String disponibile) {
        this.disponibile = disponibile;
    }
}
