package com.mainacad.dao;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

class ConnectionToDB {
  private static BasicDataSource dataSource = new BasicDataSource();

  private static Logger logger = Logger.getLogger(ConnectionToDB.class.getName());
  private static final String DB_URL = "jdbc:postgresql://localhost:5432/shop_db_ma";
  private static final String DB_USER = "postgres";
  private static final String DB_PASS = "qwerty";

  static {
    dataSource.setUrl(DB_URL);
    dataSource.setUsername(DB_USER);
    dataSource.setPassword(DB_PASS);
    dataSource.setMinIdle(5);
    dataSource.setMaxIdle(10);
    dataSource.setMaxOpenPreparedStatements(100);
  }

  protected static Connection getConnection() {

    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    Connection connection = null;

    try {
      // All tests running about 8 secs without connection pool
      // And all tests running about 0.5 sec with connection pool

      //connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
      connection = dataSource.getConnection();
    } catch (SQLException e) {
      logger.severe("Unable to get connection to Data Base!");
    }

    return connection;
  }

  private ConnectionToDB() {
  }
}
