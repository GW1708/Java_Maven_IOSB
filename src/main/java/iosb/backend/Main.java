package iosb.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.sql.*;


public class Main {
    public static void main(String[] args) {

        try {
            String jdbcUrl = "jdbc:mysql://localhost:3306/iosb_db";
            String username = "IOSB";
            String password = "Frosted-Barrette4-Revisable";  // This should be a token.

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // write_data(connection, rootNode);  // This can be improved if there is more data to be written.

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter SQL query: ");
            String sql_query = scanner.nextLine(); // Reading user input for the SQL query is dangerous.
            print_data(connection, sql_query);
            scanner.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void print_data(Connection connection, String sqlQuery) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = resultSet.getString(i);
                    System.out.println(columnName + ": " + columnValue);
                }
                System.out.println();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write_data(Connection connection) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(new File("data.json"));

            // If there is more data:
            // Check if address already exists. If not, write address data. Else, retrieve address_id.
            int address_id = write_address_data(connection, rootNode.get("address"));
            System.out.println("Writing address data successful. Primary Key: " + address_id);
            int user_id = write_user_data(connection, rootNode, address_id);
            System.out.println("Writing user data successful. Primary Key: " + user_id);
            int phone_id = write_phone_data(connection, rootNode.get("phoneNumber"), user_id);
            System.out.println("Writing phone data successful. Primary Key: " + phone_id);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int write_address_data(Connection connection, JsonNode address_node) {
        try {
            String sql = "INSERT INTO address (streetAddress, city, state, postalCode) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            String streetAddress = address_node.get("streetAddress").asText();
            String city = address_node.get("city").asText();
            String state = address_node.get("state").asText();
            int postalCode = address_node.get("postalCode").asInt();

            preparedStatement.setString(1, streetAddress);
            preparedStatement.setString(2, city);
            preparedStatement.setString(3, state);
            preparedStatement.setInt(4, postalCode);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("No generated keys returned.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static int write_user_data(Connection connection, JsonNode rootNode, int address_id ) {
        try {
            String sql = "INSERT INTO user (firstName, lastName, age, address_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, rootNode.get("firstName").asText());
            preparedStatement.setString(2, rootNode.get("lastName").asText());
            preparedStatement.setString(2, rootNode.get("lastName").asText());
            preparedStatement.setInt(4, address_id);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("No generated keys returned.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static int write_phone_data(Connection connection, JsonNode phone_node, int user_id) {
        try {
            String sql = "INSERT INTO phone (type, number, user_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // This can be improved to loop over all existing phone entries.
            preparedStatement.setString(1, phone_node.get(0).get("type").asText());
            preparedStatement.setString(2, phone_node.get(0).get("number").asText());
            preparedStatement.setInt(3, user_id);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("No generated keys returned.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
