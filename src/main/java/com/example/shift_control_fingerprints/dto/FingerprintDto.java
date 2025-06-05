package com.example.shift_control_fingerprints.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FingerprintDto {
    @NotNull
    private String template;
    @NotNull
    private Long employeeId;
}
