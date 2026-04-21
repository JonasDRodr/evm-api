package com.trycore.evm.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProjectEvmDto {

    private Long id;
    private String name;
    private String description;

    private List<ActivityEvmDto> activities;

    // Indicadores consolidados
    private BigDecimal totalBac;
    private BigDecimal totalPv;
    private BigDecimal totalEv;
    private BigDecimal totalAc;
    private BigDecimal cv;
    private BigDecimal sv;
    private BigDecimal cpi;
    private BigDecimal spi;
    private BigDecimal eac;
    private BigDecimal vac;

    private String cpiInterpretation;
    private String spiInterpretation;
}