package com.example.test_for_interview.controller;

import com.example.test_for_interview.dto.AssignRequestDto;
import com.example.test_for_interview.dto.DeTaiResponseDto;
import com.example.test_for_interview.service.DeTaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/detai")
@RequiredArgsConstructor
public class DeTaiController {

    private final DeTaiService service;

    @GetMapping("/by-class")
    public ResponseEntity<List<DeTaiResponseDto>> byClass(@RequestParam String lop) {
        return ResponseEntity.ok(service.getDeTaiByClass(lop));
    }

    @PostMapping("/{msdt}/assign")
    public ResponseEntity<?> assign(@PathVariable String msdt, @RequestBody AssignRequestDto req) {
        try {
            DeTaiResponseDto res = service.assign(msdt, req);
            return ResponseEntity.ok(res);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}
