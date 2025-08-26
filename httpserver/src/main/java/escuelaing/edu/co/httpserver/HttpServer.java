/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package escuelaing.edu.co.httpserver;

/**
 *
 * @author jaider.vargas-n
 */
import escuelaing.edu.co.httpserver.domain.User;
import escuelaing.edu.co.httpserver.services.UserService;
import java.net.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import escuelaing.edu.co.httpserver.DTOs.CreateUserDTO;
import escuelaing.edu.co.httpserver.exceptions.UserNotFoundException;

public class HttpServer {

    private static final Map<String, String> mimeTypes = new HashMap<String, String>() {
        {
            put("html", "text/html");
            put("css", "text/css");
            put("js", "application/javascript");
            put("png", "image/png");
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
        }
    };
    public static Map<String, Service> services = new HashMap<String, Service>();
    private static final UserService userService = new UserService();
    private static Path basePath;

    public static void startServer(String[] args) throws IOException, Exception {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        Boolean running = true;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            Boolean isFirstLine = true;
            URI requestUri = null;
            String method = null;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (isFirstLine) {
                    isFirstLine = false;
                    method = inputLine.split(" ")[0];
                    requestUri = new URI(inputLine.split(" ")[1]);
                    System.out.println("Path: " + requestUri.getPath());
                }
                if (!in.ready()) {
                    break;
                }
            }
            if (requestUri.getPath().startsWith("/api")) {
                out.write(processRequest(requestUri, method).getBytes());
            } // READ FROM DISK
            else {
                readFileService(requestUri, out);
            }
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static String getUser(URI requestUri) {
        String response = null;
        try {
            Long userId = Long.parseLong(requestUri.getQuery().split("=")[1]);
            User user = UserService.getUser(userId);
            String json = user.toString();
            response = getApiResponse("200", json);
        } catch (UserNotFoundException e) {
            String message = "{\"message\":\"" + e.getMessage() + "\"}";
            response = getApiResponse("404", message);
        } catch (Exception e) {
            String message = "{\"message\":\"" + e.getMessage() + "\"}";
            response = getApiResponse("500", message);
        }
        return response;
    }

    public static String getApiResponse(String statusCode) {
        String message = getMessageByCode(statusCode);
        return "HTTP/1.1 " + statusCode + " " + message + "\r\n"
                + "Content-Type: application/json\r\n"
                + "Content-Length: 0\r\n"
                + "Connection: close\r\n"
                + "\r\n";
    }

    public static String getApiResponse(String statusCode, String body) {
        String message = getMessageByCode(statusCode);
        byte[] bytes = body.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return "HTTP/1.1 " + statusCode + " " + message + "\r\n"
                + "Content-Type: application/json\r\n"
                + "Content-Length: " + bytes.length + "\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + body;
    }

    private static String getMessageByCode(String statusCode) {
        switch (statusCode) {
            case "200":
                return "OK";
            case "404":
                return "Not Found";
            case "500":
                return "Internal Server Error";
            default:
                return "";
        }

    }

    private static void readFileService(URI requestUri, OutputStream out) throws IOException {
        String fileName = requestUri.getPath();
        String output = "";

        if (fileName.equals("/")) {
            fileName = "index.html";
        }

        Path filePath = Paths.get(basePath.toString(), fileName);
        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
            output = "HTTP/1.1 404 Not Found\n\r"
                    + "content-type: text/html\n\r"
                    + "\n\r"
                    + "<h1>File not found 404</h1>";
            byte[] body = "<h1>File not found 404</h1>".getBytes(java.nio.charset.StandardCharsets.UTF_8);

            out.write(output.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
            out.write(body);
            out.flush();
            return;
        } else {
            String mymeType = getMymeType(filePath.getFileName().toString());
            byte[] fileBytes = Files.readAllBytes(filePath);
            output = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: " + mymeType + "\r\n"
                    + "Content-Length: " + fileBytes.length + "\r\n"
                    + "\r\n";
            out.write(output.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
            out.write(fileBytes);
            out.flush();

        }

    }

    private static String getMymeType(String fileName) {
        String[] parts = fileName.split("\\.");
        String extention = parts[parts.length - 1];
        String mymeType = mimeTypes.get(extention);
        if (mymeType == null || mymeType.isEmpty()) {
            mymeType = "application/octet-stream";
        }
        return mymeType;
    }

    public static void get(String route, Service s) {
        services.put(route, s);
    }

    public static void staticfiles(String route) {
        basePath = Paths.get(route).toAbsolutePath().normalize();
    }

    private static String processRequest(URI requestUri, String method) {
        //String outputLine = "";
        String serviceRoute = requestUri.getPath().substring(4);
        HttpRequest request = new HttpRequest(requestUri);
        HttpResponse response = new HttpResponse();
        Service service = services.get(serviceRoute);

        //  switch (method) {
        //case "GET":
        //outputLine = getUser(requestUri);
        //  break;
        //case "POST":
        //outputLine = createUser(requestUri);
        //  break;
        //case "DELETE":
        //outputLine = deleteUser(requestUri);
        //  break;
        //default:
        //outputLine = "HTTP/1.1 404 Not Found\n\r"
        //          + "content-type: text/html\n\r"
        //            + "\n\r"
        //              + "<h1>Resource not found 404</h1>";
        //}
        return service.executeService(request, response);

    }
}
