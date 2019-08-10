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
             PreparedStatement seqStatement = connection.prepareStatement(sequenceSql)){

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

    public static Order update(Order order){
        String statement = "UPDATE orders SET cart_id=?, item_id=?, amount=? WHERE id=?";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, order.getCartId());
            preparedStatement.setInt(2, order.getItemId());
            preparedStatement.setInt(3, order.getAmount());
            preparedStatement.setInt(4, order.getId());

            preparedStatement.executeUpdate();

            return order;

        } catch (SQLException e) {
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
                Order order = getOrderFromResultSetOrder(resultSet);
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
                Order order = getOrderFromResultSetOrder(resultSet);
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

    public static List<Order> findClosedOrdersByUserIdAndPeriod(Integer userId, Long from, Long to) {
        List<Order> orderList = new ArrayList<>();

        String statement = "SELECT o.id, o.item_id, o.amount, o.cart_id FROM orders o " +
                "JOIN carts c ON o.cart_id=c.id " +
                "WHERE " +
                "c.user_id=? AND " +
                "c.closed='1' AND " +
                "c.creation_time>=? AND " +
                "c.creation_time<=? " +
                "ORDER BY c.creation_time";;

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, from);
            preparedStatement.setLong(3, to);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orderList.add(getOrderFromResultSetOrder(resultSet));
            }

            return orderList;

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return null;
    }

    public static Integer getSumOfAllOrdersByUserIdAndPeriod(Integer userId, Long from, Long to){
        String statement = "SELECT SUM(i.price*o.amount) AS total_sum FROM items i " +
                "JOIN orders o ON o.item_id = i.id " +
                "JOIN carts c ON o.cart_id = c.id " +
                "WHERE c.user_id=? AND " +
                "c.creation_time>? AND " +
                "c.creation_time<? AND " +
                "c.closed=true";

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, from);
            preparedStatement.setLong(3, to);

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

    private static Order getOrderFromResultSetOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();

        order.setId(resultSet.getInt("id"));
        order.setItemId(resultSet.getInt("item_id"));
        order.setAmount(resultSet.getInt("amount"));
        order.setCartId(resultSet.getInt("cart_id"));

        return order;
    }
}
