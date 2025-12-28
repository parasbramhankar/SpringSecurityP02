package com.example.SpringSecurityP02.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class TestController {


    @GetMapping("/contactUs")
    public String contactUs(){
        return "This is the contact us page";
    }

    @GetMapping("/aboutUs")
    public String aboutUs(){
        return "This is the about us page ";
    }

    @GetMapping("/updates")
    public String updates(){
        return "This is the update page";
    }

    @GetMapping("/transfer")
    public String transfer(){
        return "This is the transfer page";
    }

    @GetMapping("/transactions")
    public String printAllTransactions(){
        return "printing transactions...";
    }

    @GetMapping("/getBalance")
    public String getBalance(){
        return "This is the getBalance page";
    }

}
