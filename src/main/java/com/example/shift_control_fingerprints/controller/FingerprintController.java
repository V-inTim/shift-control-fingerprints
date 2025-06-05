package com.example.shift_control_fingerprints.controller;

import com.example.shift_control_fingerprints.dto.FingerprintDto;
import com.example.shift_control_fingerprints.dto.FingerprintIdDto;
import com.example.shift_control_fingerprints.service.FingerprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fingerprints")
public class FingerprintController {
    private final FingerprintService fingerprintService;

    @Autowired
    public FingerprintController(FingerprintService fingerprintService) {
        this.fingerprintService = fingerprintService;
    }

    @PostMapping("/external")
    public ResponseEntity<FingerprintIdDto> create(@RequestBody FingerprintDto dto){
        FingerprintIdDto response = fingerprintService.create(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/external/{id}")
    public ResponseEntity<FingerprintDto> get(@PathVariable Long id){
        FingerprintDto response = fingerprintService.getTemplate(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/external")
    public ResponseEntity<List<FingerprintIdDto>> getAllId(){
        List<FingerprintIdDto> response = fingerprintService.getAllId();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<FingerprintIdDto>> getAllByEnterpriseId(@RequestParam Long enterpriseId){
        List<FingerprintIdDto> response = fingerprintService.getAllIdByEnterpriseId(enterpriseId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        fingerprintService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
