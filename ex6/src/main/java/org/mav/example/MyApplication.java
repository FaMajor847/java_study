package org.mav.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication( scanBasePackages = {
        "org.mav.example.config",
        "org.mav.example.products",
        "org.mav.example.payments",
        "org.mav.example.limits",

}
)
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}