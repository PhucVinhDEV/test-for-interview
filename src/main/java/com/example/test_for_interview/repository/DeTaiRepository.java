package com.example.test_for_interview.repository;

import com.example.test_for_interview.entity.DeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeTaiRepository extends JpaRepository<DeTai, String> {
    @Query("SELECT COUNT(sv) > 0 FROM DeTai dt JOIN dt.sinhViens sv WHERE dt.msdt = :msdt")
    boolean hasStudent(@Param("msdt") String msdt);
}
