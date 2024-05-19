package com.project.budgoal.dtos;

import com.project.budgoal.enums.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record BudgetRequest(

        String  name,
        Long amount,

        @Enumerated(EnumType.STRING)
        Category budgetCategory



) {
}
