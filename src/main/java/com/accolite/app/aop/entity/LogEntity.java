package com.accolite.app.aop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "log")
public class LogEntity {
    @Id
    @GeneratedValue
    private Long Id;
    private String message;

}
