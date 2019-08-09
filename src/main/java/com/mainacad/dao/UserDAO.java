package com.mainacad.dao;

import com.mainacad.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDAO {
    private static Logger logger = Logger.getLogger(OrderDAO.class.getName());

    public static User create(User user) {
        User checkedExistingUser = findByLogin(user.getLogin());
        if (checkedExistingUser != null) {
            logger.warning("User with login " + user.getLogin() + " already exist");
            return null;
        }

        String sql = "INSERT INTO users(login, password, first_name, last_name) " +
                "VALUES(?,?,?,?)";
        String sequenceSQL = "SELECT currval(pg_get_serial_sequence('users','id'))";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             PreparedStatement sequenceStatement = connection.prepareStatement(sequenceSQL)) {

            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());

            preparedStatement.executeUpdate();

            ResultSet resultSet = sequenceStatement.executeQuery();

            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                user.setId(id);
                return user;
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    public static User update(User user) {
        String sql = "UPDATE users SET login=?, password=?, first_name=?, last_name=? WHERE id=?";
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setInt(5, user.getId());

            preparedStatement.executeUpdate();
            return user;
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    public static User findById(Integer id) {

        String sql = "SELECT * FROM users WHERE id=?";
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                return user;
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    public static User findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login=?";
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1,login);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = getUserFromResultSetItem(resultSet);
                return user;
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    public static List<User> findAll() {
        List<User> users = new ArrayList<>();

        String statement = "SELECT * FROM users";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(getUserFromResultSetItem(resultSet));
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return users;
    }

    public static void delete(Integer id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    private static User getUserFromResultSetItem(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));

        return user;
    }
}
