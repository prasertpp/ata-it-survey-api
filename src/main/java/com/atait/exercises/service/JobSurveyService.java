package com.atait.exercises.service;

import com.atait.exercises.exception.SourceDataMapperException;
import com.atait.exercises.mapper.JobSurveyDTOMapper;
import com.atait.exercises.model.dto.JobSurveyDTO;
import com.atait.exercises.model.request.SearchJobSurveyRequest;
import com.atait.exercises.model.response.SearchJobSurveyResponse;
import com.atait.exercises.model.source.SalarySurvey;
import com.atait.exercises.repository.JobSurveyRepository;
import com.atait.exercises.repository.JobSurveySpecification;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.atait.exercises.constant.CommonConstant.INSERT_CHUNK_SIZE;

@Service
public class JobSurveyService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JobSurveyRepository jobSurveyRepository;

    @Autowired
    private JobSurveySpecification jobSurveySpecification;

    public SearchJobSurveyResponse searchingJobDataResponse(SearchJobSurveyRequest request){


        Sort sorting =  Sort.by("created").descending(); //fixme fix here to allow from request
        Pageable p = PageRequest.of(Math.max(request.getPage()-1,0) ,Math.max(request.getPageSize(),10),sorting);

        var result = jobSurveyRepository.findAll(jobSurveySpecification.salary(request.getSalaryConditions()),p);

        SearchJobSurveyResponse response = new SearchJobSurveyResponse();
        response.setJobs(result.stream().map(dto -> JobSurveyDTOMapper.INSTANCE.dtoToJobResponse(dto,request.getFields())).collect(Collectors.toList()));
        response.setPage(result.getNumber()+1);
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
            System.out.println(res);

            List<JobSurveyDTO> jobSurveyDTOS = new ArrayList<>();
            List<SalarySurvey> uncleanSurveyData = new ArrayList<>();
            for (int i = 0; i < res.size(); i++) {
                SalarySurvey salarySurvey = res.get(i);
                try {
                    JobSurveyDTO dto = JobSurveyDTOMapper.INSTANCE.surveyJsonToDTO(salarySurvey);
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
            mapper.writeValue(Paths.get("./setup/unclean_data_"+Calendar.getInstance().getTimeInMillis()+".json").toFile(),uncleanSurveyData);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
