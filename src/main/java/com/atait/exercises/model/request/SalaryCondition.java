package com.atait.exercises.model.request;

import com.atait.exercises.enums.OperatorEnum;

import java.math.BigDecimal;

public record SalaryCondition(BigDecimal value, OperatorEnum operator) {
}
