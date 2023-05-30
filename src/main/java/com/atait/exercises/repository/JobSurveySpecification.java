package com.atait.exercises.repository;

import com.atait.exercises.enums.OperatorEnum;
import com.atait.exercises.model.entity.JobSurveyEntity;
import com.atait.exercises.model.request.SalaryCondition;
import com.atait.exercises.model.dto.JobSurveySpecificationDTO;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

@Component
public class JobSurveySpecification {

    public Specification<JobSurveyEntity> createJobSurveySpecification(JobSurveySpecificationDTO spec){
        return (root,query,builder) ->
        {
            Predicate predicate = null;
            if(!CollectionUtils.isEmpty(spec.salaryConditions())) {
                for (SalaryCondition condition : spec.salaryConditions()) {
                    Predicate conditionPredicate = getBuilderForOperator(root, builder, condition, "salary");
                    predicate = Objects.isNull(predicate) ? conditionPredicate : builder.and(predicate, conditionPredicate);
                }
            }

            if(Objects.nonNull(spec.startDate())){
                Predicate startDatePredicate = builder.greaterThanOrEqualTo(root.get("created"),spec.startDate());
                predicate = Objects.isNull(predicate) ? startDatePredicate: builder.and(predicate,startDatePredicate);
            }


            if(Objects.nonNull(spec.endDate())){
                Predicate endDatePredicate = builder.lessThanOrEqualTo(root.get("created"),spec.endDate());
                predicate = Objects.isNull(predicate) ? endDatePredicate: builder.and(predicate,endDatePredicate);
            }

            if(!StringUtils.isBlank(spec.jobTitle())){
                String pattern ="%"+ spec.jobTitle().toLowerCase()+ "%";
                Predicate jobTitlePredicate = builder.like(builder.lower(root.get("jobTitle")),pattern);
                predicate = Objects.isNull(predicate) ? jobTitlePredicate: builder.and(predicate,jobTitlePredicate);
            }

            if(!StringUtils.isBlank(spec.gender())){
                Predicate genderPredicate = builder.equal(builder.lower(root.get("gender")),spec.gender().toLowerCase());
                predicate = Objects.isNull(predicate) ? genderPredicate: builder.and(predicate,genderPredicate);
            }

            return predicate;
        };
    }

    private Predicate getBuilderForOperator(Path<JobSurveyEntity> root, CriteriaBuilder builder, SalaryCondition salaryCondition, String fieldName){

        return switch(salaryCondition.operator()){
            case LT -> builder.lessThan(root.get(fieldName),salaryCondition.value());
            case GT -> builder.greaterThan(root.get(fieldName),salaryCondition.value());
            case LTE -> builder.lessThanOrEqualTo(root.get(fieldName),salaryCondition.value());
            case GTE -> builder.greaterThanOrEqualTo(root.get(fieldName),salaryCondition.value());
            case EQ -> builder.equal(root.get(fieldName),salaryCondition.value());
            default -> throw new RuntimeException("Unexpected value: " + salaryCondition.operator());
        };
    }
}
