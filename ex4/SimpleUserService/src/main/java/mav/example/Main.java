package mav.example;

import mav.example.dao.UserDao;
import mav.example.dao.UserDaoImpl;
import mav.example.service.UserService;
import mav.example.service.UserServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        DataSource dataSource = context.getBean(DataSource.class);
        UserDao userDao = new UserDaoImpl(dataSource);
        UserService userService = new UserServiceImpl(userDao);

        userService.registerUser("Alex777");
        userService.registerUser("Vasya12");
        userService.registerUser("Petya2");

        List<User> users = userService.getAllUsers();
        users.forEach(System.out::println);

        if (!users.isEmpty()) {
            User user = users.get(1);
            userService.renameUser(user.getId(), "Sasha17");
            System.out.println("После переименования: ");
            userService.getAllUsers().forEach(System.out::println);
        }

        if (!users.isEmpty()) {
            User user = users.get(2);
            userService.deleteUser(user.getId());
            System.out.println("После удаления: ");
            userService.getAllUsers().forEach(System.out::println);
        }
        context.close();
    }
}
