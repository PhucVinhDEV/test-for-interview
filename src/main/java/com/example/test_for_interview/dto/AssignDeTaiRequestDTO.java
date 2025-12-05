package com.example.test_for_interview.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignDeTaiRequestDTO {
    @NotBlank(message = "MSSV không được để trống")
    private String mssv;
}

