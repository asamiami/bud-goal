package com.project.budgoal.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.budgoal.enums.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.time.Period;

public record SavingsRequest(

        String name,

        Long amount,

        @Enumerated(EnumType.STRING)
        Category category,

        @JsonProperty(value = "time")
        LocalDate start,

        LocalDate endDate
) {
}
