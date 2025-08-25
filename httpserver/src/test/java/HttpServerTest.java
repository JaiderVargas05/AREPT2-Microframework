
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import escuelaing.edu.co.httpserver.HttpConnection;
import escuelaing.edu.co.httpserver.HttpServer;
import escuelaing.edu.co.httpserver.WebApplication;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jaider Vargas
 */
public class HttpServerTest {
    private static Thread serverThread;
    private static Path filesBasePath = Paths.get("src/main/resources/static").toAbsolutePath().normalize();
    private final HttpConnection http = new HttpConnection();
    private final ObjectMapper mapper = new ObjectMapper();
    

    @BeforeAll
    public static void startServer() throws Exception {
        serverThread = new Thread(() -> {
            try {
                WebApplication.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        
        long start = System.currentTimeMillis();
        boolean up = false;
        while (System.currentTimeMillis() - start < 5000) {
            try {
                HttpURLConnection con = (HttpURLConnection) new java.net.URL("http://localhost:35000/").openConnection();
                con.setRequestMethod("GET");
                if (con.getResponseCode() >= 200) { up = true; break; }
            } catch (IOException e) { Thread.sleep(100); }
        }
        assertTrue(up, "The server is not running");
    }
    @Test
    public void serverIndexConnectionOK() throws IOException {
        HttpURLConnection con = http.stablishConnection("GET", "/");
        assertEquals(200, con.getResponseCode());
        assertTrue(con.getContentType().contains("text/html"));
    }
    @Test
    public void servirIndexHTMLOK() throws IOException {
        String response = http.makeRequest("GET", "/index.html");
        String expected = Files.readString(filesBasePath.resolve("index.html"));
        assertEquals(expected.replaceAll("\\s+", ""), response.replaceAll("\\s+", ""));
    }
    @Test
    public void serverCSSOK() throws IOException {
        HttpURLConnection con = http.stablishConnection("GET", "/styles.css");
        assertEquals(200, con.getResponseCode());
        assertTrue(con.getContentType().contains("text/css"), "Content-Type expected is text/css, was: " + con.getContentType());

        String body = http.makeRequest("GET", "/styles.css");
        String expected = Files.readString(filesBasePath.resolve("styles.css"));
        assertEquals(expected.replaceAll("\\s+", ""), body.replaceAll("\\s+", ""));
    }
    @Test
    public void serverJSOK() throws IOException {
        HttpURLConnection con = http.stablishConnection("GET", "/app.js");
        assertEquals(200, con.getResponseCode());
        assertTrue(con.getContentType().contains("application/javascript"), "Content-Type expected is application/javascript, was: " + con.getContentType());

        String body = http.makeRequest("GET", "/app.js");
        String expected = Files.readString(filesBasePath.resolve("app.js"));
        assertEquals(expected.replaceAll("\\s+", ""), body.replaceAll("\\s+", ""));
    }
    @Test
    public void serverJPGOK() throws IOException {
        HttpURLConnection con = http.stablishConnection("GET", "/james.jpg");
        assertEquals(200, con.getResponseCode());
        assertTrue(con.getContentType().contains("image/jpeg"), "Content-Type expected is image/jpeg, was: " + con.getContentType());

        byte[] body = http.makeRequestBytes("GET", "/james.jpg");
        byte[] expected = Files.readAllBytes(filesBasePath.resolve("james.jpg"));
        assertTrue(body.length > 0 && expected.length > 0);
    }
    @Test
    public void fileNotFound() throws IOException {
        HttpURLConnection con = http.stablishConnection("GET", "/eci.com");
        assertEquals(404, con.getResponseCode());
    }
//// ---------- API /api/users ----------
    @Test
    public void getUserOK() throws IOException {

        String name = "Bob";
        int age = 30;
        String getPath = "/api/users?id=" + 2;
        String body = http.makeRequest("GET", getPath);
        JsonNode got = mapper.readTree(body);
        assertEquals(2, got.get("id").asLong());
        assertEquals(name, got.get("name").asText());
        assertEquals(age, got.get("age").asInt());
    }

    @Test
    public void getUserNotFound() throws IOException {
        HttpURLConnection con = http.stablishConnection("GET", "/api/users?id=999999");
        assertEquals(404, con.getResponseCode());
    }



}
