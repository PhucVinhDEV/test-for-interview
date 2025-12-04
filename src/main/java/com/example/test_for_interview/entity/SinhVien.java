package com.example.test_for_interview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SINHVIEN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVien {

    @Id
    @Column(name = "MSSV", length = 8, nullable = false)
    private String mssv;

    @Column(name = "TENSV", length = 30, nullable = false)
    private String tenSv;

    @Column(name = "SODT", length = 10, nullable = false)
    private String soDt;

    @Column(name = "LOP", length = 10, nullable = false)
    private String lop;

    @Column(name = "DIACHI", length = 50)
    private String diaChi;

    @ManyToMany(mappedBy = "sinhViens")
    @Builder.Default
    private Set<DeTai> deTais = new HashSet<>();
}


