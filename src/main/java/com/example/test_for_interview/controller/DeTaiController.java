package com.example.test_for_interview.controller;

import com.example.test_for_interview.dto.ApiResponseDTO;
import com.example.test_for_interview.dto.AssignDeTaiRequestDTO;
import com.example.test_for_interview.dto.DeTaiResponseDTO;
import com.example.test_for_interview.service.DeTaiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detai")
@RequiredArgsConstructor
@Tag(name = "Đề Tài API", description = "APIs quản lý đề tài và phân công sinh viên")
public class DeTaiController {

    private final DeTaiService deTaiService;

    /**
     * API 1: Get list of topics by class
     * GET /api/detai/by-class?lop=SE103.U32
     */
    @GetMapping("/by-class")
    @Operation(
        summary = "Lấy danh sách đề tài theo lớp",
        description = "Trả về tất cả đề tài trong hệ thống, kèm theo thông tin sinh viên của lớp đó (nếu có). " +
                     "Đề tài chưa có sinh viên hoặc có sinh viên lớp khác cũng hiển thị với thông tin null."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lấy danh sách thành công",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        )
    })
    public ResponseEntity<ApiResponseDTO<List<DeTaiResponseDTO>>> getDeTaiByClass(
            @Parameter(description = "Tên lớp cần tìm kiếm", example = "SE103.U32", required = true)
            @RequestParam String lop
    ) {
        List<DeTaiResponseDTO> result = deTaiService.getDeTaiByClass(lop);
        return ResponseEntity.ok(ApiResponseDTO.success(result, "Lấy danh sách đề tài thành công"));
    }

    /**
     * API 2: Assign a student to a topic
     * POST /api/detai/{msdt}/assign
     * Body: {"mssv": "13520001"}
     */
    @PostMapping("/{msdt}/assign")
    @Operation(
        summary = "Sinh viên nhận đề tài",
        description = "Gán một sinh viên vào đề tài. Đề tài chỉ có thể có một sinh viên, " +
                     "và sinh viên chỉ có thể nhận một đề tài."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Gán sinh viên thành công",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Sinh viên không tồn tại hoặc dữ liệu không hợp lệ"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Đề tài không tồn tại"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Đề tài đã có sinh viên khác hoặc sinh viên đã có đề tài"
        )
    })
    public ResponseEntity<ApiResponseDTO<DeTaiResponseDTO>> assignDeTai(
            @Parameter(description = "Mã số đề tài", example = "97007", required = true)
            @PathVariable String msdt,
            @Valid @RequestBody AssignDeTaiRequestDTO request
    ) {
        DeTaiResponseDTO result = deTaiService.assignDeTai(msdt, request);
        return ResponseEntity.ok(ApiResponseDTO.success(result, "Gán sinh viên vào đề tài thành công"));
    }
}

