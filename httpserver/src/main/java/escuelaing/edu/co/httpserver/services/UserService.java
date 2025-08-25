/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package escuelaing.edu.co.httpserver.services;

import escuelaing.edu.co.httpserver.DTOs.CreateUserDTO;
import escuelaing.edu.co.httpserver.domain.User;
import escuelaing.edu.co.httpserver.exceptions.UserNotFoundException;
import escuelaing.edu.co.httpserver.repository.UserRepository;

/**
 *
 * @author User
 */
public class UserService {
    private final static UserRepository repository = new UserRepository();
    
    public static User getUser(Long userId) throws UserNotFoundException{
        User user = repository.getUser(userId);
        if (user == null) throw new UserNotFoundException(userId);
        return user;
    }
    
    public static void deleteUser(Long userId) throws UserNotFoundException{
        Boolean isDeleted = repository.deleteUser(userId);
        if(!isDeleted) throw new UserNotFoundException(userId);
    }
    
    public static User createUser(CreateUserDTO userDTO){
        User user = repository.createUser(userDTO.getName(), userDTO.getAge());
        return user;
    }
    
    
}
