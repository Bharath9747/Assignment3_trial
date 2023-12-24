package com.accolite.app.entity;


import com.accolite.app.enumType.PaymentType;
import com.accolite.app.enumType.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "wallet")
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column()
    @Builder.Default
    private Double amount= Double.valueOf(0);
    @Column(name = "wallet_status")
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private Status status = Status.NOT_APPROVED;
    @Column(name = "payment_type")
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private PaymentType paymentType=PaymentType.ONLINE;
    @ElementCollection
    @CollectionTable(name = "unique_codes", joinColumns = @JoinColumn(name = "wallet_id"))
    @Column(name = "code")
    private List<Long> uniqueCodes;
}
