package com.project.budgoal.entites;

import com.project.budgoal.enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity

@Getter
@Setter
@NoArgsConstructor
public class Savings extends BaseEntity{

    @NonNull
    private String savingsName;

    private Long targetAmount;



    @NonNull
    @Enumerated(EnumType.STRING)
    private Category savingsCategory;

    private Integer savingsMembers;

    private LocalDate startDate;
    private LocalDate endDate;


    @ElementCollection
    @CollectionTable(name = "transactions", joinColumns = @JoinColumn(name = "savings_id"))
    @MapKeyColumn(name = "transaction_key")
    @Column(name = "transaction_value")
    private Map<String, Long> transactions;


    @ManyToMany

    private List<Users> usersList;
}
