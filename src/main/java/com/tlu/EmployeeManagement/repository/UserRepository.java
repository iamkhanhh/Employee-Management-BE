package com.tlu.EmployeeManagement.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.User;
import com.tlu.EmployeeManagement.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    void deleteById(Integer id);

    @Query("SELECT COUNT(u) FROM User u")
    Integer countUsers();  
}
