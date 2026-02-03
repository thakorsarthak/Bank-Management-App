package com.example.bankapp.DTO;

import java.time.Instant;

import com.example.bankapp.entity.Account;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDTO {

	private Long id;
	private String holderName;
	private Long contact;
	private String email;
	private AccountStatus status;
	private String branch;
	private Instant joiningDate;
	private AccountType type;
	private String accountNumber;

	public static UserListDTO from(Account account) {
		UserListDTO dto = new UserListDTO();
		dto.setId(account.getId());
		dto.setContact(account.getContact());
		dto.setEmail(account.getEmail());
		dto.setHolderName(account.getAccountHolderName());
		dto.setJoiningDate(account.getCreatedAt());
		dto.setStatus(account.getStatus());
		dto.setBranch(account.getBranch().getBranchName());
		dto.setJoiningDate(account.getCreatedAt());
		dto.setType(account.getAccountType());

		dto.setAccountNumber(mask(account.getAccountNumber()));

		return dto;

	}
	private static String mask(String acc) {
	    if (acc == null || acc.length() < 4) {
			return acc;
		}
	    return "XXXXXX" + acc.substring(acc.length() - 4);
	}


}
