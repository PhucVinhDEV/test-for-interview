package com.example.test_for_interview.controller;

import com.example.test_for_interview.dto.AssignDeTaiRequest;
import com.example.test_for_interview.dto.DeTaiResponse;
import com.example.test_for_interview.service.DeTaiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detai")
@RequiredArgsConstructor
public class DeTaiController {
    private final DeTaiService deTaiService;

    @GetMapping("/by-class")
    @Operation(summary = "Lấy danh sách đề tài theo lớp",
            description = "Trả về danh sách tất cả đề tài kèm thông tin sinh viên của lớp đó (nếu có)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "400", description = "Tham số không hợp lệ")
    })
    public ResponseEntity<List<DeTaiResponse>> getDeTaiByClass(
            @RequestParam(required = true) String lop) {

        List<DeTaiResponse> responses = deTaiService.getDeTaiByClass(lop);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{msdt}/assign")
    @Operation(summary = "Sinh viên nhận đề tài",
            description = "Gán một sinh viên vào đề tài")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gán đề tài thành công"),
            @ApiResponse(responseCode = "400", description = "Sinh viên không tồn tại"),
            @ApiResponse(responseCode = "404", description = "Đề tài không tồn tại"),
            @ApiResponse(responseCode = "409", description = "Đề tài đã có sinh viên hoặc sinh viên đã có đề tài")
    })
    public ResponseEntity<DeTaiResponse> assignDeTai(
            @PathVariable String msdt,
            @Valid @RequestBody AssignDeTaiRequest request) {

        DeTaiResponse response = deTaiService.assignDeTai(msdt, request.getMssv());
        return ResponseEntity.ok(response);
    }
}
