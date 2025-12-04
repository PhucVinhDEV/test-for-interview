package com.example.test_for_interview.service;

import com.example.test_for_interview.dto.DeTaiResponse;
import com.example.test_for_interview.entity.DeTai;
import com.example.test_for_interview.entity.SinhVien;
import com.example.test_for_interview.exception.BadRequestException;
import com.example.test_for_interview.exception.ConflictException;
import com.example.test_for_interview.exception.NotFoundException;
import com.example.test_for_interview.repository.DeTaiRepository;
import com.example.test_for_interview.repository.SinhVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeTaiService {
    private final DeTaiRepository deTaiRepository;
    private final SinhVienRepository sinhVienRepository;

    public List<DeTaiResponse> getDeTaiByClass(String lop) {
        // Lấy tất cả đề tài
        List<DeTai> allDeTai = deTaiRepository.findAll();

        // Lấy danh sách sinh viên của lớp
        List<SinhVien> sinhViensInClass = sinhVienRepository.findByLop(lop);

        List<DeTaiResponse> responses = new ArrayList<>();

        for (DeTai deTai : allDeTai) {
            // Tìm sinh viên của lớp này đang làm đề tài
            SinhVien assignedStudent = deTai.getSinhViens().stream()
                    .filter(sv -> sv.getLop().equals(lop))
                    .findFirst()
                    .orElse(null);

            DeTaiResponse response = new DeTaiResponse();
            response.setMsdt(deTai.getMsdt());
            response.setTendt(deTai.getTenDt());

            if (assignedStudent != null) {
                response.setMssv(assignedStudent.getMssv());
                response.setTensv(assignedStudent.getTenSv());
                response.setLop(assignedStudent.getLop());
            } else {
                response.setMssv(null);
                response.setTensv(null);
                response.setLop(null);
            }

            responses.add(response);
        }
        return responses.stream()
                .sorted((a, b) -> a.getMsdt().compareTo(b.getMsdt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public DeTaiResponse assignDeTai(String msdt, String mssv) {
        // 1. Kiểm tra đề tài có tồn tại không
        DeTai deTai = deTaiRepository.findById(msdt)
                .orElseThrow(() -> new NotFoundException("Đề tài không tồn tại"));

        // 2. Kiểm tra sinh viên có tồn tại không
        SinhVien sinhVien = sinhVienRepository.findById(mssv)
                .orElseThrow(() -> new BadRequestException("Sinh viên không tồn tại"));

        // 3. Kiểm tra đề tài đã có sinh viên khác chưa
        if (!deTai.getSinhViens().isEmpty()) {
            throw new ConflictException("Đề tài đã có sinh viên nhận");
        }

        // 4. Kiểm tra sinh viên đã có đề tài khác chưa
        if (sinhVienRepository.hasDeTai(mssv)) {
            throw new ConflictException("Sinh viên đã có đề tài");
        }

        // 5. Gán đề tài cho sinh viên
        sinhVien.getDeTais().add(deTai);
        deTai.getSinhViens().add(sinhVien);

        sinhVienRepository.save(sinhVien);

        // 6. Trả về thông tin đề tài sau khi gán
        DeTaiResponse response = new DeTaiResponse();
        response.setMsdt(deTai.getMsdt());
        response.setTendt(deTai.getTenDt());
        response.setMssv(sinhVien.getMssv());
        response.setTensv(sinhVien.getTenSv());
        response.setLop(sinhVien.getLop());

        return response;
    }
}
