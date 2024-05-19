package com.project.budgoal.repository;

import com.project.budgoal.entites.Savings;
import com.project.budgoal.entites.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsRepo extends JpaRepository<Savings, Long> {

    Savings findSavingsById(Long idU);


    @Override
    List<Savings> findAll();
}
