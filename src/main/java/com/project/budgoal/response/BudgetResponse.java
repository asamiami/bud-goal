package com.project.budgoal.response;

import com.project.budgoal.entites.BudgetTransaction;

import java.util.List;

public record BudgetResponse(

        String name,

        Long amount,

        List<BudgetTransaction> budgetTransactions,

        Integer members
) {
}
