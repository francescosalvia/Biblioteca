package data;

import java.time.LocalDateTime;

public class Libro {

    private String titolo;
    private String autore;
    private String anno;
    private String genere;
    private String idLibro;

    public Libro(String titolo, String autore, String anno, String genere) {
        this.titolo = titolo;
        this.autore = autore;
        this.anno = anno;
        this.genere = genere;
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


    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
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
}
