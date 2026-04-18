package com.example.bankapp.DTO;

import java.time.LocalDateTime;

import com.example.bankapp.entity.Transaction;
import com.example.bankapp.enums.TransactionDirection;
import com.example.bankapp.enums.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

	private String type;
    private double amount;
    private double beforeBalance;
    private double afterBalance;
    private String direction;
    private String description;
    private LocalDateTime timestamp;
    private String status;
    private String counterPartyName;

    public static TransactionResponseDTO from(Transaction tx) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setType(tx.getType());
        dto.setAmount(tx.getAmount());
        dto.setBeforeBalance(tx.getBeforebalance());
        dto.setAfterBalance(tx.getAfterbalance());
        dto.setDirection(tx.getDirection().name());
        dto.setDescription(tx.getDescription());
        dto.setTimestamp(tx.getTimestamp());
       // dto.setStatus(tx.getStatus().name().toLowerCase());
        dto.setStatus(tx.getStatus() != null ? tx.getStatus().name() : "UNKNOWN");
        dto.setCounterPartyName(tx.getCounterPartyName());
        return dto;
    }
}
