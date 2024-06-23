package com.project.budgoal.dtos.response;

import com.project.budgoal.enums.Category;
import lombok.Setter;

import java.time.Period;


public record SavingsResponse
        (
    String name,

    String duration,

    Category category,

    Integer memeber,

    Long amount,

    Integer transaction
        )
{


}
