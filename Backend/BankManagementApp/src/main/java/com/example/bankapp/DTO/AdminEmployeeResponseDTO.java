package com.example.bankapp.DTO;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.bankapp.entity.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEmployeeResponseDTO {

    private List<EmployeeListDTO> data;
    private long totalRecords;

    public static AdminEmployeeResponseDTO fromPage(Page<Employee> page) {

        List<EmployeeListDTO> list = page.getContent()
                .stream()
                .map(EmployeeListDTO::from)
                .toList();

        return new AdminEmployeeResponseDTO(
                list,
                page.getTotalElements()
        );
    }

}
