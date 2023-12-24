package com.accolite.app.controller;

import com.accolite.app.entity.PaymentEntity;
import com.accolite.app.entity.UsersEntity;
import com.accolite.app.entity.WalletEntity;
import com.accolite.app.service.PaymentService;
import com.accolite.app.service.UsersService;
import com.accolite.app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")

public class UsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/allusers")
    public List<UsersEntity> getAllUser(){
        return usersService.getAllUsers();
    }
    @GetMapping("/allwallet")
    public List<WalletEntity> getAllWallet(){
        return walletService.getAllWalletDetails();
    }
    @GetMapping("/all")
    public List<PaymentEntity> getAllPayments(){
        return paymentService.getAllPayments();
    }
    @GetMapping("/approve/account/{id}")
    public ResponseEntity<String> approveUser(@PathVariable Long id){
        return usersService.approveUser(id);
    }
    @GetMapping("/activate/{id}")
    public ResponseEntity<String> activateWallet(@PathVariable Long id){
        return usersService.activateWallet(id);
    }
    @GetMapping("/approve/payment/{id}/{option}")
    public ResponseEntity<String> approvePayment(@PathVariable Long id, @PathVariable Byte option){
        return usersService.approvePayment(id,option);
    }

}
