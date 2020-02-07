package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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


}
