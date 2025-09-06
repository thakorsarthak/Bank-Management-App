package com.example.bankapp.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts", 
uniqueConstraints =  {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "contact"),
        @UniqueConstraint(columnNames = "aadhaar_number"),
        @UniqueConstraint(columnNames = "pan_number")
    })
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber; // 12-digit generated account number

    @Column(name = "holder_name")
    private String accountHolderName;

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, name = "password")
    private String password;
    
    @Column(nullable = false , name="transaction_pin")
    private String pin;

    @Column(nullable = false, unique = true)
    private Long contact;

 //   @Column( name="pan_number", nullable = false, length = 20 )
    @NotBlank(message = "PAN is required")
    @Pattern(
        regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$",
        message = "Invalid PAN format"
    )
    private String panNo;

    //@Column(nullable = true , name="aadhaar_number")
    @NotBlank(message = "Aadhaar number is required")
    @Pattern(
        regexp = "^[0-9]{12}$",
        message = "Aadhaar must be 12 digits"
    )
    private String aadhaarNo;
    
    @Column(name = "branch_code", nullable = false, length = 4)
    private String branchCode;  //  "1001" for Ahmedabad

    @Column(name = "product_code", nullable = false, length = 2)
    private String productCode; // 01 for savings, 02 for current , 03 Student , O4 Senior Citizen , 05 salary 
    
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
}

