package com.example.test_for_interview.repository;

import com.example.test_for_interview.entity.DeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetaiRepository extends JpaRepository<DeTai, String> {

    @Query("SELECT dt FROM DeTai dt LEFT JOIN dt.sinhViens sv WHERE sv.lop = :lop OR sv.lop IS NULL ORDER BY dt.msdt")
    List<DeTai> findAllWithSinhViensByLop(@Param("lop") String lop);
}
