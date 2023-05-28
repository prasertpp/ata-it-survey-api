package com.atait.exercises.repository;

import com.atait.exercises.model.dto.JobSurveyDTO;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class JobSurveySpecification {

    public Specification<JobSurveyDTO> salary(){
        return (root,query,builder) ->
        {
            Path<Integer> salaryObj = root.get("salary");

        return builder.and(builder.lessThan(salaryObj,100000), builder.greaterThan(salaryObj, 50000));

        };
    }
}
