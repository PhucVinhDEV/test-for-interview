package com.example.test_for_interview.service;

import com.example.test_for_interview.dto.AssignDeTaiRequest;
import com.example.test_for_interview.dto.DeTaiByClassResponse;
import com.example.test_for_interview.entity.DeTai;
import com.example.test_for_interview.entity.SinhVien;
import com.example.test_for_interview.repository.DeTaiRepository;
import com.example.test_for_interview.repository.SinhVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeTaiService {

    private final DeTaiRepository deTaiRepository;
    private final SinhVienRepository sinhVienRepository;

    @Transactional(readOnly = true)
    public List<DeTaiByClassResponse> getDeTaiByClass(String lop) {
        List<DeTaiRepository.DeTaiByClassRow> rows = deTaiRepository.findDeTaiByClass(lop);
        List<DeTaiByClassResponse> result = new ArrayList<>(rows.size());

        for (DeTaiRepository.DeTaiByClassRow row : rows) {
            result.add(DeTaiByClassResponse.builder()
                    .msdt(row.getMsdt())
                    .tendt(row.getTendt())
                    .mssv(row.getMssv())
                    .tensv(row.getTensv())
                    .lop(row.getLop())
                    .build());
        }

        return result;
    }

    @Transactional
    public DeTaiByClassResponse assignDeTai(String msdt, AssignDeTaiRequest request) {
        String mssv = request.getMssv();
        if (mssv == null || mssv.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mssv không được để trống");
        }

        DeTai deTai = deTaiRepository.findById(msdt)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Đề tài không tồn tại"));

        SinhVien sinhVien = sinhVienRepository.findById(mssv)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sinh viên không tồn tại"));

        // Nếu đề tài đã có sinh viên khác nhận
        boolean hasOtherStudent = deTai.getSinhViens()
                .stream()
                .anyMatch(sv -> !sv.getMssv().equals(mssv));
        if (hasOtherStudent) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Đề tài đã có sinh viên nhận");
        }

        // Nếu sinh viên đã có đề tài khác
        if (deTaiRepository.existsOtherDeTaiByMssv(mssv, msdt)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sinh viên đã có đề tài");
        }

        // Gán sinh viên cho đề tài (nếu chưa có)
        boolean alreadyAssigned = deTai.getSinhViens()
                .stream()
                .anyMatch(sv -> sv.getMssv().equals(mssv));
        if (!alreadyAssigned) {
            deTai.getSinhViens().add(sinhVien);
            deTaiRepository.save(deTai);
        }

        return DeTaiByClassResponse.builder()
                .msdt(deTai.getMsdt())
                .tendt(deTai.getTenDt())
                .mssv(sinhVien.getMssv())
                .tensv(sinhVien.getTenSv())
                .lop(sinhVien.getLop())
                .build();
    }
}


