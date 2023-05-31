package com.atait.exercises.model.dto;

import com.atait.exercises.model.request.SalaryCondition;

import java.util.Date;
import java.util.List;

public record JobSurveySpecificationDTO(List<SalaryCondition> salaryConditions, Date startDate, Date endDate, String jobTitle,String gender) {
}
