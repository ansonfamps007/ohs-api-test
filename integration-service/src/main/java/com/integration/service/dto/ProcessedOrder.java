package com.integration.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessedOrder {

    private String userPid;

    private String orderPid;

    private String supplierPid;
}
