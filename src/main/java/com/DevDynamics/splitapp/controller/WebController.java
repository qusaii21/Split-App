package com.DevDynamics.splitapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/add-expense")
    public String addExpense() {
        return "add-expense";
    }

    @GetMapping("/add-person")
    public String addPerson() {
        return "add-person";
    }
}
