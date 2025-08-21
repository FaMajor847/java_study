package mav.example.dao;

import mav.example.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    public Optional<User> findById(Long id);

    List<User> findAll();

    void create(User user);
    void update(User user);
    void delete(Long id);

}
