package ar.edu.utn.frbb.tup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"ar.edu.utn.frbb.tup", "ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}