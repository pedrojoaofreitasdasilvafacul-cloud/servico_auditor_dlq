package com.seudominio.auditor.domain.model;

import java.util.List;
import com.seudominio.auditor.dto.OrderItemDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFailedEvent {
    private String zipCode;
    private Long customerId;
    private List<OrderItemDto> orderItems;
    private String origin;
    private String occurredAt;
}
