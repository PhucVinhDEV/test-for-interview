package com.example.test_for_interview.repository;

import com.example.test_for_interview.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinhVienRepository extends JpaRepository<SinhVien, String> {

    boolean existsByLop(String lop);
}


