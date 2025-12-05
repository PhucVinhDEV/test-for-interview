package com.example.test_for_interview.service;

import com.example.test_for_interview.dto.AssignRequestDto;
import com.example.test_for_interview.dto.DeTaiResponseDto;
import com.example.test_for_interview.entity.DeTai;
import com.example.test_for_interview.entity.SinhVien;
import com.example.test_for_interview.repository.DeTaiRepository;
import com.example.test_for_interview.repository.SinhVienRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Data
public class DeTaiService {

    private final DeTaiRepository deTaiRepository;
    private final SinhVienRepository sinhVienRepository;

    /**
     * API 1: Lấy danh sách đề tài theo lớp
     * */
    public List<DeTaiResponseDto> getDeTaiByClass(String lop) {
        List<DeTai> all = deTaiRepository.findAll();
        List<DeTaiResponseDto> result = new ArrayList<>();

        for (DeTai dt : all) {
            Optional<SinhVien> svOpt = dt.getSinhViens().stream()
                    .filter(sv -> sv.getLop().equalsIgnoreCase(lop))
                    .findFirst();

            if (svOpt.isPresent()) {
                SinhVien sv = svOpt.get();
                result.add(new DeTaiResponseDto(
                        dt.getMsdt(),
                        dt.getTenDt(),
                        sv.getMssv(),
                        sv.getTenSv(),
                        sv.getLop()
                ));
            } else {
                result.add(new DeTaiResponseDto(
                        dt.getMsdt(),
                        dt.getTenDt(),
                        null,
                        null,
                        null
                ));
            }
        }

        // sắp xếp msdt tăng dần
        result.sort(Comparator.comparing(DeTaiResponseDto::getMsdt));
        return result;
    }

    /**
     * API 2: Gán đề tài cho sinh viên
     */
    @Transactional
    public DeTaiResponseDto assign(String msdt, AssignRequestDto req) {
        String mssv = req.getMssv().trim();

        DeTai dt = deTaiRepository.findById(msdt)
                .orElseThrow(() -> new NoSuchElementException("Đề tài không tồn tại"));

        SinhVien sv = sinhVienRepository.findById(mssv)
                .orElseThrow(() -> new IllegalArgumentException("Sinh viên không tồn tại"));

        // 1: Đề tài đã có sinh viên khác?
        if (!dt.getSinhViens().isEmpty()) {
            // kiểm tra idempotent
            if (dt.getSinhViens().stream().anyMatch(s -> s.getMssv().equals(mssv))) {
                return new DeTaiResponseDto(
                        dt.getMsdt(), dt.getTenDt(),
                        sv.getMssv(), sv.getTenSv(), sv.getLop()
                );
            }

            throw new IllegalStateException("Đề tài đã có sinh viên nhận");
        }

        // 2: Sinh viên đã có đề tài khác?
        if (!sv.getDeTais().isEmpty()) {
            throw new IllegalStateException("Sinh viên đã có đề tài");
        }

        // 3: Gán ManyToMany
        dt.getSinhViens().add(sv);
        sv.getDeTais().add(dt);

        return new DeTaiResponseDto(
                dt.getMsdt(), dt.getTenDt(),
                sv.getMssv(), sv.getTenSv(), sv.getLop()
        );
    }
}
