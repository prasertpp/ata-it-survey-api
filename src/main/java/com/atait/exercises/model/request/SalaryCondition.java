package com.atait.exercises.model.request;

import lombok.Data;

import java.math.BigDecimal;

public record SalaryCondition(BigDecimal value, String operator) {
}
