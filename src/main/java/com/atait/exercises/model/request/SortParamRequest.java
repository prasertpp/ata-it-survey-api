package com.atait.exercises.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Data
@AllArgsConstructor
@Validated
public class SortParamRequest {
    @NotBlank(message = "sort cannot be empty")
    private String sortField;

    private Optional<Sort.Direction> sortType;
}
