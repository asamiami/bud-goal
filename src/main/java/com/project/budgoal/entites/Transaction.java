package com.project.budgoal.entites;

import com.project.budgoal.enums.TransactionCatgory;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseEntity{

    private Long amount;

    private TransactionCatgory category;

    @ManyToOne
    private  Budget userBudget;

    @ManyToOne
    private Savings savings;




}
