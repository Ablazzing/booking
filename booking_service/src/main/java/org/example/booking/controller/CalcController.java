package org.example.booking.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("dev")
public class CalcController {

    @GetMapping("/calc")
    public void printNumber() {
        throw new RuntimeException("Ошибка из calc");
    }
}
