package com.seudominio.auditor.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderFailedEventDto {
    private String zipCode;
    private Long customerId;
    private List<OrderItemDto> orderItems;
    private String origin;
    private String occurredAt;
}
