package com.atait.exercises.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> implements Serializable {

    private static final long serialVersionUID = 3657167754944087268L;
    private Status status;
    private T data;
}