package com.example.shift_control_fingerprints.permission;

import com.example.shift_control_fingerprints.dto.OwnershipDto;
import com.example.shift_control_fingerprints.exception.EnterpriseException;
import com.example.shift_control_fingerprints.repository.FingerprintRepository;
import com.example.shift_control_fingerprints.security.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.AccessDeniedException;

@Component("fingerprintPermission")
public class FingerprintPermission {
    private final WebClient webClient;
    private final AuthUtils authUtils;
    private final FingerprintRepository fingerprintRepository;
    @Autowired
    public  FingerprintPermission(@Value("${external.enterprise-service.url}") String baseUrl,
                                  AuthUtils authUtils,
                                  FingerprintRepository fingerprintRepository) {
        this.webClient = WebClient.create(baseUrl);
        this.authUtils = authUtils;
        this.fingerprintRepository = fingerprintRepository;
    }

    public boolean hasAccessToEnterpriseByFingerPrintId(Long fingerprintId) {
        Long enterpriseId = fingerprintRepository.findById(fingerprintId)
                .orElseThrow(() -> new EnterpriseException("Записи с таким id нет"))
                .getEnterpriseId();

        return getIsOwner(enterpriseId);
    }

    public boolean hasAccessToEnterpriseByEnterpriseId(Long enterpriseId) {
        return getIsOwner(enterpriseId);
    }

    public boolean hasAccessToEnterprise() {
        Long enterpriseId = authUtils.getEnterpriseId();
        return  getIsOwner(enterpriseId);
    }

    private boolean getIsOwner(Long enterpriseId){
        OwnershipDto dto = webClient.get()
                .uri("/enterprises/{id}/check-ownership", enterpriseId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new AccessDeniedException("Client error: " + body))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Server error: " + body))
                )
                .bodyToMono(OwnershipDto.class)
                .block();

        return dto != null && dto.isOwner();
    }

}
