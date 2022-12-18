package org.example.booking.handlers;

import org.example.booking.controller.BookingController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(assignableTypes = {BookingController.class})
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity handle(Exception e) {
        System.out.println("hi");
        return new ResponseEntity("Oops, we have a problems!\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
