package com.trycore.evm.service;

import com.trycore.evm.dto.ActivityEvmDto;
import com.trycore.evm.model.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EvmCalculatorServiceTest {

    private EvmCalculatorService evmCalculatorService;

    @BeforeEach
    void setUp() {
        evmCalculatorService = new EvmCalculatorService();
    }

    @Test
    void calculate_shouldReturnCorrectEvmIndicators() {
        Activity activity = buildActivity("100000", "50", "40", "45000");

        ActivityEvmDto result = evmCalculatorService.calculate(activity);

        assertEquals(new BigDecimal("50000.0000"), result.getPv());
        assertEquals(new BigDecimal("40000.0000"), result.getEv());
        assertEquals(new BigDecimal("-5000.0000"), result.getCv());
        assertEquals(new BigDecimal("-10000.0000"), result.getSv());
        assertEquals(new BigDecimal("0.8889"), result.getCpi());
        assertEquals(new BigDecimal("0.8000"), result.getSpi());
    }

    @Test
    void calculate_shouldReturnZeroCpi_whenActualCostIsZero() {
        Activity activity = buildActivity("100000", "50", "40", "0");

        ActivityEvmDto result = evmCalculatorService.calculate(activity);

        assertEquals(BigDecimal.ZERO, result.getCpi());
        assertEquals(BigDecimal.ZERO, result.getEac());
    }

    @Test
    void calculate_shouldReturnZeroSpi_whenPlannedProgressIsZero() {
        Activity activity = buildActivity("100000", "0", "40", "45000");

        ActivityEvmDto result = evmCalculatorService.calculate(activity);

        assertEquals(BigDecimal.ZERO, result.getSpi());
    }

    @Test
    void calculate_shouldReturnZeroIndicators_whenActualProgressIsZero() {
        Activity activity = buildActivity("100000", "50", "0", "0");

        ActivityEvmDto result = evmCalculatorService.calculate(activity);

        assertEquals(new BigDecimal("0.0000"), result.getEv());
        assertEquals(BigDecimal.ZERO, result.getCpi());
    }

    @Test
    void interpretCpi_shouldReturnUnderBudget_whenCpiGreaterThanOne() {
        String result = evmCalculatorService.interpretCpi(new BigDecimal("1.2"));
        assertEquals("Bajo presupuesto - eficiente en costos", result);
    }

    @Test
    void interpretCpi_shouldReturnOverBudget_whenCpiLessThanOne() {
        String result = evmCalculatorService.interpretCpi(new BigDecimal("0.8"));
        assertEquals("Sobre presupuesto - gastando más de lo avanzado", result);
    }

    @Test
    void interpretSpi_shouldReturnAhead_whenSpiGreaterThanOne() {
        String result = evmCalculatorService.interpretSpi(new BigDecimal("1.1"));
        assertEquals("Adelantado en cronograma", result);
    }

    @Test
    void interpretSpi_shouldReturnBehind_whenSpiLessThanOne() {
        String result = evmCalculatorService.interpretSpi(new BigDecimal("0.9"));
        assertEquals("Atrasado en cronograma", result);
    }

    private Activity buildActivity(String bac, String planned, String actual, String ac) {
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setName("Test Activity");
        activity.setBac(new BigDecimal(bac));
        activity.setPlannedProgress(new BigDecimal(planned));
        activity.setActualProgress(new BigDecimal(actual));
        activity.setActualCost(new BigDecimal(ac));
        return activity;
    }
}