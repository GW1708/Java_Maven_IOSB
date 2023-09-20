package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("data.json"));

            // Access JSON data as needed
            String firstName = rootNode.get("firstName").asText();
            String lastName = rootNode.get("lastName").asText();
            int age = rootNode.get("age").asInt();

            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
            System.out.println("Age: " + age);

            // Further processing of the data or storing it in a database
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
