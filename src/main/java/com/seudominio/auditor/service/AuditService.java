package com.seudominio.auditor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seudominio.auditor.domain.model.ErrorAudit;
import com.seudominio.auditor.domain.model.OrderFailedEvent;
import com.seudominio.auditor.domain.repository.ErrorAuditRepository;
import com.seudominio.auditor.domain.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Slf4j
@Service
public class AuditService {
    
    private final ErrorAuditRepository repository;
    private final SeverityClassifierService classifier;
    private final ObjectMapper objectMapper;
    
    public AuditService(ErrorAuditRepository repository, 
                       SeverityClassifierService classifier) {
        this.repository = repository;
        this.classifier = classifier;
        this.objectMapper = new ObjectMapper();
    }
    
    @Transactional
    public void saveFailedMessage(String queueName, String rawPayload) {
        try {
            log.info("Ì≥ù Processando mensagem da DLQ");
            
            OrderFailedEvent event = objectMapper.readValue(rawPayload, OrderFailedEvent.class);
            var severity = classifier.classifyByTotalAmount(event.getOrderItems());
            int totalItems = event.getOrderItems().stream()
                    .mapToInt(item -> item.getAmount() != null ? item.getAmount() : 0).sum();
            
            log.info("Ì≥ä Total itens: {} | Severidade: {}", totalItems, severity);
            
            ErrorAudit audit = new ErrorAudit();
            audit.setQueueName(queueName);
            audit.setPayload(rawPayload);
            audit.setTimestamp(Instant.now());
            audit.setStatus(Status.PENDING_ANALYSIS);
            audit.setSeverity(severity);
            
            repository.save(audit);
            log.info("Ì≤æ Mensagem salva - ID: {} | Severidade: {}", audit.getErrorId(), severity);
            
        } catch (Exception e) {
            log.error("‚ùå Erro ao processar mensagem: {}", e.getMessage());
            throw new RuntimeException("Falha ao salvar mensagem da DLQ", e);
        }
    }
}
