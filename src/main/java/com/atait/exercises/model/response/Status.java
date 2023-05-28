package com.atait.exercises.model.response;

import com.atait.exercises.enums.StatusCode;

public record Status(StatusCode code, String message) {

}