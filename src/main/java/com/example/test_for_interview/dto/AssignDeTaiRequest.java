package com.example.test_for_interview.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignDeTaiRequest {
    @NotBlank(message = "MSSV không được để trống")
    private String mssv;
}
