package dao;

import data.Prestito;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrenotazioneDao extends DatabaseDao {

    public void insertPrestito(int idLibro, int idCliente, String restituito) throws SQLException {

        PreparedStatement ps = getConnection().prepareStatement("INSERT INTO prestito(id_libro, id_cliente, restituito) VALUES (?,?,?)");

        ps.setInt(1, idLibro);
        ps.setInt(2, idCliente);
        ps.setString(3, restituito);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
    }

    public void updatePrestito(int idLibro, int idCliente,String restituito) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("UPDATE prestito SET restituito = ? where id_libro = ? and id_cliente = ? ");
        ps.setString(1,restituito);
        ps.setInt(2,idLibro);
        ps.setInt(3,idCliente);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();

    }

    public Optional<Timestamp> getDataPrestito(int idLibro, int idCliente) throws SQLException {

        Optional<Timestamp> dataPrestito = null;

        PreparedStatement ps = getConnection().prepareStatement("SELECT data_insert from prestito where id_libro = ? and id_cliente = ? ");

        ps.setInt(1,idLibro);
        ps.setInt(2,idCliente);

        ResultSet pr = ps.executeQuery();

        while (pr.next()) {
            dataPrestito = Optional.of(pr.getTimestamp("data_insert"));
        }

        return dataPrestito;

    }

    public List<Prestito> getPrestito() throws SQLException {
        List<Prestito> prestiti = new ArrayList<>();

        PreparedStatement ps = getConnection().prepareStatement("SELECT * from prestito");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            int  idUtente = rs.getInt("id_cliente");
            int  idLibro = rs.getInt("id_libro");
            LocalDate dataPrestito = rs.getTimestamp("data_insert").toLocalDateTime().toLocalDate();
            String restituito = rs.getString("restituito");

            Prestito prestito = new Prestito();
            prestito.setIdLibro(idLibro);
            prestito.setIdUtente(idUtente);
            prestito.setDataPrestito(dataPrestito);
            prestito.setRestituito(restituito);

            prestiti.add(prestito);

        }

        return prestiti;

    }

    public Optional<Prestito> getPrestitoPerIdLibro(int idLibro) throws SQLException {
        Prestito prestito = new Prestito();
        Optional<Prestito> prestito1 = null;

        PreparedStatement ps = getConnection().prepareStatement("SELECT * from prestito where id_libro = ?");
        ps.setInt(1,idLibro);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            int idUtente = rs.getInt("id_cliente");
            LocalDate dataPrestito = rs.getTimestamp("data_insert").toLocalDateTime().toLocalDate();

            prestito.setIdLibro(idLibro);
            prestito.setIdUtente(idUtente);
            prestito.setDataPrestito(dataPrestito);

            prestito1 = Optional.of(prestito);

        }

        return prestito1;

    }



    public Optional<Integer> getConteggio(int idCliente, String restituito) throws SQLException {

        Optional<Integer> conteggioPrestiti = null;

        PreparedStatement ps = getConnection().prepareStatement("SELECT COUNT(DISTINCT id_libro) AS conteggio  FROM prestito where id_cliente = ? and restituito = ? ");

        ps.setInt(1,idCliente);
        ps.setString(2, restituito);

        ResultSet pr = ps.executeQuery();

        while (pr.next()) {
            conteggioPrestiti = Optional.of(pr.getInt("conteggio"));
        }

        return conteggioPrestiti;

    }






}
