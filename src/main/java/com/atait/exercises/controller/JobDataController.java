package com.atait.exercises.controller;

import com.atait.exercises.enums.OperatorEnum;
import com.atait.exercises.enums.StatusCode;
import com.atait.exercises.exception.ValidationException;
import com.atait.exercises.model.request.SalaryCondition;
import com.atait.exercises.model.request.SearchJobSurveyRequest;
import com.atait.exercises.model.request.SortParamRequest;
import com.atait.exercises.model.response.CommonResponse;
import com.atait.exercises.model.response.JobResponse;
import com.atait.exercises.model.response.SearchJobSurveyResponse;
import com.atait.exercises.model.response.Status;
import com.atait.exercises.service.JobSurveyService;
import com.atait.exercises.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    final Logger logger = LoggerFactory.getLogger(JobDataController.class);

    private static final String SALARY_QUERY_PARAM_PATTERN = "^salary\\[(.*)\\].*";

    @GetMapping(value = "/job_data", produces = "application/json")
    public ResponseEntity<CommonResponse<SearchJobSurveyResponse>> jobDataSearching(
            HttpServletRequest servletRequest,
            @RequestParam(value = "start_date",required = false) String startDate,
            @RequestParam(value = "end_date",required = false) String endDate,
            @RequestParam(value = "job_title",required = false) String jobTitle,
            @RequestParam(value = "gender",required = false) String gender,
            @RequestParam(value = "page_size", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "field", required = false)
            @Value("#{'${field}'.split(',')}") List<String> fields,
            @RequestParam(value = "sort", required = false) List<String> sorting,
            @RequestParam(value = "sort_type", required = false) List<String> sortTypes
    ) {
        logger.info("[jobDataSearching] START");
        List<SalaryCondition> salaryConditions = getSalaryConditions(servletRequest);
        List<SortParamRequest> sortParamRequests = getSortParamRequests(sorting, sortTypes);

        SearchJobSurveyRequest request = new SearchJobSurveyRequest(salaryConditions,startDate,endDate,jobTitle,gender, fields, page, pageSize, sortParamRequests);
        logger.info("[jobDataSearching] REQUEST : {}",request);
        validateJobDataSearchingValue(request);
        CommonResponse<SearchJobSurveyResponse> response = new CommonResponse<>();
        response.setStatus(new Status(StatusCode.SUCCESS, "success"));
        response.setData(jobSurveyService.searchingJobDataResponse(request));

        logger.info("[jobDataSearching] DONE");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<SortParamRequest> getSortParamRequests(List<String> sortFields, List<String> sortTypes) {
        boolean isSortFieldEmpty = CollectionUtils.isEmpty(sortFields);
        boolean isSortTypeEmpty = CollectionUtils.isEmpty(sortTypes);
        //NOTE: when no sorting set SortParamRequest to null
        if( isSortFieldEmpty && isSortTypeEmpty){
            return null;
        }
        List<SortParamRequest> sortParamRequests = new ArrayList<>();
        if (isSortFieldEmpty || isSortTypeEmpty || sortFields.size() != sortTypes.size()) {
            throw new ValidationException("sort and sort_type are invalid format, the number of sort field and sort_type must match");
        }
        for (int i = 0; i < sortFields.size(); i++) {
            Optional<Sort.Direction> sortType = Sort.Direction.fromOptionalString(sortTypes.get(i));
            sortParamRequests.add(new SortParamRequest(sortFields.get(i), sortType));
        }

        return sortParamRequests;
    }

    private List<SalaryCondition> getSalaryConditions(HttpServletRequest servletRequest) {
        Map<String,String[]> allQueryParams = servletRequest.getParameterMap();
        List<SalaryCondition> salaryConditions = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        Set<String> operatorsSet = new HashSet<>();
        for (var entry : allQueryParams.entrySet()) {
            Pattern pattern = Pattern.compile(SALARY_QUERY_PARAM_PATTERN);
            Matcher matcher = pattern.matcher(entry.getKey());
            if (matcher.matches()) {
                String operator = matcher.group(1);
                if (!operatorsSet.contains(operator)) {
                    try {
                        Optional<OperatorEnum> operatorEnum = OperatorEnum.of(operator);
                        if(operatorEnum.isEmpty()){
                            errorList.add("["+operator+ "] is invalid value, operators supports only [gt],[lt],[eq],[gte],[lte]");
                            break;
                        }
                        SalaryCondition salaryCondition = new SalaryCondition(new BigDecimal(entry.getValue()[0]), operatorEnum.get());
                        salaryConditions.add(salaryCondition);
                        operatorsSet.add(operator);
                    } catch (NumberFormatException e) {
                        errorList.add(entry.getKey() + " is invalid value");
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(errorList)) {
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
            List<String> constraintViolationErrors = violations.stream().map(ConstraintViolation::getMessage).toList();
            errorList.addAll(constraintViolationErrors);
            throw new ValidationException(errorList);
        }

            validateFieldAndSorting(request, errorList);
//         validate date
            if(StringUtils.isNotBlank(request.getStartDate()) && StringUtils.isNotBlank(request.getEndDate()) && DateUtils.isBefore(DateUtils.DDMMYYYY_SLASH_PATTERN,request.getEndDate(),request.getStartDate())){
                errorList.add("start_date is after end_date");
            }
//         validate jobTitle


            if (!CollectionUtils.isEmpty(errorList)) {
                throw new ValidationException(errorList);
            }

    }

    private void validateFieldAndSorting(SearchJobSurveyRequest request, List<String> errorList) {
        if (!CollectionUtils.isEmpty(request.getFields()) || !CollectionUtils.isEmpty(request.getSortParamRequests())) {
            var requestFields = Arrays.stream(JobResponse.class.getDeclaredFields())
                    .map(requestField -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, requestField.getName()))
                    .toList();

            if (!CollectionUtils.isEmpty(request.getFields())) {
                for (String field : request.getFields()) {
                    if (!requestFields.contains(field)) {
                        errorList.add("field support value only " +
                                String.join(",", requestFields));
                        break;
                    }
                }
            }

            if (!CollectionUtils.isEmpty(request.getSortParamRequests())) {
                for (SortParamRequest sortParamRequest : request.getSortParamRequests()) {
                    if (!sortParamRequest.getSortType().isPresent()) {
                        errorList.add("sort_type support value only 'asc' or 'desc'");
                        break;
                    }

                    if (!requestFields.contains(sortParamRequest.getSortField())) {
                        errorList.add("sort support value only " +
                                String.join(",", requestFields));
                       break;
                    }
                }
            }
        }
    }
}
