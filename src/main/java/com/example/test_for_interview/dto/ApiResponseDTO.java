package com.example.test_for_interview.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @Builder.Default
    private boolean success = true;

    private String message;

    private T data;

    private ErrorResponseDTO error;

    public static <T> ApiResponseDTO<T> success(T data) {
        return ApiResponseDTO.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> success(T data, String message) {
        return ApiResponseDTO.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> error(ErrorResponseDTO error) {
        return ApiResponseDTO.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .error(error)
                .build();
    }
}

