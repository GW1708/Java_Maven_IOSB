package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.DriverManager.println;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("data.json"));

            String jdbcUrl = "jdbc:mysql://localhost:3306/iosb_db";
            String username = "IOSB";
            String password = "Frosted-Barrette4-Revisable";

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "INSERT INTO phone (type, number, user_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // preparedStatement.setString(1, rootNode.get("phoneNumber").get(0).get("type").asText());
            // preparedStatement.setString(2, rootNode.get("phoneNumber").get(0).get("number").asText());
            // preparedStatement.setString(3, "1");

            preparedStatement.setString(1, rootNode.get("phoneNumber").get(1).get("type").asText());
            preparedStatement.setString(2, rootNode.get("phoneNumber").get(1).get("number").asText());
            preparedStatement.setString(3, "1");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
