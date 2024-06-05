package com.project.budgoal.response;

import java.util.Map;

public record BudgetResponse(

        String name,

        Long amount,

        Map<String, Long> transactions,

        Integer members
) {
}
