package com.project.budgoal.entites;

import com.project.budgoal.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.Map;

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

    @ElementCollection
    private Map<String, Long> transactions;


    @ManyToMany
    @JoinTable(
            name = "users_budget",
            joinColumns = @JoinColumn(name = "budget_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    private List<Users> usersList;


    public boolean addUsersToUserList (Users user){

        return usersList.add(user);

    }

    public Long addTransactionToTransactionsList (String transactionName, Long amount){
        return transactions.put(transactionName, amount);
    }
}
