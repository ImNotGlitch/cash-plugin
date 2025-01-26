package com.github.imnotglitch.database;

import com.github.imnotglitch.cache.User;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class Repository {

    private static final String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS `cash` (" +
            " name VARCHAR(64) NOT NULL PRIMARY KEY," +
            " cash INTEGER NOT NULL" +
            ");";

    protected final HikariDataSource source = new HikariDataSource();


    public Repository(String host, int port, String database, String user, String password) {
        source.setPoolName("Cash - User Repository");
        source.setMaxLifetime(1800000L);

        source.setMaximumPoolSize(10);
        source.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        source.setUsername(user);
        source.setPassword(password);

        source.addDataSourceProperty("autoReconnect", "true");
        source.addDataSourceProperty("useSLL", "false");
        source.addDataSourceProperty("characterEncoding", "utf-8");
        source.addDataSourceProperty("encoding", "UTF-8");
        source.addDataSourceProperty("useUnicode", "true");
        source.addDataSourceProperty("rewriteBatchedStatements", "true");
        source.addDataSourceProperty("jdbcCompliantTruncation", "false");
        source.addDataSourceProperty("cachePrepStmts", "true");
        source.addDataSourceProperty("prepStmtCacheSize", "275");
        source.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }


    public Connection getConnection() {
        try {
            return source.getConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void create(User user) {
        CompletableFuture.runAsync(() -> {

            try (Connection connection = getConnection()) {

                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `cash` VALUES(?,?);")) {

                    preparedStatement.setString(1, user.getName());
                    preparedStatement.setInt(2, user.getCash());

                    preparedStatement.execute();

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        });
    }

    public void update(User user) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection()) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `cash` SET cash=? WHERE name=?")) {
                    preparedStatement.setInt(1, user.getCash());
                    preparedStatement.setString(2, user.getName());
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public User search(String name) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `cash` WHERE name=?;")) {
                preparedStatement.setString(1, name);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!resultSet.next()) return  null;

                    final int cash = resultSet.getInt("cash");

                    return new User(name, cash);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    public void init() {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
                statement.execute();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
    public void closeConnection() {
        source.close();
    }
}
