package com.bideafactory.house_reservation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {

    @GetMapping("/health-check")
    public String get() {
        return "OK";
    }
}
