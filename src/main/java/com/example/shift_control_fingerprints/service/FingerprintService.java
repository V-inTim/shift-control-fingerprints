package com.example.shift_control_fingerprints.service;

import com.example.shift_control_fingerprints.dto.FingerprintDto;
import com.example.shift_control_fingerprints.dto.FingerprintIdDto;
import com.example.shift_control_fingerprints.entity.Fingerprint;
import com.example.shift_control_fingerprints.exception.EnterpriseException;
import com.example.shift_control_fingerprints.repository.FingerprintRepository;
import com.example.shift_control_fingerprints.security.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Service
public class FingerprintService {
    private final FingerprintRepository fingerprintRepository;
    private final AuthUtils authUtils;

    @Autowired
    public FingerprintService(FingerprintRepository fingerprintRepository, AuthUtils authUtils) {
        this.fingerprintRepository = fingerprintRepository;
        this.authUtils = authUtils;
    }

    @PreAuthorize("@enterprisePermission.hasAccessToEnterprise()")
    public FingerprintIdDto create (FingerprintDto dto){
        byte[] template = Base64.getDecoder().decode(dto.getTemplate());
        Fingerprint fingerprint = Fingerprint.builder()
                .template(template)
                .enterpriseId(authUtils.getEnterpriseId())
                .employeeId(dto.getEmployeeId()).build();
        fingerprint = fingerprintRepository.save(fingerprint);
        return new FingerprintIdDto(fingerprint.getId(), fingerprint.getEmployeeId());
    }

    @PreAuthorize("@enterprisePermission.hasAccessToEnterprise()")
    public FingerprintDto getTemplate(Long id){
        Fingerprint fingerprint = fingerprintRepository.findById(id)
                .orElseThrow(()->new EnterpriseException("Записи с таким id нет."));
        String base = Base64.getEncoder().encodeToString(fingerprint.getTemplate());

        return new FingerprintDto(base, fingerprint.getEmployeeId());
    }
    @PreAuthorize("@enterprisePermission.hasAccessToEnterprise()")
    @Transactional(readOnly = true)
    public List<FingerprintIdDto> getAllId(){
        Long enterpriseId = authUtils.getEnterpriseId();
        return fingerprintRepository.findAllByEnterpriseId(enterpriseId)
                .stream().map(fp ->{
                    return new FingerprintIdDto(fp.getId(), fp.getEnterpriseId());
                }).toList();
    }
    @PreAuthorize("@enterprisePermission.hasAccessToEnterpriseByEnterpriseId(#enterpriseId)")
    @Transactional(readOnly = true)
    public List<FingerprintIdDto> getAllIdByEnterpriseId(Long enterpriseId){
        return fingerprintRepository.findAllByEnterpriseId(enterpriseId)
                .stream().map(fp ->{
                    return new FingerprintIdDto(fp.getId(), fp.getEnterpriseId());
                }).toList();
    }
    @PreAuthorize("@enterprisePermission.hasAccessToEnterpriseByFingerprintId(#id)")
    public void delete(Long id){
        fingerprintRepository.deleteById(id);
    }
}
