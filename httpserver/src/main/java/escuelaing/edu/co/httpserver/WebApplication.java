
package escuelaing.edu.co.httpserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import static escuelaing.edu.co.httpserver.HttpServer.get;
import static escuelaing.edu.co.httpserver.HttpServer.getApiResponse;
import static escuelaing.edu.co.httpserver.HttpServer.startServer;
import static escuelaing.edu.co.httpserver.HttpServer.staticfiles;
import escuelaing.edu.co.httpserver.domain.User;
import escuelaing.edu.co.httpserver.exceptions.UserNotFoundException;
import escuelaing.edu.co.httpserver.services.UserService;

/**
 *
 * @author jaider.vargas-n
 */

public class WebApplication {
    public static void main(String[] args) throws Exception {
        staticfiles("src/main/resources/static");
        get("/users", (req, resp) -> {
        String json = "";
        String code = "200";
        try {
            User user = UserService.getUser(Long.parseLong(req.getValue("id")));
            json = new ObjectMapper().writeValueAsString(user);
        } catch (UserNotFoundException e) {
            json = "{\"message\":\"" + e.getMessage() + "\"}";
            code = "404";
        } catch (Exception e) {
            json = "{\"message\":\"" + e.getMessage() + "\"}";
            code = "500";
        }
        return HttpServer.getApiResponse(code, json);});
        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI); 
        });
        startServer(args);

    }
    
    
}
