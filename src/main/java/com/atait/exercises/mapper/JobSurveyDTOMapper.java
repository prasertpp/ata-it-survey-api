package com.atait.exercises.mapper;


import com.atait.exercises.mapper.decorator.JobSurveyDecorator;
import com.atait.exercises.model.entity.JobSurveyEntity;
import com.atait.exercises.model.response.JobResponse;
import com.atait.exercises.model.source.SalarySurvey;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
@DecoratedWith(JobSurveyDecorator.class)
public interface JobSurveyDTOMapper {
    JobSurveyDTOMapper INSTANCE = Mappers.getMapper(JobSurveyDTOMapper.class);

    @Mapping(source = "survey.timestamp", target = "created", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "survey.employer", target = "companyName")
    @Mapping(source = "survey.jobTitle", target = "jobTitle")
    @Mapping(source = "survey.yearsAtEmployer", target = "yearAtEmployer")
    @Mapping(source = "survey.yearsOfExperience", target = "totalYearExperience")
    @Mapping(source = "survey.salary", target = "salary")
    @Mapping(source = "survey.signingBonus", target = "signingBonus")
    @Mapping(source = "survey.annualBonus", target = "annualBonus")
    @Mapping(source = "survey.annualStockValueBonus", target = "annualStockValueBonus")
    @Mapping(source = "survey.gender", target = "gender")
    @Mapping(source = "survey.additionalComments", target = "comments")
    @Mapping(target = "jobId",ignore = true)
    @Mapping(target = "salaryCurrency",ignore = true)
    JobSurveyEntity sourceJsonToDTO(SalarySurvey survey);


    @Mapping(source = "dto.jobId" ,target = "jobId")
    @Mapping(source = "dto.companyName", target = "companyName")
    @Mapping(source = "dto.location", target = "location")
    @Mapping(source = "dto.jobTitle", target = "jobTitle")
    @Mapping(source = "dto.salary", target = "salary")
    @Mapping(source = "dto.created", target = "createdDate")
    @Mapping(source = "dto.gender", target = "gender")
    @Mapping(source = "dto.salaryCurrency", target = "salaryCurrency")
    JobResponse dtoToJobResponse(JobSurveyEntity dto, List<String> fields);


}
