package com.accolite.app.service;

import com.accolite.app.entity.PaymentEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {
    List<PaymentEntity> getAllPayments();
    ResponseEntity<String > savePayment(PaymentEntity payment);


    ResponseEntity<String> approvePayment(Long id, int option);
}
