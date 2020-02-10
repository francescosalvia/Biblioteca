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

    public void insertPrestito(int idLibro, int idCliente) throws SQLException {

        PreparedStatement ps = getConnection().prepareStatement("INSERT INTO prestito(id_libro, id_cliente) VALUES (?,?)");

        ps.setInt(1, idLibro);
        ps.setInt(2, idCliente);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
    }

    public void deletePrestito(int idLibro, int idCliente) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("DELETE from prestito where id_libro = ? and id_cliente = ? ");

        ps.setInt(1,idLibro);
        ps.setInt(2,idCliente);

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

            String idUtente = rs.getString("id_cliente");
            String idLibro = rs.getString("id_libro");
            LocalDate dataPrestito = rs.getTimestamp("data_insert").toLocalDateTime().toLocalDate();

            Prestito prestito = new Prestito();
            prestito.setIdLibro(idLibro);
            prestito.setIdUtente(idUtente);
            prestito.setDataPrestito(dataPrestito);

            prestiti.add(prestito);

        }

        return prestiti;

    }

    public Optional<Prestito> getPrestitoPerIdLibro(String idLibro) throws SQLException {
        Prestito prestito = new Prestito();
        Optional<Prestito> prestito1 = null;

        PreparedStatement ps = getConnection().prepareStatement("SELECT * from prestito where id_libro = ?");
        ps.setString(1,idLibro);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String idUtente = rs.getString("id_cliente");
            LocalDate dataPrestito = rs.getTimestamp("data_insert").toLocalDateTime().toLocalDate();

            prestito.setIdLibro(idLibro);
            prestito.setIdUtente(idUtente);
            prestito.setDataPrestito(dataPrestito);

            prestito1 = Optional.of(prestito);

        }

        return prestito1;

    }



}
