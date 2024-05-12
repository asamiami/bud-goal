package com.project.budgoal.dtos;

import com.project.budgoal.enums.TransactionCatgory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record BudgetTransactionRequest(
        Long amount,

        @Enumerated(EnumType.STRING)
        TransactionCatgory category

) {
}
