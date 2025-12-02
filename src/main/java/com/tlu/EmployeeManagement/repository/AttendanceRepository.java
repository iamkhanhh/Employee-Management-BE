package com.tlu.EmployeeManagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    Optional<Attendance> findById(Integer id);

    List<Attendance> findByEmpId(Integer empId);

    @Query("SELECT a FROM Attendance a WHERE a.empId = :empId AND a.checkIn BETWEEN :startDate AND :endDate")
    List<Attendance> findByEmpIdAndDateRange(
        @Param("empId") Integer empId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM Attendance a WHERE a.checkIn BETWEEN :startDate AND :endDate")
    List<Attendance> findByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM Attendance a WHERE a.empId = :empId AND a.checkOut IS NULL")
    Optional<Attendance> findActiveAttendance(@Param("empId") Integer empId);

    @Query("SELECT a FROM Attendance a WHERE a.empId = :empId AND a.checkIn BETWEEN :startDate AND :endDate")
    Optional<Attendance> findAttendanceForToday(
        @Param("empId") Integer empId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM Attendance a WHERE a.empId IN :empIds AND a.checkIn BETWEEN :startDate AND :endDate")
    List<Attendance> findByEmpIdsAndDateRange(
        @Param("empIds") List<Integer> empIds,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );


   @Query("SELECT a FROM Attendance a WHERE a.empId = :empId AND MONTH(a.checkIn) = :month AND YEAR(a.checkIn) = :year")
    List<Attendance> findByEmpIdAndMonthAndYear(
            @Param("empId") Integer empId,
            @Param("month") int month,
            @Param("year") int year
    );

    void deleteById(Integer id);
}
