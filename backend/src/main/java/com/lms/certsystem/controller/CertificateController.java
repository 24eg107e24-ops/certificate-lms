package com.lms.certsystem.controller;

import com.lms.certsystem.entity.Certificate;
import com.lms.certsystem.service.CertificateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Certificate>> getStudentCertificates(@PathVariable Long studentId) {
        return ResponseEntity.ok(certificateService.getStudentCertificates(studentId));
    }

    @GetMapping("/verify/{certificateId}")
    public ResponseEntity<?> verifyCertificate(@PathVariable String certificateId) {
        Optional<Certificate> certOpt = certificateService.verifyCertificate(certificateId);
        if (certOpt.isPresent()) {
            return ResponseEntity.ok(certOpt.get());
        } else {
            return ResponseEntity.status(404).body("Certificate not found or invalid!");
        }
    }

    @GetMapping("/{certificateId}/pdf")
    public ResponseEntity<byte[]> downloadCertificatePdf(@PathVariable String certificateId) {
        try {
            byte[] pdfBytes = certificateService.getCertificatePdf(certificateId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Certificate-" + certificateId + ".pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Certificate>> getAllCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }
}
