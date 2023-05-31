package com.atait.exercises;

import com.atait.exercises.enums.StatusCode;
import com.atait.exercises.model.response.CommonResponse;
import com.atait.exercises.model.response.SearchJobSurveyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobSurveyBDDTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void test_getJobData_success_noParams() {
        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+"/survey/api/v1/job_data")
                .encode().toUriString();
        ResponseEntity<CommonResponse<SearchJobSurveyResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CommonResponse<SearchJobSurveyResponse>>() {}
        );

        CommonResponse<SearchJobSurveyResponse> body= response.getBody();

        assertEquals(StatusCode.SUCCESS,body.getStatus().code(),"body.status.code invalid");
        assertEquals("success",body.getStatus().message(),"body.status.message invalid");
        assertNull(body.getStatus().errors(),"body.status.errors invalid");
        SearchJobSurveyResponse data = body.getData();
        assertEquals(10,data.getPageSize(),"body.data.pageSize invalid");
        assertEquals(1,data.getPage(),"body.data.page invalid");
        assertEquals(10,data.getJobs().size(),"body.data.jobs size invalid");
    }


}
