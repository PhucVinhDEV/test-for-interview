package com.example.test_for_interview.service;

import com.example.test_for_interview.dto.AssignDeTaiRequestDTO;
import com.example.test_for_interview.dto.DeTaiResponseDTO;
import com.example.test_for_interview.entity.DeTai;
import com.example.test_for_interview.entity.SinhVien;
import com.example.test_for_interview.repository.DetaiRepository;
import com.example.test_for_interview.repository.SinhVienRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeTaiService {

    private final DetaiRepository detaiRepository;
    private final SinhVienRepository sinhVienRepository;

    /**
     * API 1: Get all topics with student information filtered by class
     * Returns all topics, with student info if they belong to the specified class
     */
    public List<DeTaiResponseDTO> getDeTaiByClass(String lop) {
        // Get all topics
        List<DeTai> allDeTais = detaiRepository.findAll();
        List<DeTaiResponseDTO> result = new ArrayList<>();

        for (DeTai deTai : allDeTais) {
            // Check if this topic has any student from the specified class
            Optional<SinhVien> sinhVienFromClass = deTai.getSinhViens().stream()
                    .filter(sv -> sv.getLop().equals(lop))
                    .findFirst();

            if (sinhVienFromClass.isPresent()) {
                // Topic has a student from this class
                SinhVien sv = sinhVienFromClass.get();
                result.add(DeTaiResponseDTO.builder()
                        .msdt(deTai.getMsdt())
                        .tendt(deTai.getTenDt())
                        .mssv(sv.getMssv())
                        .tensv(sv.getTenSv())
                        .lop(sv.getLop())
                        .build());
            } else if (deTai.getSinhViens().isEmpty()) {
                // Topic has no student at all - show with null values
                result.add(DeTaiResponseDTO.builder()
                        .msdt(deTai.getMsdt())
                        .tendt(deTai.getTenDt())
                        .mssv(null)
                        .tensv(null)
                        .lop(null)
                        .build());
            }
            // If topic has students but none from this class, don't include it
        }

        // Sort by msdt
        result.sort((a, b) -> a.getMsdt().compareTo(b.getMsdt()));

        return result;
    }

    /**
     * API 2: Assign a student to a topic
     * Business rules:
     * - Topic must exist (404 if not)
     * - Student must exist (400 if not)
     * - Topic must not have another student (409 if it does)
     * - Student must not have another topic (409 if they do)
     * - If student is already assigned to this exact topic, return success (idempotent)
     */
    @Transactional
    public DeTaiResponseDTO assignDeTai(String msdt, AssignDeTaiRequestDTO request) {
        // Validate request
        if (request.getMssv() == null || request.getMssv().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MSSV không được để trống");
        }

        String mssv = request.getMssv().trim();

        // 1. Check if topic exists
        DeTai deTai = detaiRepository.findById(msdt)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Đề tài không tồn tại"));

        // 2. Check if student exists
        SinhVien sinhVien = sinhVienRepository.findById(mssv)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sinh viên không tồn tại"));

        // 3. Check if this exact assignment already exists (idempotent case)
        if (deTai.getSinhViens().contains(sinhVien)) {
            // Already assigned, return success
            return buildDeTaiResponse(deTai, sinhVien);
        }

        // 4. Check if topic already has another student
        if (!deTai.getSinhViens().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Đề tài đã có sinh viên nhận");
        }

        // 5. Check if student already has another topic
        if (!sinhVien.getDeTais().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sinh viên đã có đề tài");
        }

        // 6. Assign student to topic
        deTai.getSinhViens().add(sinhVien);
        sinhVien.getDeTais().add(deTai);

        detaiRepository.save(deTai);
        sinhVienRepository.save(sinhVien);

        return buildDeTaiResponse(deTai, sinhVien);
    }

    private DeTaiResponseDTO buildDeTaiResponse(DeTai deTai, SinhVien sinhVien) {
        return DeTaiResponseDTO.builder()
                .msdt(deTai.getMsdt())
                .tendt(deTai.getTenDt())
                .mssv(sinhVien.getMssv())
                .tensv(sinhVien.getTenSv())
                .lop(sinhVien.getLop())
                .build();
    }
}

