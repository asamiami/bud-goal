package com.project.budgoal.entites;

import com.project.budgoal.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Budget extends BaseEntity {


    private String budgetName;

    private Long budgetAmount;


    @Enumerated(EnumType.STRING)
    private Category budgetCategory;

    private Integer budgetMembers;

    @ElementCollection
    private Map<String, Long> transactions = new HashMap<>();


    @ManyToMany
    @JoinTable(
            name = "users_budget",
            joinColumns = @JoinColumn(name = "budget_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    private List<Users> usersList = new ArrayList<>();


    public void addUsersToUserList (Users user){

         usersList.add(user);

    }

    public Long addTransactionToTransactionsList (String transactionName, Long amount){
        return transactions.put(transactionName, amount);
    }
}
