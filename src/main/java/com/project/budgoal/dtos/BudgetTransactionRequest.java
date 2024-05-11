package com.project.budgoal.dtos;

import com.project.budgoal.enums.TransactionCatgory;

public record BudgetTransactionRequest(
        Long amount,

        TransactionCatgory category
) {
}
