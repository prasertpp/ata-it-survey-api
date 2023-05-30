package com.atait.exercises.service;

import com.atait.exercises.exception.SourceDataMapperException;
import com.atait.exercises.mapper.JobSurveyDTOMapper;
import com.atait.exercises.model.entity.JobSurveyEntity;
import com.atait.exercises.model.request.SearchJobSurveyRequest;
import com.atait.exercises.model.response.SearchJobSurveyResponse;
import com.atait.exercises.model.source.SalarySurvey;
import com.atait.exercises.model.dto.JobSurveySpecificationDTO;
import com.atait.exercises.repository.JobSurveyRepository;
import com.atait.exercises.repository.JobSurveySpecification;
import com.atait.exercises.utils.DateUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.atait.exercises.constant.CommonConstant.DEFAULT_PAGE;
import static com.atait.exercises.constant.CommonConstant.DEFAULT_PAGE_SIZE;
import static com.atait.exercises.constant.CommonConstant.INSERT_CHUNK_SIZE;

@Service
public class JobSurveyService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JobSurveyRepository jobSurveyRepository;

    @Autowired
    private JobSurveySpecification jobSurveySpecification;

    private Map<String,String> searchingJobFieldMapping;

    @PostConstruct
    public void cacheMapping(){
        //NOTE: To solve problem when request send response fieldName is not related to Dto fieldName
        // e.g. created_date (in JobResponse.class), created (in JobSurveyDTo.class)
        searchingJobFieldMapping = new HashMap<>();
        searchingJobFieldMapping.put("job_id","jobId");
        searchingJobFieldMapping.put("job_title","jobTitle");
        searchingJobFieldMapping.put("salary","salary");
        searchingJobFieldMapping.put("location","location");
        searchingJobFieldMapping.put("company_name","companyName");
        searchingJobFieldMapping.put("gender","gender");
        searchingJobFieldMapping.put("created_date","created");
        searchingJobFieldMapping.put("salary_currency","salaryCurrency");
    }

    public SearchJobSurveyResponse searchingJobDataResponse(SearchJobSurveyRequest request) {
        List<Sort.Order> orders = CollectionUtils.isEmpty(request.getSortParamRequests()) ?
                Arrays.asList(new Sort.Order(Sort.Direction.DESC,searchingJobFieldMapping.get("job_id")))
                :request.getSortParamRequests().stream()
                .map(s -> new Sort.Order(s.getSortType().get(), searchingJobFieldMapping.get(s.getSortField())))
                .collect(Collectors.toList());

        Sort sort = Sort.by(orders);

        Pageable p = PageRequest.of(Math.max(request.getPage() - 1, DEFAULT_PAGE), Math.max(request.getPageSize(), DEFAULT_PAGE_SIZE), sort);

        Date startDate = StringUtils.isEmpty(request.getStartDate())? null : DateUtils.strtoDate(DateUtils.DMY_PATTERN,request.getStartDate(), DateUtils.DateParsingOption.START_OF_DAY);
        Date endDate = StringUtils.isEmpty(request.getEndDate())? null : DateUtils.strtoDate(DateUtils.DMY_PATTERN,request.getEndDate(), DateUtils.DateParsingOption.END_OF_DAY);

        JobSurveySpecificationDTO specificationDTO = new JobSurveySpecificationDTO(request.getSalaryConditions(), startDate,endDate,request.getJobTitle(),request.getGender());
        Page<JobSurveyEntity> result = jobSurveyRepository.findAll(jobSurveySpecification.createJobSurveySpecification(specificationDTO), p);

        SearchJobSurveyResponse response = new SearchJobSurveyResponse();
        response.setJobs(result.stream().map(dto -> JobSurveyDTOMapper.INSTANCE.dtoToJobResponse(dto, request.getFields())).toList());
        response.setPage(result.getNumber() + 1);
        response.setPageSize(result.getNumberOfElements());
        response.setTotalItems(result.getTotalElements());
        response.setTotalPages(result.getTotalPages());

        return response;
    }

    public void insertJobSurveyData() {
        try {
            String salarySurveyFile = "setup/salary_survey-3.json";
            List<SalarySurvey> res = mapper.readValue(new File(salarySurveyFile), new TypeReference<>() {
            });

            List<JobSurveyEntity> jobSurveyDTOS = new ArrayList<>();
            List<SalarySurvey> uncleanSurveyData = new ArrayList<>();
            for (int i = 0; i < res.size(); i++) {
                SalarySurvey salarySurvey = res.get(i);
                try {
                    JobSurveyEntity dto = JobSurveyDTOMapper.INSTANCE.sourceJsonToDTO(salarySurvey);
                    jobSurveyDTOS.add(dto);
                } catch (SourceDataMapperException e) {
                    uncleanSurveyData.add(salarySurvey);
                    continue;
                }
                if (i == res.size() - 1 || jobSurveyDTOS.size() == INSERT_CHUNK_SIZE) {
                    jobSurveyRepository.saveAll(jobSurveyDTOS);
                    jobSurveyDTOS.clear();
                }
            }
            mapper.writeValue(Paths.get("./setup/unclean_data_" + Calendar.getInstance().getTimeInMillis() + ".json").toFile(), uncleanSurveyData);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
