package com.example.bankapp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber; // 12-digit generated account number

    @Column(name = "holder_name", nullable = false)
    private String accountHolderName;

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String pin;

    @Column(nullable = false, unique = true)
    private Long contact;

    @Column(name = "branch_code", nullable = false, length = 4)
    private String branchCode;  //  "1001" for Ahmedabad

    @Column(name = "product_code", nullable = false, length = 2)
    private String productCode; // 01 for savings, 02 for current

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }


}

