package com.atait.exercises.controller;

import com.atait.exercises.enums.StatusCode;
import com.atait.exercises.exception.ValidationException;
import com.atait.exercises.model.request.SalaryCondition;
import com.atait.exercises.model.request.SearchJobSurveyRequest;
import com.atait.exercises.model.response.CommonResponse;
import com.atait.exercises.model.response.JobResponse;
import com.atait.exercises.model.response.SearchJobSurveyResponse;
import com.atait.exercises.model.response.Status;
import com.atait.exercises.service.JobSurveyService;
import com.atait.exercises.utils.OperatorUtils;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.google.common.base.CaseFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;

import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/api/v1")
@Validated
public class JobDataController {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JobSurveyService jobSurveyService;

    @Autowired
    private Validator validator;

    @GetMapping(value = "/job_data", produces = "application/json")
    public ResponseEntity<CommonResponse<SearchJobSurveyResponse>> jobDataSearching(
            HttpServletRequest servletRequest,
            @RequestParam(value = "page_size", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "fields", required = false)
            @Value("#{'${fields}'.split(',')}") List<String> fields
    ) {

        List<SalaryCondition> salaryConditions = getSalaryConditions(servletRequest);

        SearchJobSurveyRequest request = new SearchJobSurveyRequest(salaryConditions,fields, page, pageSize);
        validateJobDataSearchingValue(request);
        CommonResponse<SearchJobSurveyResponse> response = new CommonResponse<>();
        response.setStatus(new Status(StatusCode.SUCCESS, "success"));
        response.setData(jobSurveyService.searchingJobDataResponse(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<SalaryCondition> getSalaryConditions(HttpServletRequest servletRequest) {
        var allQueryParams  = servletRequest.getParameterMap();
        List<SalaryCondition> salaryConditions = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        Set<String> operatorsSet = new HashSet<>();
        for(var entry : allQueryParams.entrySet()){
            String salaryPattern ="^salary\\[(.*)\\].*";
            Pattern pattern = Pattern.compile(salaryPattern);
            Matcher matcher = pattern.matcher(entry.getKey());
            if(matcher.matches()){
                String operator = matcher.group(1);
                String comparisonOperator = OperatorUtils.convertToComparisonOperator(operator);
                if(!operatorsSet.contains(comparisonOperator)) {
                    try {
                        SalaryCondition salaryCondition = new SalaryCondition(new BigDecimal(entry.getValue()[0]), comparisonOperator);
                        salaryConditions.add(salaryCondition);
                        operatorsSet.add(comparisonOperator);
                    } catch (NumberFormatException e) {
                        errorList.add(entry.getKey() + " is invalid value");
                    }
                }
            }
        }
        if(!CollectionUtils.isEmpty(errorList)){
            throw new ValidationException(errorList);
        }

        return salaryConditions;
    }


    @PostMapping("/job_data")
    public ResponseEntity<CommonResponse<Void>> jobDataInsertion() {

        jobSurveyService.insertJobSurveyData();
        CommonResponse<Void> response = new CommonResponse<>();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    public void validateJobDataSearchingValue(SearchJobSurveyRequest request) {

//        validate spring constraint
        Set<ConstraintViolation<SearchJobSurveyRequest>> violations = validator.validate(request);

        List<String> errorList = new ArrayList<>();
        if (!violations.isEmpty()) {
            List<String> constraintViolationErrors= violations.stream().map(violation -> violation.getMessage()).collect(Collectors.toList());
            errorList.addAll(constraintViolationErrors);
        }

//        validate fields
        if (!CollectionUtils.isEmpty(request.getFields())) {
            var requestFields = Arrays.stream(JobResponse.class.getDeclaredFields())
                    .map(requestField -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, requestField.getName()))
                    .collect(Collectors.toList());

            for (String field : request.getFields()) {
                if (!requestFields.contains(field)) {
                    errorList.add("fields support value only " +
                            String.join(",", requestFields));
                    break;
                }
            }
        }

        //validate salary
    }
}
