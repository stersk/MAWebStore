package com.mainacad.dao;

import com.mainacad.model.Cart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CartDAO {
    private static Logger logger = Logger.getLogger(OrderDAO.class.getName());

    /**
     * @param cart must be always open (closed = false)
     * @return stored cart with id
     */
    public static Cart create(Cart cart) {
        String sql = "INSERT INTO carts(creation_time, closed, user_id) VALUES(?,?,?)";
        String sequenceSql = "SELECT currval(pg_get_serial_sequence('carts','id'))";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             Statement seqStatement = connection.createStatement()) {

            preparedStatement.setLong(1, cart.getCreationTime());
            preparedStatement.setBoolean(2, cart.getClosed());
            preparedStatement.setInt(3, cart.getUserId());

            preparedStatement.executeUpdate();

            ResultSet resultSet = seqStatement.executeQuery(sequenceSql);
            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                cart.setId(id);

                return cart;
            }
        } catch (SQLException e){
            logger.severe(e.getMessage());
        }

        return null;
    }

    public static Cart update(Cart cart) {
        String statement = "UPDATE carts SET creation_time=?, closed=?, user_id=? WHERE id=?";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setLong(1, cart.getCreationTime());
            preparedStatement.setBoolean(2, cart.getClosed());
            preparedStatement.setInt(3, cart.getUserId());
            preparedStatement.setInt(4, cart.getId());

            preparedStatement.executeUpdate();

            return cart;

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return null;
    }

    public static Cart findById(Integer id) {
        String statement = "SELECT * FROM carts WHERE id=?";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return getCartFromResultSetItem(resultSet);
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return null;
    }

    public static List<Cart> findByUser(Integer userId) {
        List<Cart> cartList = new ArrayList<>();

        String statement = "SELECT * FROM carts WHERE user_id=?";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cartList.add(getCartFromResultSetItem(resultSet));
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return cartList;
    }

    public static Cart findOpenCartByUser(Integer userId){
        String statement = "SELECT * FROM carts WHERE closed='0' AND user_id=?";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            //preparedStatement.setBoolean(1, false);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return getCartFromResultSetItem(resultSet);
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return null;
    }

    public static void delete(Integer cartId){
        String statement = "DELETE FROM carts WHERE id=?";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, cartId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    private static Cart getCartFromResultSetItem(ResultSet resultSet) throws SQLException {
        Cart cart = new Cart();

        cart.setId(resultSet.getInt("id"));
        cart.setCreationTime(resultSet.getLong("creation_time"));
        cart.setClosed(resultSet.getBoolean("closed"));
        cart.setUserId(resultSet.getInt("user_id"));

        return cart;
    }

    public static Integer getCartSum(Integer cartId){
        String statement = "SELECT SUM(i.price*o.amount) AS total_sum FROM items i " +
                "JOIN orders o ON o.item_id = i.id " +
                "JOIN carts c ON o.cart_id = c.id " +
                "WHERE c.id=?";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, cartId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return  resultSet.getInt("total_sum");
            }

            return null;

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return null;
    }

    public static Cart close(Integer cartId) {
        String sql = "UPDATE carts SET creation_time=?, closed=? WHERE id=?";
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, System.currentTimeMillis());
            preparedStatement.setBoolean(2, true);
            preparedStatement.setInt(3, cartId);
            preparedStatement.executeUpdate();
            Cart cart = findById(cartId);
            return cart;
        } catch (Exception e){
            logger.severe(e.getMessage());
        }
        return null;
    }
}