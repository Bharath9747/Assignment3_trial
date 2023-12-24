package com.accolite.app.entity;

import com.accolite.app.enumType.PaymentStatus;
import com.accolite.app.enumType.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")

public class PaymentEntity {
    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private Long id;
    private Long userId;
    @Column(name = "payment_type")
    private PaymentType paymentType;
    @Column(name = "vendor_id")
    private Long vendorId;
    private Double longitude;
    private Double lattitude;
    @Column(name = "unique_code")
    private Long uniqueCode;
    @Column()
    private final Double amount= Double.valueOf(0);
//    0->pending
//    1->completed
//    2->rejected
//    3->flagged
    @Column(name = "payment_status")
    private PaymentStatus status=PaymentStatus.NOT_APPROVED;
    @Version
    private Long versionId;

}
