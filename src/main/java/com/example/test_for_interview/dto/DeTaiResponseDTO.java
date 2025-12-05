package com.example.test_for_interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeTaiResponseDTO {
    private String msdt;
    private String tendt;
    private String mssv;
    private String tensv;
    private String lop;
}


