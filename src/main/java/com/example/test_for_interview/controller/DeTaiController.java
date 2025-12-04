package com.example.test_for_interview.controller;

import com.example.test_for_interview.dto.AssignDeTaiRequest;
import com.example.test_for_interview.dto.DeTaiByClassResponse;
import com.example.test_for_interview.service.DeTaiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detai")
@RequiredArgsConstructor
@Tag(name = "Đề tài", description = "API quản lý đề tài và sinh viên nhận đề tài")
public class DeTaiController {

    private final DeTaiService deTaiService;

    @GetMapping("/by-class")
    @Operation(summary = "Lấy danh sách đề tài theo lớp")
    public ResponseEntity<List<DeTaiByClassResponse>> getByClass(@RequestParam("lop") String lop) {
        return ResponseEntity.ok(deTaiService.getDeTaiByClass(lop));
    }

    @PostMapping("/{msdt}/assign")
    @Operation(summary = "Sinh viên nhận đề tài")
    public ResponseEntity<DeTaiByClassResponse> assignDeTai(
            @PathVariable("msdt") String msdt,
            @RequestBody AssignDeTaiRequest request
    ) {
        return ResponseEntity.ok(deTaiService.assignDeTai(msdt, request));
    }
}


