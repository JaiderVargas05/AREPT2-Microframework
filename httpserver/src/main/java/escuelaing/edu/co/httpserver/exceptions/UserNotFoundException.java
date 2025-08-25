/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package escuelaing.edu.co.httpserver.exceptions;

/**
 *
 * @author Jaider Vargas
 */
public class UserNotFoundException extends RuntimeException {
    private static String USER_NOT_FOUND = "User with id @@id not found";
    public UserNotFoundException(Long userId) {
        super(USER_NOT_FOUND.replace("@@id", userId.toString()));
    }
    
}
