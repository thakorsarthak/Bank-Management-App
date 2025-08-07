package com.example.bankapp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetPinWithOtpDTO {


	    private String email;
	    private String contact;

	    @NotBlank(message = "New PIN is required")
	    @Size(min = 4, max = 8, message = "PIN must be 4 to 8 digits")
	    private String newPin;

	    @NotBlank(message = "Confirm PIN is required")
	    private String confirmPin;
	}


