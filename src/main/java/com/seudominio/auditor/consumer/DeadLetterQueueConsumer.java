package com.seudominio.auditor.consumer;

import com.seudominio.auditor.service.AuditService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeadLetterQueueConsumer {
    
    private final AuditService auditService;
    private final String dlqName;
    
    public DeadLetterQueueConsumer(AuditService auditService,
                                   @Value("${sqs.dlq.name}") String dlqName) {
        this.auditService = auditService;
        this.dlqName = dlqName;
    }
    
    @SqsListener("${sqs.dlq.name}")
    public void receiveMessage(String message) {
        log.info("Ì≥® Mensagem recebida da DLQ: {}", dlqName);
        log.debug("Payload: {}", message);
        
        try {
            auditService.saveFailedMessage(dlqName, message);
            log.info("‚úÖ Mensagem processada e removida da DLQ com sucesso!");
        } catch (Exception e) {
            log.error("‚ùå Falha no processamento - mensagem permanecer√° na DLQ");
            throw new RuntimeException(e);
        }
    }
}
