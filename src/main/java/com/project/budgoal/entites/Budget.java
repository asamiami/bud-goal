package com.project.budgoal.entites;

import com.project.budgoal.enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.Period;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Budget extends BaseEntity {

    @NonNull
    private String budgetName;

    private Long budgetAmount;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Category budgetCategory;

    private Integer budgetMembers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userBudget")
    private List<BudgetTransaction> transaction;


    @ManyToMany
    @JoinTable(
            name = "users_budget",
            joinColumns = @JoinColumn(name = "budget_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    private List<Users> usersList;


}
