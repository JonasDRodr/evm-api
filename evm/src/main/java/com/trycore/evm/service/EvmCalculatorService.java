package com.trycore.evm.service;

import com.trycore.evm.dto.ActivityEvmDto;
import com.trycore.evm.model.Activity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class EvmCalculatorService {

    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final MathContext MATH_CONTEXT = new MathContext(10, ROUNDING_MODE);

    public ActivityEvmDto calculate(Activity activity) {
        ActivityEvmDto dto = new ActivityEvmDto();

        dto.setId(activity.getId());
        dto.setName(activity.getName());
        dto.setBac(activity.getBac());
        dto.setPlannedProgress(activity.getPlannedProgress());
        dto.setActualProgress(activity.getActualProgress());
        dto.setActualCost(activity.getActualCost());

        BigDecimal bac = activity.getBac();
        BigDecimal plannedProgress = activity.getPlannedProgress().divide(BigDecimal.valueOf(100), SCALE, ROUNDING_MODE);
        BigDecimal actualProgress = activity.getActualProgress().divide(BigDecimal.valueOf(100), SCALE, ROUNDING_MODE);
        BigDecimal ac = activity.getActualCost();

        BigDecimal pv = bac.multiply(plannedProgress).setScale(SCALE, ROUNDING_MODE);
        BigDecimal ev = bac.multiply(actualProgress).setScale(SCALE, ROUNDING_MODE);
        BigDecimal cv = ev.subtract(ac).setScale(SCALE, ROUNDING_MODE);
        BigDecimal sv = ev.subtract(pv).setScale(SCALE, ROUNDING_MODE);

        BigDecimal cpi = ac.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : ev.divide(ac, SCALE, ROUNDING_MODE);

        BigDecimal spi = pv.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : ev.divide(pv, SCALE, ROUNDING_MODE);

        BigDecimal eac = cpi.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : bac.divide(cpi, SCALE, ROUNDING_MODE);

        BigDecimal vac = bac.subtract(eac).setScale(SCALE, ROUNDING_MODE);

        dto.setPv(pv);
        dto.setEv(ev);
        dto.setCv(cv);
        dto.setSv(sv);
        dto.setCpi(cpi);
        dto.setSpi(spi);
        dto.setEac(eac);
        dto.setVac(vac);

        return dto;
    }

    public String interpretCpi(BigDecimal cpi) {
        if (cpi.compareTo(BigDecimal.ONE) > 0) {
            return "Bajo presupuesto - eficiente en costos";
        } else if (cpi.compareTo(BigDecimal.ONE) == 0) {
            return "En presupuesto";
        } else {
            return "Sobre presupuesto - gastando más de lo avanzado";
        }
    }

    public String interpretSpi(BigDecimal spi) {
        if (spi.compareTo(BigDecimal.ONE) > 0) {
            return "Adelantado en cronograma";
        } else if (spi.compareTo(BigDecimal.ONE) == 0) {
            return "En cronograma";
        } else {
            return "Atrasado en cronograma";
        }
    }
}