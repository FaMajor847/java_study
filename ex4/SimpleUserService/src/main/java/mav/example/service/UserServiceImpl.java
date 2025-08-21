package mav.example.service;

import mav.example.User;
import mav.example.dao.UserDao;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void registerUser(String userName) {
        User newUser = new User();
        newUser.setUsername(userName);
        userDao.create(newUser);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public void renameUser(Long id, String newName) {
        Optional<User> userOpt = userDao.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(newName);
            userDao.update(user);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}
