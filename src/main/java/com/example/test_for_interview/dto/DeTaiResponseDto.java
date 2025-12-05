package com.example.test_for_interview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeTaiResponseDto {
    private String msdt;
    private String tendt;
    private String mssv;
    private String tensv;
    private String lop;
}
