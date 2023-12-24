package com.accolite.app.service;

import com.accolite.app.entity.UsersEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UsersService {
    List<UsersEntity> getAllUsers();
    ResponseEntity<String> approveUser(Long id);

    ResponseEntity<String> activateWallet(Long id);


    ResponseEntity<String> approvePayment(Long id, int option);

}
