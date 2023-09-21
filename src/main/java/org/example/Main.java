package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("data.json"));

            String jdbcUrl = "jdbc:mysql://localhost:3306/iosb_db";
            String username = "IOSB";
            String password = "Frosted-Barrette4-Revisable";

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            write_data(connection, rootNode);

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write_data(Connection connection, JsonNode rootNode) {
        // check if address already exists. If not write address data
        write_address_data(connection, rootNode.get("address"));
        Integer address_id = 1;  // Rewrite using a reading method
        write_user_data(connection, rootNode.get("user"), address_id);
        Integer user_id = 1;  // Rewrite using a reading method
        write_phone_data(connection, rootNode.get("phoneNumber"), user_id);
    }

    public static void write_address_data(Connection connection, JsonNode address_node) {
        try {
            String sql = "INSERT INTO address (streetAddress, city, state, postalCode) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, address_node.get("streetAddress").asText());
            preparedStatement.setString(2, address_node.get("city").asText());
            preparedStatement.setString(3, address_node.get("state").asText());
            preparedStatement.setInt(4, address_node.get("postalCode").asInt());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void write_user_data(Connection connection, JsonNode rootNode, Integer address_id ) {
        try {
            String sql = "INSERT INTO user (firstName, lastName, age, address_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, rootNode.get("firstName").asText());
            preparedStatement.setString(2, rootNode.get("lastName").asText());
            preparedStatement.setString(2, rootNode.get("lastName").asText());
            preparedStatement.setInt(4, address_id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void write_phone_data(Connection connection, JsonNode phone_node, Integer user_id) {
        try {
            String sql = "INSERT INTO phone (type, number, user_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Refactor to loop over all phone entries instead
            preparedStatement.setString(1, phone_node.get(0).get("type").asText());
            preparedStatement.setString(2, phone_node.get(0).get("number").asText());
            preparedStatement.setInt(3, user_id);

            preparedStatement.setString(1, phone_node.get(1).get("type").asText());
            preparedStatement.setString(2, phone_node.get(1).get("number").asText());
            preparedStatement.setInt(3, user_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
