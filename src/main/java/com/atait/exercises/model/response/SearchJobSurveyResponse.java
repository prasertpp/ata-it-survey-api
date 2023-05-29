package com.atait.exercises.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchJobSurveyResponse {
    private List<JobResponse> jobs;
    private Long totalItems;
    private Integer totalPages;
    private Integer page;
    private Integer pageSize;
}
