package com.seudominio.auditor.domain.model;

import jakarta.persistence.*;
import com.seudominio.auditor.domain.enums.Severity;
import com.seudominio.auditor.domain.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "error_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorAudit {
    
    @Id
    private String errorId;
    
    @Column(name = "queue_name", nullable = false)
    private String queueName;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;
    
    @Column(nullable = false)
    private Instant timestamp;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;
    
    @PrePersist
    public void prePersist() {
        if (errorId == null) {
            errorId = UUID.randomUUID().toString();
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        if (status == null) {
            status = Status.PENDING_ANALYSIS;
        }
    }
}
