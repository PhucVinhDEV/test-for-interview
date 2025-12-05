package com.example.test_for_interview.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignRequestDto {

    @NotBlank
    private String mssv;
}
