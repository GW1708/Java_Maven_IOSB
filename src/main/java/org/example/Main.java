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
            String sql = "INSERT INTO user (firstName, lastName, age) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, rootNode.get("firstName").asText());
            preparedStatement.setString(2, rootNode.get("lastName").asText());
            preparedStatement.setInt(3, rootNode.get("age").asInt());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
