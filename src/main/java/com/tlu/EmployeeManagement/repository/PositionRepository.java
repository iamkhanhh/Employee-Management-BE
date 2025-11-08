package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

    Optional<Position> findById(Integer id);

    @Query("SELECT p FROM Position p WHERE p.isDeleted = false")
    List<Position> findAllActive();

    Optional<Position> findByPositionName(String positionName);

    void deleteById(Integer id);
}
