package com.atait.exercises.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JobResponse {
    private String jobId;
    private String jobTitle;
    private BigDecimal salary;
    private String location;
    private String companyName;
    //FIXME add column ??
}
