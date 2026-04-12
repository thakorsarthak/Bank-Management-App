package com.example.bankapp.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "branch",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "branch_code"),
        @UniqueConstraint(columnNames = "ifsc_code")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_code", length = 4)
    private String branchCode;          // 1001

    @Column(name = "branch_name")
    private String branchName;          // Ahmedabad

    @Column(name = "ifsc_code", length = 11)
    private String ifscCode;            // BANK0AHM001

    @Column(nullable = false)
    private boolean active = true;

    // Optional but future-proof
    private String city;
    private String state;

    // timestamps 
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
