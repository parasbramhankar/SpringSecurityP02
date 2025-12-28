package com.example.SpringSecurityP02.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class TestController {

    @GetMapping("/")
    @PreAuthorize("hashRole('ADMIN')")
    public String test(){
        return "this is test class";
    }


    @GetMapping("/contactUs")
    @PreAuthorize("hashRole('USER')")
    public String contactUs(){
        return "This is the contact us page";
    }

    @GetMapping("/aboutUs")
    @PreAuthorize("hashRole('USER')")
    public String aboutUs(){
        return "This is the about us page ";
    }

    @GetMapping("/updates")
    @PreAuthorize("hashRole('USER')")
    public String updates(){
        return "This is the update page";
    }

    @GetMapping("/transfer")
    @PreAuthorize("hashRole('USER')")
    public String transfer(){
        return "This is the transfer page";
    }

    @GetMapping("/transactions")
    @PreAuthorize("hashRole('USER') or hashRole('ADMIN')")
    public String printAllTransactions(){
        return "printing transactions...";
    }

    @GetMapping("/getBalance")
    @PreAuthorize("hashRole('USER') or hashRole('ADMIN')")
    public String getBalance(){
        return "This is the getBalance page";
    }

}
