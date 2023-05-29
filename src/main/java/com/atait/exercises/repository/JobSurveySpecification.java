package com.atait.exercises.repository;

import com.atait.exercises.model.dto.JobSurveyDTO;
import com.atait.exercises.model.request.SalaryCondition;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class JobSurveySpecification {

    public Specification<JobSurveyDTO> salary(List<SalaryCondition> salaryConditions){
        return (root,query,builder) ->
        {
            Predicate predicate = null;
            if(!CollectionUtils.isEmpty(salaryConditions)) {
                for (SalaryCondition condition : salaryConditions) {
                    Predicate conditionPredicate = getBuilderForOperator(root, builder, condition, "salary");
                    predicate = (predicate == null) ? conditionPredicate : builder.and(predicate, conditionPredicate);
                }
                return predicate;
            }
            return predicate;
        };
    }

    private Predicate getBuilderForOperator(Path<JobSurveyDTO> root, CriteriaBuilder builder,SalaryCondition salaryCondition,String fieldName){

        return switch(salaryCondition.operator()){
            case "<" -> builder.lessThan(root.get(fieldName),salaryCondition.value());
            case ">" -> builder.greaterThan(root.get(fieldName),salaryCondition.value());
            case "<=" -> builder.lessThanOrEqualTo(root.get(fieldName),salaryCondition.value());
            case ">=" -> builder.greaterThanOrEqualTo(root.get(fieldName),salaryCondition.value());
            case "=" -> builder.equal(root.get(fieldName),salaryCondition.value());
            default -> throw new RuntimeException("Unexpected value: " + salaryCondition.operator());
        };
    }
}
