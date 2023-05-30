package com.atait.exercises.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "job_survey", schema = "public")
@Data
public class JobSurveyEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "location")
    private String location;
    @Column(name = "job_title")
    private String jobTitle;
    @Column(name = "year_at_employer")
    private BigDecimal yearAtEmployer;
    @Column(name = "total_year_experience")
    private BigDecimal totalYearExperience;
    @Column(name = "salary")
    private BigDecimal salary;
    @Column(name = "salary_currency")
    private String salaryCurrency;
    @Column(name = "signing_bonus")
    private BigDecimal signingBonus;
    @Column(name = "annual_bonus")
    private BigDecimal annualBonus;
    @Column(name = "annual_stock_value_bonus")
    private BigDecimal annualStockValueBonus;
    @Column(name = "gender")
    private String gender;
    @Column(name = "comments")
    private String comments;
    @Column(name = "created")
    private Date created;
}
