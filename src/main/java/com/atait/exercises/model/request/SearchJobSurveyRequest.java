package com.atait.exercises.model.request;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchJobSurveyRequest {

    private List<SalaryCondition> salaryConditions;

    private List<String> fields;

    private int page;

    @Max(value = 1000, message = "page_size is limited at 1000")
    private int pageSize;

/*
Timestamp
Employer
Location
Job Title
Years at Employer
Years of Experience
Salary
Signing Bonus
Annual Bonus
Annual Stock Value/Bonus
Gender
Additional Comments
* */
}
