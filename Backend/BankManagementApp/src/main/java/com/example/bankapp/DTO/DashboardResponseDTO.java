package com.example.bankapp.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResponseDTO {

	private List<TransactionDTO> transaction;
	  private double totalCredited;
	    private double totalDebited;
	    private int creditScore;
	    private boolean hasCreditCard;

}
