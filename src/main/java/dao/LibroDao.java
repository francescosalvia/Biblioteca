package dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.util.Date;
import java.util.Optional;

public class LibroDao  extends  DatabaseDao {



    public void insertLibro(String titolo, String autore, String anno, String genere, String disponibile) throws SQLException {

        PreparedStatement ps = getConnection().prepareStatement("INSERT INTO libro(titolo,  autore,  anno,  genere,  disponibile) VALUES (?,?,?,?,?)");

        ps.setString(1, titolo);
        ps.setString(2, autore);
        ps.setString(3, anno);
        ps.setString(4, genere);
        ps.setString(5, disponibile);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
    }

    public void updateLibro(String titolo, String autore, String anno, String genere, int idLibro) throws SQLException {

        PreparedStatement ps = getConnection().prepareStatement("UPDATE libro SET  titolo = ?,  autore = ?,  anno = ?,  genere = ? where id_libro = ?");

        ps.setString(1, titolo);
        ps.setString(2, autore);
        ps.setString(3, anno);
        ps.setString(4, genere);
        ps.setInt(5,idLibro);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
    }

    public Optional<Integer> getIdLibro(String titolo,String autore) throws SQLException {

        Optional<Integer> idCliente = Optional.empty();
        PreparedStatement ps = getConnection().prepareStatement("SELECT id_libro FROM libro where titolo = ? and autore = ?");

        ps.setString(1,titolo);
        ps.setString(2,autore);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            idCliente = Optional.of(rs.getInt("id_libro"));
        }

        return idCliente;
    }


    public Optional<Integer> getIdLibroDisponibile(String titolo,String autore,String disponibile) throws SQLException {

        Optional<Integer> idCliente = Optional.empty();
        PreparedStatement ps = getConnection().prepareStatement("SELECT id_libro FROM libro where titolo = ? and autore = ? and disponibile = ?");

        ps.setString(1,titolo);
        ps.setString(2,autore);
        ps.setString(3,disponibile);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            idCliente = Optional.of(rs.getInt("id_libro"));
        }

        return idCliente;
    }

        public void updateDisponibilitàLibro(String autore, String titolo, String disponibilità) throws SQLException {

            PreparedStatement ps = getConnection().prepareStatement("UPDATE libro SET disponibile = ? where  autore = ? and titolo = ? ");

            ps.setString(1,disponibilità);
            ps.setString(2,autore);
            ps.setString(3,titolo);


            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();


        }




}
