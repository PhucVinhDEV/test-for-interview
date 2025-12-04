package com.example.test_for_interview.repository;

import com.example.test_for_interview.entity.DeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeTaiRepository extends JpaRepository<DeTai, String> {

    @Query(value = """
            SELECT dt.MSDT   AS msdt,
                   dt.TENDT  AS tendt,
                   sv.MSSV   AS mssv,
                   sv.TENSV  AS tensv,
                   sv.LOP    AS lop
            FROM DETAI dt
                     LEFT JOIN SV_DETAI sd ON dt.MSDT = sd.MSDT
                     LEFT JOIN SINHVIEN sv ON sd.MSSV = sv.MSSV
                        AND (:lop IS NULL OR TRIM(sv.LOP) = TRIM(:lop))
            ORDER BY dt.MSDT
            """, nativeQuery = true)
    List<DeTaiByClassRow> findDeTaiByClass(@Param("lop") String lop);

    @Query(value = """
            SELECT COUNT(*) > 0
            FROM SV_DETAI sd
            WHERE sd.MSSV = :mssv
              AND sd.MSDT <> :msdt
            """, nativeQuery = true)
    boolean existsOtherDeTaiByMssv(@Param("mssv") String mssv, @Param("msdt") String msdt);

    interface DeTaiByClassRow {
        String getMsdt();
        String getTendt();
        String getMssv();
        String getTensv();
        String getLop();
    }
}


