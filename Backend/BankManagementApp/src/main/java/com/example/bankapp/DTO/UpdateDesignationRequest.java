package com.example.bankapp.DTO;

import com.example.bankapp.enums.Designation;

import lombok.Data;

@Data
public class UpdateDesignationRequest {
	 private Designation designation;
}
