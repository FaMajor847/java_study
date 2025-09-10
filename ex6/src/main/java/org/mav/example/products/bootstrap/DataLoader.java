package org.mav.example.products.bootstrap;

import org.mav.example.products.domain.AccountType;
import org.mav.example.products.domain.Note;
import org.mav.example.products.domain.Product;
import org.mav.example.products.domain.User;
import org.mav.example.products.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository users;

    public DataLoader(UserRepository users) {
        this.users = users;
    }

    @Override
    @Transactional
    public void run(String... args) {
        //тестовый пользователь
        User user = new User();
        user.setUsername("Alex_01");

        // Добавим заметки
        Note note1 = new Note();
        note1.setText("note 1");
        user.addNote(note1);

        Note note2 = new Note();
        note2.setText("note 2");
        user.addNote(note2);

        // Добавим продукты
        Product product1 = new Product();
        product1.setAccountNumber("ACCOUNT-01");
        product1.setBalance(new BigDecimal("9999.99"));
        product1.setType(AccountType.ACCOUNT.getValue());
        user.addProduct(product1);

        Product product2 = new Product();
        product2.setAccountNumber("CARD-01");
        product2.setBalance(new BigDecimal("205000.50"));
        product2.setType(AccountType.CARD.getValue());
        user.addProduct(product2);

        users.save(user);
    }
}