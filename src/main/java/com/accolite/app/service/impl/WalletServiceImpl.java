package com.accolite.app.service.impl;

import com.accolite.app.entity.UsersEntity;
import com.accolite.app.entity.WalletEntity;
import com.accolite.app.enumType.PaymentType;
import com.accolite.app.enumType.Status;
import com.accolite.app.handler.ExceptionHandler;
import com.accolite.app.repository.UsersRepository;
import com.accolite.app.repository.WalletRepository;
import com.accolite.app.service.WalletService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UsersRepository usersRepository;


    @Override
    public List<WalletEntity> getAllWalletDetails() {
        return walletRepository.findAll();
    }

    @Override
    @Transactional
    public void saveWallet(WalletEntity wallet, int role_id) {
        if (wallet != null) {
            if (role_id == 2) {
                Long uniqueCode = generateUniqueCode();
                List<Long> unique_code = new ArrayList<>();
                unique_code.add(uniqueCode);
                wallet.setUniqueCodes(unique_code);
                wallet.setAmount(1000.0);
            }
            else
                wallet.setStatus(Status.APPROVED);
            walletRepository.save(wallet);
        }
        else
            throw new ExceptionHandler(HttpStatus.NOT_FOUND,"NULL Value");
    }

    @Override
    @Transactional
    public ResponseEntity<String> addMoney(Long id, Double amount) {
        WalletEntity wallet = walletRepository.findById(id).orElse(null);
        if (wallet != null) {
            UsersEntity usersEntity = usersRepository.findById(id).get();
            if (usersEntity.getRole().ordinal() == 2) {
                if (wallet.getStatus().ordinal() != 0) {
                    Double newAmount = wallet.getAmount() + amount;
                    wallet.setAmount(newAmount);
                    walletRepository.save(wallet);
                    return new ResponseEntity<>("Amount Added", HttpStatus.CREATED);
                } else
                    throw new ExceptionHandler(HttpStatus.NOT_IMPLEMENTED,"Inactive Account");
            } else
                throw new ExceptionHandler(HttpStatus.NOT_IMPLEMENTED,"Only User can add money");
        }
        else
            throw new ExceptionHandler(HttpStatus.NOT_FOUND,"No User Exists");
    }

    @Override
    @Transactional
    public ResponseEntity<String> setWalletStatus(Long id) {
        WalletEntity wallet = walletRepository.findById(id).orElse(null);
        if (wallet != null) {
            if (wallet.getStatus().ordinal() == 1)
                throw new ExceptionHandler(HttpStatus.NOT_IMPLEMENTED,"Wallet Already activated");
            wallet.setStatus(Status.APPROVED);
            walletRepository.save(wallet);
            return new ResponseEntity<>("Wallet Activated", HttpStatus.ACCEPTED);
        }
        else
            throw new ExceptionHandler(HttpStatus.NOT_FOUND,"No User Exists");
    }

    @Override
    @Transactional
    public ResponseEntity<String> changePaymentType(Long id) {
        WalletEntity wallet = walletRepository.findById(id).orElse(null);
        if (wallet != null) {
            UsersEntity usersEntity = usersRepository.findById(id).get();
            if (usersEntity.getRole().ordinal() == 2) {
                if (wallet.getStatus().ordinal() != 0 && wallet.getAmount()>=5000) {
                    int paymentType =  wallet.getPaymentType().ordinal();
                    if (paymentType == 1) {
                        Long uniqueCode = generateUniqueCode();
                        List<Long> unique_code = new ArrayList<>();
                        unique_code.add(uniqueCode);
                        wallet.setUniqueCodes(unique_code);
                        wallet.setPaymentType(PaymentType.ONLINE);
                        wallet.setStatus(Status.NOT_APPROVED);
                    } else {
                        Set<Long> unique_CodeSet = new HashSet<>();
                        while (true) {
                            if (unique_CodeSet.size() == 5)
                                break;
                            unique_CodeSet.add(generateUniqueCode());
                        }
                        wallet.setUniqueCodes(new ArrayList<>(unique_CodeSet));
                        wallet.setPaymentType(PaymentType.OFFLINE);
                    }

                    walletRepository.save(wallet);
                    return new ResponseEntity<>("Payment Updated to " + (paymentType == 0 ? "Offline" : "Online"), HttpStatus.CREATED);
                } else
                    throw new ExceptionHandler(HttpStatus.NOT_IMPLEMENTED,"Wallet Not Activated or Check the Wallet Balance");

            } else
                throw new ExceptionHandler(HttpStatus.NOT_MODIFIED,"Only User can change Payment type");
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public void updateWallet(Long vendorId, Long userId, Double amount) {
        WalletEntity userWallet = walletRepository.findById(userId).get();
        WalletEntity vendorWallet = walletRepository.findById(vendorId).get();
        double userNewAmount = userWallet.getAmount() - amount;
        double vendorNewAmount = vendorWallet.getAmount() + amount;
        userWallet.setAmount(userNewAmount);
        vendorWallet.setAmount(vendorNewAmount);
        walletRepository.save(userWallet);
        walletRepository.save(vendorWallet);
    }

    @Override
    @Transactional
    public void updateWallet(Long userId, Double amount) {
        WalletEntity userWallet = walletRepository.findById(userId).get();
        UsersEntity adminEntity = usersRepository.findAll().stream().filter(x -> x.getRole().ordinal() == 0).collect(Collectors.toList()).stream().findFirst().get();
        if(adminEntity!=null) {
            WalletEntity adminWallet = walletRepository.findById(adminEntity.getId()).get();
            double adminNewAmount = adminWallet.getAmount() + amount;
            double userNewAmount = userWallet.getAmount() - amount;
            userWallet.setAmount(userNewAmount);
            adminWallet.setAmount(adminNewAmount);
            walletRepository.save(userWallet);
            walletRepository.save(adminWallet);
        }
        else
            throw new ExceptionHandler(HttpStatus.NOT_FOUND,"No Admin Exists");
    }



    private Long generateUniqueCode() {
        return new Random().nextLong(100000, 300000);
    }
}
