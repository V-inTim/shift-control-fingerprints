package com.example.shift_control_fingerprints.repository;

import com.example.shift_control_fingerprints.entity.Fingerprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FingerprintRepository extends JpaRepository<Fingerprint, Long> {
    List<Fingerprint> findAllByEnterpriseId (Long enterpriseId);
}
