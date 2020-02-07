package dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseDao {

    private static Connection con;


    protected static Connection getConnection() throws SQLException {
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






}
