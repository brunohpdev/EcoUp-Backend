package br.com.ecoup.infra;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class ConnectionFactory {

    private DriverManagerDataSource banco;

    public ConnectionFactory() {

        banco = new DriverManagerDataSource();

        banco.setDriverClassName("oracle.jdbc.OracleDriver");
        banco.setUrl("jdbc:oracle:thin:@//oracle.fiap.com.br:1521/ORCL");
        banco.setUsername(System.getenv("DB_USER"));
        banco.setPassword(System.getenv("DB_PASSWORD"));
    }

    @Bean
    public DataSource dataSource() {
        return banco;
    }

    public Connection getConnection() throws SQLException {
        return banco.getConnection();
    }
}