package com.atait.exercises.exception;

import lombok.Getter;

import java.util.List;

public class ValidationException extends RuntimeException{
    @Getter
    private List<String> errors;

    public ValidationException(String e){
        super(e);
    }

    public ValidationException(List<String> e){
        this.errors = e;
    }

}
