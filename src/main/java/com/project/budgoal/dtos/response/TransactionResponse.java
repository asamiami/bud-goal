package com.project.budgoal.dtos.response;

import com.project.budgoal.enums.TransactionCatgory;

public record TransactionResponse(


    TransactionCatgory category,

    Long amount



) {
}
