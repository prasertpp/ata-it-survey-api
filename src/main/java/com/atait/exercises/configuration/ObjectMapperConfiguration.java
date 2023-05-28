package com.atait.exercises.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper setupObjectMapper(){
        return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }
}
