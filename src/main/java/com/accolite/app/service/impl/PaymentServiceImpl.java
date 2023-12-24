package com.accolite.app.service.impl;

import com.accolite.app.entity.PaymentEntity;
import com.accolite.app.entity.UsersEntity;
import com.accolite.app.entity.WalletEntity;
import com.accolite.app.enumType.PaymentStatus;
import com.accolite.app.handler.ExceptionHandler;
import com.accolite.app.repository.PaymentRepository;
import com.accolite.app.repository.UsersRepository;
import com.accolite.app.repository.WalletRepository;
import com.accolite.app.service.PaymentService;
import com.accolite.app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public ResponseEntity<String > savePayment(PaymentEntity payment) {
        if (payment != null) {
            Long userId = payment.getUserId();
            Long vendorId = payment.getVendorId();
            UsersEntity userDTO = usersRepository.findById(userId).get();
            UsersEntity vendorDTO = usersRepository.findById(vendorId).get();

            if (userDTO != null && vendorDTO != null) {
                if (userDTO.getRole().ordinal() == 2 && vendorDTO.getRole().ordinal() == 1 && userDTO.getStatus().ordinal() == 1 && vendorDTO.getStatus().ordinal() == 1) {
                    WalletEntity userWallet = walletRepository.findById(userId).get();
                    if (userWallet.getStatus().ordinal() == 1 && userWallet.getAmount()>=payment.getAmount()) {
                        List<Long> uniqueCodes = userWallet.getUniqueCodes();
                        payment.setPaymentType(userWallet.getPaymentType());
                        boolean distanceBetweenVendor = checkFlagged(payment, vendorDTO, uniqueCodes, payment.getUniqueCode());
                        if (distanceBetweenVendor ) {
                            paymentRepository.save(payment);
                            return new ResponseEntity<>("Payment Added. Waiting for Approval from Admin",HttpStatus.CREATED);
                        } else {
                            payment.setStatus(PaymentStatus.FLAGGED);
                            paymentRepository.save(payment);
                            return new ResponseEntity<>("You are far away from Vendor.But Payment Added",HttpStatus.CREATED);
                        }
                    } else
                        throw new ExceptionHandler(HttpStatus.NOT_MODIFIED,"Activate Wallet Or Check Wallet Balance User : " + userId);

                }
            }
        }

        throw new ExceptionHandler(HttpStatus.NOT_FOUND,"Empty Payment");
    }

    @Override
    public ResponseEntity<String> approvePayment(Long id, int option) {
        PaymentEntity payment = paymentRepository.findById(id).orElse(null);
        if (payment != null && payment.getStatus().ordinal()==0) {
                if (option == 1) {
                    payment.setStatus(PaymentStatus.COMPLETED);
                    walletService.updateWallet(payment.getVendorId(), payment.getUserId(), payment.getAmount());
                } else
                    payment.setStatus(PaymentStatus.REJECTED);

            paymentRepository.save(payment);
            return new ResponseEntity<>("Updated in Payment", HttpStatus.ACCEPTED);
        }
        if (payment.getStatus().ordinal() == 3) {
            walletService.updateWallet(payment.getUserId(), payment.getAmount());
            throw new ExceptionHandler(HttpStatus.NOT_FOUND,"Updated Payment As Fraud");
        }
        throw new ExceptionHandler(HttpStatus.NOT_FOUND,"Not Found Or Already Payment Approved");

    }

    private boolean checkFlagged(PaymentEntity paymentDTO, UsersEntity vendorDTO, List<Long> uniqueCodes, Long uniqueCode) {

        if (calculateDistance(paymentDTO.getLattitude(), paymentDTO.getLongitude(), vendorDTO.getLattitude(), vendorDTO.getLongitude()) <= 500) {
            return uniqueCodes.contains(uniqueCode);
        }
        return false;
    }

    static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        int EARTH_RADIUS = 6371;
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * EARTH_RADIUS;

        return distance;
    }

}
