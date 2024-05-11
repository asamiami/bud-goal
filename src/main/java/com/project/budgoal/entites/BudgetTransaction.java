package com.project.budgoal.entites;

import com.project.budgoal.enums.Category;
import com.project.budgoal.enums.TransactionCatgory;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder

public class BudgetTransaction extends BaseEntity{

    private Long amount;

    private TransactionCatgory category;

    @ManyToOne
    private  Budget userBudget;




}
