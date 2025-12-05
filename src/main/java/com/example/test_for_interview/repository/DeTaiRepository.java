package com.example.test_for_interview.repository;

import com.example.test_for_interview.entity.DeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeTaiRepository extends JpaRepository<DeTai, String> {
}
