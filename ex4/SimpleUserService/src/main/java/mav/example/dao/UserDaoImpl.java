package mav.example.dao;

import mav.example.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {


    private final DataSource dataSource;

    public UserDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "select id, username from users where id = ?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getLong("id"), resultSet.getString("username")));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Could not find user with id " + id, e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from users";
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                users.add(new User(resultSet.getLong("id"), resultSet.getString("username")));
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not find users", e);
        }
        return users;
    }

    @Override
    public void create(User user) {

        String sql = "insert into users (username) values (?)";
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while creating user", e);
        }

    }

    @Override
    public void update(User user) {
        String sql = "update users set username = ? where id = ?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setLong(2, user.getId());
            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error while updating user", e);
        }

    }

    @Override
    public void delete(Long id) {
        String sql = "delete from users where id = ?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting user", e);
        }
    }
}
