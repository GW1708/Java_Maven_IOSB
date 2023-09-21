import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static iosb.backend.Main.print_data;


public class DataRetrievalIntegrationTest {

    private static Connection testConnection;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @BeforeAll
    public static void dataBaseSetUp() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/iosb_db";
        String username = "IOSB";
        String password = "Frosted-Barrette4-Revisable";

        testConnection = DriverManager.getConnection(jdbcUrl, username, password);
    }

    @BeforeEach
    public void outputStreamSetUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testPrintUserData() {
        String sqlQuery = "SELECT age FROM user WHERE id = '1'";
        print_data(testConnection, sqlQuery);

        String capturedOutput = outputStream.toString().trim();
        String expectedOutput = "age: 25";

        Assertions.assertEquals(expectedOutput, capturedOutput);
    }

    @Test
    public void testPrintAddressData() {
        String sqlQuery = "SELECT postalCode FROM address WHERE id = '1'";
        print_data(testConnection, sqlQuery);

        String capturedOutput = outputStream.toString().trim();
        String expectedOutput = "postalCode: 10021";

        Assertions.assertEquals(expectedOutput, capturedOutput);
    }

    @Test
    public void testPrintPhoneData() {
        String sqlQuery = "SELECT number FROM phone WHERE id = '2'";
        print_data(testConnection, sqlQuery);

        String capturedOutput = outputStream.toString().trim();
        String expectedOutput = "number: 646 555-4567";

        Assertions.assertEquals(expectedOutput, capturedOutput);
    }

    @Test
    public void testJoin() {
        String sqlQuery = "SELECT a.postalCode FROM user INNER JOIN address as a ON a.id=address_id";
        print_data(testConnection, sqlQuery);

        String capturedOutput = outputStream.toString().trim();
        String expectedOutput = "postalCode: 10021";

        Assertions.assertEquals(expectedOutput, capturedOutput);
    }
}
