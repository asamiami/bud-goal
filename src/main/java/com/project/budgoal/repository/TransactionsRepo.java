package com.project.budgoal.repository;

import com.project.budgoal.entites.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepo extends JpaRepository<Transaction, Long> {
}
