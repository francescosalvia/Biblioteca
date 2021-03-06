package dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import data.Cliente;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDao extends DatabaseDao {


    public void insertCliente(String nome, String cognome, Date data, String luogoNascita, String residenza, String email, String numeroTelefono) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("INSERT INTO cliente(nome,  cognome,  data_nascita,  luogo_nascita,  residenza,  email, telefono) VALUES (?,?,?,?,?,?,?)");
        ps.setString(1, nome);
        ps.setString(2, cognome);
        ps.setDate(3, data);
        ps.setString(4, luogoNascita);
        ps.setString(5, residenza);
        ps.setString(6, email);
        ps.setString(7, numeroTelefono);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
    }

    public Optional<Integer> getIdCliente(String email) throws SQLException {

        Optional<Integer> idCliente = Optional.empty();
        PreparedStatement ps = getConnection().prepareStatement("SELECT id_cliente FROM cliente where email = ?");

        ps.setString(1, email);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            idCliente = Optional.of(rs.getInt("id_cliente"));
        }

        return idCliente;
    }


    public void updateCliente(String nome, String cognome, Date data, String luogoNascita, String residenza, String email, String numeroTelefono, int idCliente) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("UPDATE cliente SET nome = ?,  cognome = ?,  data_nascita = ?,  luogo_nascita = ?,  residenza = ?,  email = ?, telefono = ? where id_cliente = ?");
        ps.setString(1, nome);
        ps.setString(2, cognome);
        ps.setDate(3, data);
        ps.setString(4, luogoNascita);
        ps.setString(5, residenza);
        ps.setString(6, email);
        ps.setString(7, numeroTelefono);
        ps.setInt(8, idCliente);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
    }

    public void updateTelefonoResidenza(String variabile, String tipo) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("UPDATE cliente SET " + tipo + " = ? ");
        ps.setString(1, variabile);


        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
    }


    public List<Cliente> getClienti() throws SQLException {

        List<Cliente> clienti = new ArrayList<>();

        PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM cliente");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            int idCliente = rs.getInt("id_cliente");
            String nome = rs.getString("nome");
            String cognome = rs.getString("cognome");
            String luogoNascita = rs.getString("luogo_nascita");
            Date date = rs.getDate("data_nascita");
            String email = rs.getString("email");
            String residenza = rs.getString("residenza");
            String telefono = rs.getString("telefono");


            Cliente cliente = new Cliente(nome, cognome, date, luogoNascita, residenza, email, telefono);
            cliente.setIdCliente(idCliente);
            clienti.add(cliente);
        }
        return clienti;
    }

    public Optional<Cliente> getClientiPerId(int idCliente) throws SQLException {

        Cliente cliente;
        Optional<Cliente> cliente1 = null;

        PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM cliente where id_cliente = ?");
        ps.setInt(1,idCliente);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String nome = rs.getString("nome");
            String cognome = rs.getString("cognome");
            String luogoNascita = rs.getString("luogo_nascita");
            Date date = rs.getDate("data_nascita");
            String email = rs.getString("email");
            String residenza = rs.getString("residenza");
            String telefono = rs.getString("telefono");


            cliente = new Cliente(nome, cognome, date, luogoNascita, residenza, email, telefono);
            cliente.setIdCliente(idCliente);
            cliente1 = Optional.of(cliente);
        }
        return cliente1;
    }


}
