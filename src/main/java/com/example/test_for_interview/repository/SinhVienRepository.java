package com.example.test_for_interview.repository;

import com.example.test_for_interview.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SinhVienRepository extends JpaRepository<SinhVien, String> {
    List<SinhVien> findByLop(String lop);

    @Query("SELECT COUNT(sv) > 0 FROM SinhVien sv JOIN sv.deTais dt WHERE sv.mssv = :mssv")
    boolean hasDeTai(@Param("mssv") String mssv);
}
