package com.atait.exercises.controller;

import com.atait.exercises.exception.MapperErrorException;
import com.atait.exercises.model.response.JobResponse;
import com.atait.exercises.model.response.SearchJobSurveyResponse;
import com.atait.exercises.service.JobSurveyService;
import com.atait.exercises.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(JobDataController.class)
public class JobDataControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ObjectMapper mapper;

    @MockBean
    private JobSurveyService jobSurveyService;

    @SpyBean
    private Validator validator;

    @Test
    public void test_jobDataSearching_success() throws Exception {
        SearchJobSurveyResponse resp = new SearchJobSurveyResponse();
        resp.setPage(1);
        resp.setPageSize(10);
        resp.setTotalItems(1L);
        resp.setTotalPages(1);
        resp.setJobs(Arrays.asList(new JobResponse(1L, "Software developer",
                BigDecimal.valueOf(135000),
                "Chicago",
                "Discover",
                "Male",
                "USD",
                DateUtils.strtoLocalDateTime(DateUtils.DDMMYYYY_SLASH_PATTERN, "11/02/2020", DateUtils.DateParsingOption.START_OF_DAY))));
        when(jobSurveyService.searchingJobDataResponse(any())).thenReturn(resp);

        mockMvc.perform(
                get("/api/v1/job_data")
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("SUCCESS")))
                .andExpect(jsonPath("$.status.message", is("success")))
                .andExpect(jsonPath("$.status.errors").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.data.page", is(1)))
                .andExpect(jsonPath("$.data.page_size", is(10)))
                .andExpect(jsonPath("$.data.total_items", is(1)))
                .andExpect(jsonPath("$.data.total_pages", is(1)))
                .andExpect(jsonPath("$.data.jobs[0].job_id", is(1)))
                .andExpect(jsonPath("$.data.jobs[0].job_title", is("Software developer")))
                .andExpect(jsonPath("$.data.jobs[0].salary", is(135000)))
                .andExpect(jsonPath("$.data.jobs[0].location", is("Chicago")))
                .andExpect(jsonPath("$.data.jobs[0].company_name", is("Discover")))
                .andExpect(jsonPath("$.data.jobs[0].gender", is("Male")))
                .andExpect(jsonPath("$.data.jobs[0].salary_currency", is("USD")))
                .andExpect(jsonPath("$.data.jobs[0].created_date", is("11/02/2020")));
    }

    @Test
    public void test_jobDataSearching_success_params_allParam() throws Exception {
        SearchJobSurveyResponse resp = new SearchJobSurveyResponse();
        resp.setPage(1);
        resp.setPageSize(10);
        resp.setTotalItems(1L);
        resp.setTotalPages(1);
        resp.setJobs(Arrays.asList(new JobResponse(1L, "Software developer",
                BigDecimal.valueOf(135000),
                "Chicago",
                "Discover",
                "Male",
                "USD",
                DateUtils.strtoLocalDateTime(DateUtils.DDMMYYYY_SLASH_PATTERN, "11/02/2020", DateUtils.DateParsingOption.START_OF_DAY))));
        when(jobSurveyService.searchingJobDataResponse(any())).thenReturn(resp);

        mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("start_date", "11/02/2020")
                        .queryParam("end_date", "11/02/2020")
                        .queryParam("job_title", "soft")
                        .queryParam("gender", "male")
                        .queryParam("page", "1")
                        .queryParam("page_size", "10")
                        .queryParam("field", "job_id," + "job_title," + "salary," + "location," + "company_name," + "gender," + "salary_currency," + "created_date")
                        .queryParam("sort", "job_id")
                        .queryParam("sort_type", "desc")
                        .queryParam("salary[gte]", "2000")
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("SUCCESS")))
                .andExpect(jsonPath("$.status.message", is("success")))
                .andExpect(jsonPath("$.status.errors").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.data.page", is(1)))
                .andExpect(jsonPath("$.data.page_size", is(10)))
                .andExpect(jsonPath("$.data.total_items", is(1)))
                .andExpect(jsonPath("$.data.total_pages", is(1)))
                .andExpect(jsonPath("$.data.jobs[0].job_id", is(1)))
                .andExpect(jsonPath("$.data.jobs[0].job_title", is("Software developer")))
                .andExpect(jsonPath("$.data.jobs[0].salary", is(135000)))
                .andExpect(jsonPath("$.data.jobs[0].location", is("Chicago")))
                .andExpect(jsonPath("$.data.jobs[0].company_name", is("Discover")))
                .andExpect(jsonPath("$.data.jobs[0].gender", is("Male")))
                .andExpect(jsonPath("$.data.jobs[0].salary_currency", is("USD")))
                .andExpect(jsonPath("$.data.jobs[0].created_date", is("11/02/2020")));
    }

    @Test
    public void test_jobDataSearching_failed_getSalaryConditions_invalid_operator() throws Exception {
        mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("salary[ls]", "10")
        ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.status.message", is("invalid parameter")))
                .andExpect(jsonPath("$.status.errors[0]", is("[ls] is invalid value, operators supports only [gt],[lt],[eq],[gte],[lte]")));
    }

    @Test
    public void test_jobDataSearching_failed_getSalaryConditions_invalid_value() throws Exception {
        mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("salary[gt]", "abc")
        ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.status.message", is("invalid parameter")))
                .andExpect(jsonPath("$.status.errors[0]", is("salary[gt] is invalid value")));
    }

    @Test
    public void test_jobDataSearching_failed_getSortParamRequests_invalid_sort_sortType_size() throws Exception {
        var noSort = mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("sort_type", "abc")
        );
        var noSortType = mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("sort", "job_id")
        );
        var sizeUnequal = mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("sort", "job_id,salary")
                        .queryParam("sort_type", "asc")
        );

        var performResults = Arrays.asList(noSort, noSortType, sizeUnequal);

        for (var each : performResults) {
            each.andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status.code", is("BAD_REQUEST")))
                    .andExpect(jsonPath("$.status.message", is("invalid parameter")))
                    .andExpect(jsonPath("$.status.errors[0]", is("sort and sort_type are invalid format, the number of sort field and sort_type must match")));
        }
    }


    @Test
    public void test_jobDataSearching_failed_validateFieldAndSorting_invalid_field_value() throws Exception {
        mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("field", "abc")
        ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.status.message", is("invalid parameter")))
                .andExpect(jsonPath("$.status.errors[0]", is("field support value only job_id,job_title,salary,location,company_name,gender,salary_currency,created_date")));
    }

    @Test
    public void test_jobDataSearching_failed_validateFieldAndSorting_invalid_sort_value() throws Exception {
        mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("sort", "abc")
                        .queryParam("sort_type", "asc")
        ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.status.message", is("invalid parameter")))
                .andExpect(jsonPath("$.status.errors[0]", is("sort support value only job_id,job_title,salary,location,company_name,gender,salary_currency,created_date")));
    }


    @Test
    public void test_jobDataSearching_failed_validateFieldAndSorting_invalid_sortType_value() throws Exception {
        mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("sort", "job_id")
                        .queryParam("sort_type", "asz")
        ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.status.message", is("invalid parameter")))
                .andExpect(jsonPath("$.status.errors[0]", is("sort_type support value only 'asc' or 'desc'")));
    }

    @Test
    public void test_jobDataSearching_failed_validateJobDataSearchingValue_multiple_error_startDate_endDate_pageSize() throws Exception {
        mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("start_date", "12/29/2022")
                        .queryParam("end_date", "12/29/2022")
                        .queryParam("page_size", "1200")
        ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.status.message", is("invalid parameter")))

                .andExpect(jsonPath("$.status.errors", containsInAnyOrder(
                        "start_date is invalid format, format is dd/MM/yyyy",
                        "end_date is invalid format, format is dd/MM/yyyy",
                        "page_size is limited at 1000"
                        )
                ));
    }

    @Test
    public void test_jobDataSearching_failed_validateJobDataSearchingValue_endDate_before_startDate() throws Exception {
        mockMvc.perform(
                get("/api/v1/job_data")
                        .queryParam("start_date", "13/12/2022")
                        .queryParam("end_date", "12/12/2022")

        ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.status.message", is("invalid parameter")))
                .andExpect(jsonPath("$.status.errors[0]", is("start_date is after end_date")));
    }

    @Test
    public void test_jobDataSearching_unknown_internalServerError() throws Exception {
        when(jobSurveyService.searchingJobDataResponse(any())).thenThrow(new RuntimeException("other Exception"));

        mockMvc.perform(
                get("/api/v1/job_data")
        ).andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("INTERNAL_SERVER_ERROR")))
                .andExpect(jsonPath("$.status.message", is("INTERNAL_SERVER_ERROR")))
                .andExpect(jsonPath("$.status.errors").doesNotHaveJsonPath());

    }

    @Test
    public void test_jobDataSearching_mapping_internalServerError() throws Exception {
        when(jobSurveyService.searchingJobDataResponse(any())).thenThrow(new MapperErrorException("ca"));

        mockMvc.perform(
                get("/api/v1/job_data")
        ).andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is("INTERNAL_SERVER_ERROR")))
                .andExpect(jsonPath("$.status.message", is("INTERNAL_SERVER_ERROR")))
                .andExpect(jsonPath("$.status.errors").doesNotHaveJsonPath());

    }


}
