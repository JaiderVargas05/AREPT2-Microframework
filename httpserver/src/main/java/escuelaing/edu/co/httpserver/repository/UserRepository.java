/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package escuelaing.edu.co.httpserver.repository;

import escuelaing.edu.co.httpserver.domain.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Jaider Vargas
 */
public class UserRepository {
    private Map<Long, User> users = new ConcurrentHashMap<>();
    private AtomicLong seq = new AtomicLong(1);
    
    public UserRepository() {
    createUser("Alice", 25);
    createUser("Bob", 30);
    }
    public User createUser(String name, int age){
        Long userId = seq.getAndIncrement();
        User user = new User (userId,name,age);
        users.put(userId, user);
        return user;
    }
    public Boolean deleteUser(Long userId){
        User user = users.get(userId);
        if (user == null) return false;
        users.remove(userId);
        return true;
    }
    public User getUser(Long userId){
        return users.get(userId);
    }
    
}
