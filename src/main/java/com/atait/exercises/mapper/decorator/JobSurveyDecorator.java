package com.atait.exercises.mapper.decorator;

import com.atait.exercises.exception.MapperErrorException;
import com.atait.exercises.mapper.JobSurveyDTOMapper;
import com.atait.exercises.model.entity.JobSurveyEntity;
import com.atait.exercises.model.response.JobResponse;
import com.atait.exercises.model.source.SalarySurvey;
import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract  class JobSurveyDecorator implements JobSurveyDTOMapper {
    private final JobSurveyDTOMapper mapper;

    final Logger logger = LoggerFactory.getLogger(JobSurveyDecorator.class);

    public JobSurveyDecorator(JobSurveyDTOMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public JobSurveyEntity sourceJsonToDTO(SalarySurvey survey){
        try {
            JobSurveyEntity dto = mapper.sourceJsonToDTO(survey);
            if(survey.getSalary().indexOf("$")  != -1 || survey.getSalary().toLowerCase().indexOf("usd")!= -1 || Objects.nonNull(dto.getSalary())){
                dto.setSalaryCurrency("USD");
            }

            return dto;
        }catch (Exception e){
            logger.error("[JobSurveyDecorator]::sourceJsonToDTO can't map surveyJsonToDTO : {}",e);


            throw new MapperErrorException("Failed to map sourceJsonToDTO");
        }

    }

    @Override
    public JobResponse dtoToJobResponse(JobSurveyEntity dto, List<String> displayFields){
          JobResponse jobResponse = mapper.dtoToJobResponse(dto,displayFields);
        if(!CollectionUtils.isEmpty(displayFields)) {

            Arrays.stream(JobResponse.class.getDeclaredFields())
                    .filter(field -> {
                        var displayFieldInCamel = displayFields.stream().map(displayField -> CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,displayField));
                        return !displayFieldInCamel.anyMatch(x -> field.getName().equals(x));
                    })
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            field.set(jobResponse, null);
                        } catch (IllegalAccessException | IllegalArgumentException e) {
                            logger.error("[JobSurveyDecorator]::dtoToJobResponse failed to set visibility on field :{}",e);
                            throw new MapperErrorException("Failed to map dtoToJobResponse");
                        }
                    });
        }
          return jobResponse;

    }





}
