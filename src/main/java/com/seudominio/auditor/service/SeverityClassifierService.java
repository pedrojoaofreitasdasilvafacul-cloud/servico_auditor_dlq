package com.seudominio.auditor.service;

import com.seudominio.auditor.domain.enums.Severity;
import com.seudominio.auditor.dto.OrderItemDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeverityClassifierService {
    
    public Severity classifyByTotalAmount(List<OrderItemDto> items) {
        if (items == null || items.isEmpty()) {
            return Severity.LOW;
        }
        
        int total = items.stream()
                .mapToInt(item -> item.getAmount() != null ? item.getAmount() : 0)
                .sum();
        
        if (total > 100) {
            return Severity.HIGH;
        } else if (total >= 50) {
            return Severity.MEDIUM;
        } else {
            return Severity.LOW;
        }
    }
}
