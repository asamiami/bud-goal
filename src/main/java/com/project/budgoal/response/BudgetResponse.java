package com.project.budgoal.response;

import com.project.budgoal.entites.Transaction;

import java.util.List;

public record BudgetResponse(

        String name,

        Long amount,

        List<Transaction> transactions,

        Integer members
) {
}
