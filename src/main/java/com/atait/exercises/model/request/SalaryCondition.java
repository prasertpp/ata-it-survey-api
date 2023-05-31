package com.atait.exercises.model.request;

import com.atait.exercises.enums.OperatorEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record SalaryCondition(@DecimalMin(value = "0.0", inclusive = false)
                              @Digits(integer=20, fraction=2)
                              BigDecimal value, OperatorEnum operator) {
}
