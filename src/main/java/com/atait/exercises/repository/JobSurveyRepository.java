package com.atait.exercises.repository;

import com.atait.exercises.model.dto.JobSurveyDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSurveyRepository extends JpaRepository<JobSurveyDTO,Long>, JpaSpecificationExecutor<JobSurveyDTO> {


}
