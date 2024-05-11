package com.project.budgoal.repository;

import com.project.budgoal.entites.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findUsersById(Long id);
    Optional<Users> findById(Long userId);

    Optional<Users> findByEmail(String email);
}
