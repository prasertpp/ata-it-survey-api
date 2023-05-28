package com.atait.exercises.controller;

import com.atait.exercises.enums.StatusCode;
import com.atait.exercises.model.request.SearchJobSurveyRequest;
import com.atait.exercises.model.response.CommonResponse;
import com.atait.exercises.model.response.JobDataResponse;
import com.atait.exercises.model.response.Status;
import com.atait.exercises.service.JobSurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


@Controller
@RequestMapping("/api/v1")
public class JobDataController {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JobSurveyService jobSurveyService;



    @GetMapping(value = "/job_data",produces = "application/json")
    public ResponseEntity<CommonResponse<JobDataResponse>> jobDataSearching(
            SearchJobSurveyRequest request
            ){
        CommonResponse<JobDataResponse> response = new CommonResponse<>();
        response.setStatus(new Status(StatusCode.SUCCESS,"success"));
        response.setData(jobSurveyService.searchingJobDataResponse(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/job_data")
    public ResponseEntity<CommonResponse<Void>> jobDataInsertion(){

        jobSurveyService.insertJobSurveyData();
        CommonResponse<Void> response = new CommonResponse<>();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
