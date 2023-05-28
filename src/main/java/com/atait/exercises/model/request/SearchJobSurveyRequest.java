package com.atait.exercises.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchJobSurveyRequest {

    private String salary;

    @Value("#{'${fields}'.split(',')}")
    private List<String> fields;


    private Integer page = 1;

    private Integer pageSize = 10;

/*
Timestamp
Employer
Location
Job Title
Years at Employer
Years of Experience
Salary
Signing Bonus
Annual Bonus
Annual Stock Value/Bonus
Gender
Additional Comments
* */
}
