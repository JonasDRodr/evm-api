package com.trycore.evm.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ActivityEvmDto {

    private Long id;
    private String name;
    private BigDecimal bac;
    private BigDecimal plannedProgress;
    private BigDecimal actualProgress;
    private BigDecimal actualCost;

    // Indicadores EVM
    private BigDecimal pv;
    private BigDecimal ev;
    private BigDecimal cv;
    private BigDecimal sv;
    private BigDecimal cpi;
    private BigDecimal spi;
    private BigDecimal eac;
    private BigDecimal vac;
}