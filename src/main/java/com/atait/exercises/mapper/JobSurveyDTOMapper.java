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

    @Mapping(source = "timestamp", target = "created", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "employer", target = "companyName")
    @Mapping(source = "jobTitle", target = "jobTitle")
    @Mapping(source = "yearsAtEmployer", target = "yearAtEmployer")
    @Mapping(source = "yearsOfExperience", target = "totalYearExperience")
    @Mapping(source = "salary", target = "salary")
    @Mapping(source = "signingBonus", target = "signingBonus")
    @Mapping(source = "annualBonus", target = "annualBonus")
    @Mapping(source = "annualStockValueBonus", target = "annualStockValueBonus")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "additionalComments", target = "comments")
    JobSurveyEntity sourceJsonToDTO(SalarySurvey survey);


    @Mapping(source = "dto.jobId" ,target = "jobId")
    @Mapping(source = "dto.companyName", target = "companyName")
    @Mapping(source = "dto.location", target = "location")
    @Mapping(source = "dto.jobTitle", target = "jobTitle")
    @Mapping(source = "dto.salary", target = "salary")
    @Mapping(source = "dto.created", target = "createdDate")
    @Mapping(source = "dto.gender", target = "gender")
    JobResponse dtoToJobResponse(JobSurveyEntity dto, List<String> fields);


}
