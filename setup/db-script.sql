CREATE TABLE public."job_survey" (
id bigserial NOT NULL,
company_name varchar,
location varchar,
job_title varchar NOT NULL,
year_at_employer numeric,
total_year_experience numeric,
salary numeric NOT NULL CHECK (salary >= 0),
salary_currency varchar,
signing_bonus numeric,
annual_bonus numeric,
annual_stock_value_bonus varchar,
gender varchar NOT NULL,
comments varchar,
survey_date TIMESTAMP NOT NULL,
created TIMESTAMP NOT NULL,
CONSTRAINT job_survey_pk PRIMARY KEY (id)

);

CREATE INDEX job_survey_job_title_idx ON public.job_survey (job_title);
CREATE INDEX job_survey_salary_idx ON public.job_survey (salary);
CREATE INDEX job_survey_gender_idx ON public.job_survey (gender);
CREATE INDEX job_survey_survey_date_idx ON public.job_survey (survey_date DESC);



