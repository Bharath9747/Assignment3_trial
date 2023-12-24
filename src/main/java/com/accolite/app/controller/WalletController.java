package com.accolite.app.controller;

import com.accolite.app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")

public class WalletController {
    @Autowired
    private WalletService walletService;


    @GetMapping("/addmoney/{id}/{amount}")
    public ResponseEntity<String> addMoney(@PathVariable Long id,@PathVariable Double amount){
        return walletService.addMoney(id,amount);
    }
    @GetMapping("/changepayment/{id}")
    public ResponseEntity<String> changePayment(@PathVariable Long id){
        return walletService.changePaymentType(id);
    }

}
