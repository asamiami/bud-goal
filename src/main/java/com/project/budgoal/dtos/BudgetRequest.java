package com.project.budgoal.dtos;

import com.project.budgoal.entites.BudgetTransaction;
import com.project.budgoal.enums.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;

import java.time.Period;
import java.util.List;

public record BudgetRequest(

        String  name,
        Long amount,

        @Enumerated(EnumType.STRING)
        Category budgetCategory



) {
}
