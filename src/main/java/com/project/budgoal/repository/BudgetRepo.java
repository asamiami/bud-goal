package com.project.budgoal.repository;

import com.project.budgoal.entites.Budget;
import com.project.budgoal.entites.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepo extends JpaRepository<Budget, Long> {

        Budget findBudgetById (Long id);

        List<Budget> findAllByUsersList(Users user);;



}
