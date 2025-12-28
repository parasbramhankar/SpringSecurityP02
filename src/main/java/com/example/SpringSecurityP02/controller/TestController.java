package com.example.SpringSecurityP02.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class TestController {

    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String test() {
        return "this is test class";
    }

    @GetMapping("/contactUs")
    @PreAuthorize("hasRole('USER')")
    public String contactUs() {
        return "This is the contact us page";
    }

    @GetMapping("/aboutUs")
    @PreAuthorize("hasRole('USER')")
    public String aboutUs() {
        return "This is the about us page";
    }

    @GetMapping("/updates")
    @PreAuthorize("hasRole('USER')")
    public String updates() {
        return "This is the update page";
    }

    @GetMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public String transfer() {
        return "This is the transfer page";
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String printAllTransactions() {
        return "printing transactions...";
    }

    @GetMapping("/getBalance")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String getBalance() {
        return "This is the getBalance page";
    }
}
