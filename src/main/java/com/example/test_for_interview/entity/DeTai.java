package com.example.test_for_interview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DETAI")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeTai {

    @Id
    @Column(name = "MSDT", length = 6, nullable = false)
    private String msdt;

    @Column(name = "TENDT", length = 30, nullable = false)
    private String tenDt;

    @ManyToMany
    @JoinTable(
            name = "SV_DETAI",
            joinColumns = @JoinColumn(name = "MSDT"),
            inverseJoinColumns = @JoinColumn(name = "MSSV")
    )
    @Builder.Default
    private Set<SinhVien> sinhViens = new HashSet<>();
}


