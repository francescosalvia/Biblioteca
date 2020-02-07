package dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class ClienteDao {

    private static Connection con;


    private static Connection getConnection() throws SQLException {
        if (con == null) {
            MysqlDataSource dataSource = new MysqlDataSource();

            dataSource.setServerName("127.0.0.1");
            dataSource.setPortNumber(3306);
            dataSource.setUser("root");
            dataSource.setPassword("root");
            dataSource.setDatabaseName("biblioteca");

            con = dataSource.getConnection();
        }

        return con;
    }


    public void insertCliente(String nome, String cognome, LocalDateTime data, String luogoNascita, String residenza, String email, String numeroTelefono) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("INSERT INTO cliente(nome,  cognome,  data_nascita,  luogo_nascita,  residenza,  email, telefono) VALUES (?,?,?,?,?,?,?)");
        ps.setString(1, nome);
        ps.setString(2, cognome);
        Timestamp dataNascita = Timestamp.valueOf(data);
        ps.setTimestamp(3, dataNascita);
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

        ps.setString(1,email);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            idCliente = Optional.of(rs.getInt("id_cliente"));
        }

        return idCliente;
    }








}
