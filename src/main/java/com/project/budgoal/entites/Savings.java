package com.project.budgoal.entites;

import com.project.budgoal.enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity

@Getter
@Setter
@NoArgsConstructor
public class Savings extends BaseEntity{

    @NonNull
    private String savingsName;

    private Long savingAmount;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Category savingsCategory;

    private Integer savingsMembers;

    private Period savingsDuration;


    @ManyToMany

    private List<Users> usersList;
}
