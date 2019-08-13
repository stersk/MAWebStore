package com.mainacad.dao;

import com.mainacad.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OrderDAO {

    private static Logger logger = Logger.getLogger(OrderDAO.class.getName());
    public static Order create(Order order){
        String sql = "INSERT INTO orders(item_id, amount, cart_id) VALUES(?,?,?)";
        String sequenceSql = "SELECT currval(pg_get_serial_sequence('orders','id'))";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             PreparedStatement seqStatement = connection.prepareStatement(sequenceSql)
             ){

            preparedStatement.setInt(1, order.getItemId());
            preparedStatement.setInt(2, order.getAmount());
            preparedStatement.setInt(3, order.getCartId());

            preparedStatement.executeUpdate();

            ResultSet resultSet = seqStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                order.setId(id);

                return order;
            }
        } catch (SQLException e){
            logger.severe(e.getMessage());
        }

        return null;
    }

    public static Order findById(Integer id){
        String statement = "SELECT * FROM orders WHERE id=?";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();

                order.setId(resultSet.getInt("id"));
                order.setItemId(resultSet.getInt("item_id"));
                order.setAmount(resultSet.getInt("amount"));
                order.setCartId(resultSet.getInt("cart_id"));

                return order;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Order> findByCart(Integer cartId){
        List<Order> orders = new ArrayList<>();
        String statement = "SELECT * FROM orders WHERE cart_id=?";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, cartId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();

                order.setId(resultSet.getInt("id"));
                order.setItemId(resultSet.getInt("item_id"));
                order.setAmount(resultSet.getInt("amount"));
                order.setCartId(resultSet.getInt("cart_id"));

                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }



    public static void delete(Integer id){
        String statement = "DELETE FROM orders WHERE id=?";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Order> findClosedOrdersByUserAndPeriod(Integer userId, Long from, Long to) {
        String sql = "SELECT o.id, o.item_id, o.amount, o.cart_id FROM orders o " +
                "JOIN carts c ON o.cart_id=c.id " +
                "WHERE " +
                "c.user_id=? AND " +
                "c.creation_time>=? AND " +
                "c.creation_time<=? " +
                "ORDER BY c.creation_time";

        return null;
    }
}
