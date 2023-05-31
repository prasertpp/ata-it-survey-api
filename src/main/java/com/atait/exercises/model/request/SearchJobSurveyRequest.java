package com.atait.exercises.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchJobSurveyRequest {

//    search conditions
    @Valid
    private List<SalaryCondition> salaryConditions;

    @Pattern(regexp = "^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$", message = "start_date is invalid format, format is dd/MM/yyyy")
    private String startDate;

    @Pattern(regexp = "^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$", message = "end_date is invalid format, format is dd/MM/yyyy")
    private String endDate;

    private String jobTitle;

    private String gender;

//    display conditions
    private List<String> fields;

    private int page;

    @Max(value = 1000, message = "page_size is limited at 1000")
    private int pageSize;

    @Valid
    private List<SortParamRequest> sortParamRequests;




}
