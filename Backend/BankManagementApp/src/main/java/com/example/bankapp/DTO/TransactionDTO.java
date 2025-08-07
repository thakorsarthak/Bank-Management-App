package com.example.bankapp.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TransactionDTO {
	 private LocalDateTime timestamp;
	    private String type;
	    private Double amount;
	    private String description;
	    private Double beforeBalance;
	    private Double afterBalance;
}
