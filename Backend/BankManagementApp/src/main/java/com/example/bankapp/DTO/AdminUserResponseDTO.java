package com.example.bankapp.DTO;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.bankapp.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponseDTO {

	private List<UserListDTO> users;
	private long totalRecords;

	public  static AdminUserResponseDTO fromPage(Page<Account> page) {

		List<UserListDTO> list = page.getContent().stream().map(UserListDTO::from).toList();

		return new AdminUserResponseDTO(list, page.getTotalElements());
	}

}
