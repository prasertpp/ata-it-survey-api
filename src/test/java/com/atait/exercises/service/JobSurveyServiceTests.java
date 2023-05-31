package com.atait.exercises.service;

import com.atait.exercises.model.entity.JobSurveyEntity;
import com.atait.exercises.model.request.SearchJobSurveyRequest;
import com.atait.exercises.model.request.SortParamRequest;
import com.atait.exercises.model.response.SearchJobSurveyResponse;
import com.atait.exercises.repository.JobSurveyRepository;
import com.atait.exercises.repository.JobSurveySpecification;
import com.atait.exercises.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

import static com.atait.exercises.constant.CommonConstant.DEFAULT_PAGE;
import static com.atait.exercises.constant.CommonConstant.DEFAULT_PAGE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;



class JobSurveyServiceTests {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private JobSurveyRepository jobSurveyRepository;

    @Spy
    private JobSurveySpecification jobSurveySpecification;

    @InjectMocks
    public JobSurveyService jobSurveyService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        jobSurveyService.cacheMapping();
    }

    @Test
    void test_searchingJobDataResponse_success() {
        JobSurveyEntity entity1 = new JobSurveyEntity();
        entity1.setJobId(2724L);
        entity1.setCompanyName("Discover");
        entity1.setLocation("Chicago");
        entity1.setJobTitle("Software developer");
        entity1.setYearAtEmployer(new BigDecimal(10));
        entity1.setTotalYearExperience(new BigDecimal(10));
        entity1.setSalary(new BigDecimal(135000));
        entity1.setSalaryCurrency("USD");
        entity1.setSigningBonus(new BigDecimal(5000));
        entity1.setAnnualBonus(new BigDecimal(10000));
        entity1.setAnnualStockValueBonus(new BigDecimal(3000));
        entity1.setGender("Male");
        entity1.setComments("");
        entity1.setSurveyDate(DateUtils.strtoDate(DateUtils.DDMMYYYY_SLASH_PATTERN, "12/02/2020", DateUtils.DateParsingOption.START_OF_DAY));
        entity1.setCreated(Calendar.getInstance().getTime());
        var dbResult = Arrays.asList(entity1);
        //set result to page 1 with result 10
        Page<JobSurveyEntity> jobSurveyEntities = new PageImpl<>(dbResult, PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE), dbResult.size());

        when(jobSurveyRepository.findAll(
                ArgumentMatchers.<Specification<JobSurveyEntity>>any(),
                ArgumentMatchers.<Pageable>any())).thenReturn(jobSurveyEntities);

        SearchJobSurveyRequest req = new SearchJobSurveyRequest(null, null, null, null, null, null, 1, 10, null);
        SearchJobSurveyResponse resp = jobSurveyService.searchingJobDataResponse(req);
        assertEquals(1, resp.getPage());
        assertEquals(10, resp.getPageSize());
        assertEquals(1, resp.getTotalItems());
        assertEquals(1, resp.getTotalPages());
        assertEquals(1, resp.getJobs().size());

        //assertJobs
        var jobResponse = resp.getJobs().get(0);
        assertEquals(entity1.getJobId(), jobResponse.getJobId());
        assertEquals(entity1.getJobTitle(), jobResponse.getJobTitle());
        assertEquals(entity1.getSalary(), jobResponse.getSalary());
        assertEquals(entity1.getLocation(), jobResponse.getLocation());
        assertEquals(entity1.getCompanyName(), jobResponse.getCompanyName());
        assertEquals(entity1.getGender(), jobResponse.getGender());
        assertEquals(entity1.getSalaryCurrency(), jobResponse.getSalaryCurrency());
        var dbDate = entity1.getSurveyDate().toInstant().atZone(ZoneId.of("UTC")).toLocalDate().atStartOfDay();
        assertTrue(dbDate.isEqual(jobResponse.getSurveyDate()));

    }

    @Test
    void test_searchingJobDataResponse_success_withSortOrder() {

        JobSurveyEntity entity1 = new JobSurveyEntity();
        entity1.setJobId(2724L);
        entity1.setCompanyName("Discover");
        entity1.setLocation("Chicago");
        entity1.setJobTitle("Software developer");
        entity1.setYearAtEmployer(new BigDecimal(10));
        entity1.setTotalYearExperience(new BigDecimal(10));
        entity1.setSalary(new BigDecimal(135000));
        entity1.setSalaryCurrency("USD");
        entity1.setSigningBonus(new BigDecimal(5000));
        entity1.setAnnualBonus(new BigDecimal(10000));
        entity1.setAnnualStockValueBonus(new BigDecimal(3000));
        entity1.setGender("Male");
        entity1.setComments("");
        entity1.setSurveyDate(DateUtils.strtoDate(DateUtils.DDMMYYYY_SLASH_PATTERN, "12/02/2020", DateUtils.DateParsingOption.START_OF_DAY));
        entity1.setCreated(Calendar.getInstance().getTime());

        var dbResult = Arrays.asList(entity1);
        //set result to page 1 with result 10
        Page<JobSurveyEntity> jobSurveyEntities = new PageImpl<>(dbResult, PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE), dbResult.size());

        when(jobSurveyRepository.findAll(
                ArgumentMatchers.<Specification<JobSurveyEntity>>any(),
                ArgumentMatchers.<Pageable>any())).thenReturn(jobSurveyEntities);

        var sortParamRequest = new SortParamRequest("job_id", Optional.of(Sort.Direction.DESC));
        SearchJobSurveyRequest req = new SearchJobSurveyRequest(null, null, null, null, null, null, 1, 10, Arrays.asList(sortParamRequest));
        SearchJobSurveyResponse resp = jobSurveyService.searchingJobDataResponse(req);
        assertEquals(1, resp.getPage());
        assertEquals(10, resp.getPageSize());
        assertEquals(1, resp.getTotalItems());
        assertEquals(1, resp.getTotalPages());
        assertEquals(1, resp.getJobs().size());

        //assertJobs
        var jobResponse = resp.getJobs().get(0);
        assertEquals(entity1.getJobId(), jobResponse.getJobId());
        assertEquals(entity1.getJobTitle(), jobResponse.getJobTitle());
        assertEquals(entity1.getSalary(), jobResponse.getSalary());
        assertEquals(entity1.getLocation(), jobResponse.getLocation());
        assertEquals(entity1.getCompanyName(), jobResponse.getCompanyName());
        assertEquals(entity1.getGender(), jobResponse.getGender());
        assertEquals(entity1.getSalaryCurrency(), jobResponse.getSalaryCurrency());
        var dbDate = entity1.getSurveyDate().toInstant().atZone(ZoneId.of("UTC")).toLocalDate().atStartOfDay();
        assertTrue(dbDate.isEqual(jobResponse.getSurveyDate()));
    }

}
