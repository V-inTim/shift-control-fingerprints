package com.example.shift_control_fingerprints.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FingerprintIdDto {
    private Long id;
    private Long employeeId;
}
