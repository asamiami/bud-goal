package com.project.budgoal.entites;

import com.project.budgoal.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity

@Getter
@Setter
@NoArgsConstructor
public class Savings extends BaseEntity{


    private String savingsName;

    private Long targetAmount;




    @Enumerated(EnumType.STRING)
    private Category savingsCategory;

    private Integer savingsMembers;

    private LocalDate startDate;
    private LocalDate endDate;


    @ElementCollection
    @MapKeyColumn(name = "transaction_key")
    @Column(name = "transaction_value")
    private Map<String, Long> transactions;


    @ManyToMany

    private List<Users> usersList;


    public void addUsersToUserList (Users user){

         usersList.add(user);

    }

    public Long addTransactionToTransactionsList (String transactionName, Long amount){
        return transactions.put(transactionName, amount);
    }
}
