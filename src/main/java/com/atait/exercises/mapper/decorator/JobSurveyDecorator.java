package com.atait.exercises.mapper.decorator;

import com.atait.exercises.exception.IllegalDisplayFieldNameException;
import com.atait.exercises.exception.SourceDataMapperException;
import com.atait.exercises.mapper.JobSurveyDTOMapper;
import com.atait.exercises.model.entity.JobSurveyEntity;
import com.atait.exercises.model.response.JobResponse;
import com.atait.exercises.model.source.SalarySurvey;
import com.google.common.base.CaseFormat;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public abstract  class JobSurveyDecorator implements JobSurveyDTOMapper {
    private final JobSurveyDTOMapper mapper;

    public JobSurveyDecorator(JobSurveyDTOMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public JobSurveyEntity sourceJsonToDTO(SalarySurvey survey){
        try {
            JobSurveyEntity dto = mapper.sourceJsonToDTO(survey);
//FIXME do this?            dto.setSalaryCurrency("USD");
            return dto;
        }catch (Exception e){
            throw new SourceDataMapperException("Can't map surveyJsonToDTO , "+e);
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
                        } catch (IllegalAccessException e) {
                            throw new IllegalDisplayFieldNameException("Cannot set Field in Response" + e);
                        }
                    });
        }
          return jobResponse;

    }



}
