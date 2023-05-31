# Data IT Survey API

The Data IT Survey API is used for searching data from client surveys.

## Prerequisites

Before starting the application, make sure you have the following prerequisites installed on your machine:

1. Docker (Ensure that port 5432 is not in use as it will be used for the database)
2. Java (Version 20)

## Setup

1. Run the following command to start the Docker containers for the setup:

   ```shell
   docker-compose -f setup/docker-compose.yml up -d
   ```

   This command will create the database and initialize the required tables.

2. Use the following command to install sources files before running code
    
    ```shell
     mvn install -DskipTests
    ```

3. Use the following curl command to insert data into the API:

   ```shell
   curl --location --request POST 'http://localhost:8080/survey/api/v1/job_data' \
   --data-raw ''
   ```

   This step will insert the data into the API.

4. Once the setup is complete, you can start searching.

## Documentation

Access the API documentation using the following URLs:

1. Swagger UI: [http://localhost:8080/survey/swagger-ui/index.html#/job-data-controller/jobDataSearching](http://localhost:8080/survey/swagger-ui/index.html#/job-data-controller/jobDataSearching)
2. Swagger API Docs: [http://localhost:8080/survey/v3/api-docs](http://localhost:8080/survey/v3/api-docs)

## Constraints

- The project only inserts a partial set of values from the `salary-survey-3.json` file.
- Database (DB) constraints:
    - `job_title` is a required `varchar` field.
    - `salary` is a required `numeric` field that must have a value greater than or equal to 0.
    - `gender` is a required `varchar` field.
    - `survey_date` is a required `TIMESTAMP` field.
- API constraints:
    - When inserting data, the `salary` must be greater than or equal to 100.