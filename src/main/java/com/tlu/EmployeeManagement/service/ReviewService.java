package com.tlu.EmployeeManagement.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tlu.EmployeeManagement.dto.request.EmployeeReviewSubmitDto;
import com.tlu.EmployeeManagement.dto.request.KpiPeriodCreateDto;
import com.tlu.EmployeeManagement.dto.request.KpiPeriodFilterDto;
import com.tlu.EmployeeManagement.dto.request.KpiPeriodUpdateDto;
import com.tlu.EmployeeManagement.dto.request.KpiScoreSubmitDto;
import com.tlu.EmployeeManagement.dto.response.EmployeeReviewRecordDto;
import com.tlu.EmployeeManagement.dto.response.EmployeeReviewScoreDto;
import com.tlu.EmployeeManagement.dto.response.KpiCriteriaResponse;
import com.tlu.EmployeeManagement.dto.response.KpiPeriodResponse;
import com.tlu.EmployeeManagement.dto.response.KpiResultResponse;
import com.tlu.EmployeeManagement.dto.response.PagedResponse;
import com.tlu.EmployeeManagement.entity.Department;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.entity.KpiCriteria;
import com.tlu.EmployeeManagement.entity.KpiPeriod;
import com.tlu.EmployeeManagement.entity.KpiResults;
import com.tlu.EmployeeManagement.entity.KpiScore;
import com.tlu.EmployeeManagement.entity.User;
import com.tlu.EmployeeManagement.enums.RatingEmployeeType;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;
import com.tlu.EmployeeManagement.enums.UserRole;
import com.tlu.EmployeeManagement.exception.ResourceNotFoundException;
import com.tlu.EmployeeManagement.repository.DepartmentRepository;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;
import com.tlu.EmployeeManagement.repository.KpiCriteriaRepository;
import com.tlu.EmployeeManagement.repository.KpiPeriodRepository;
import com.tlu.EmployeeManagement.repository.KpiResultsRepository;
import com.tlu.EmployeeManagement.repository.KpiScoreRepository;
import com.tlu.EmployeeManagement.repository.UserRepository;
import com.tlu.EmployeeManagement.specification.KpiPeriodSpecification;
import com.tlu.EmployeeManagement.util.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {

    KpiPeriodRepository kpiPeriodRepository;
    KpiCriteriaRepository kpiCriteriaRepository;
    KpiScoreRepository kpiScoreRepository;
    KpiResultsRepository kpiResultsRepository;
    EmployeeRepository employeeRepository;
    UserRepository userRepository;
    DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public PagedResponse<KpiPeriodResponse> getKpiPeriods(KpiPeriodFilterDto filterDto) {
        Specification<KpiPeriod> spec = KpiPeriodSpecification.filterKpiPeriods(
            filterDto.getPeriodName(),
            filterDto.getStartDate(),
            filterDto.getEndDate()
        );

        Pageable pageable = PageRequest.of(
            filterDto.getPage(),
            filterDto.getPageSize(),
            Sort.by(Sort.Direction.DESC, "startDate")
        );

        Page<KpiPeriod> periodPage = kpiPeriodRepository.findAll(spec, pageable);

        List<KpiPeriodResponse> responses = periodPage.getContent().stream()
            .map(this::mapToKpiPeriodResponse)
            .collect(Collectors.toList());

        return PagedResponse.<KpiPeriodResponse>builder()
            .content(responses)
            .currentPage(periodPage.getNumber())
            .pageSize(periodPage.getSize())
            .totalElements(periodPage.getTotalElements())
            .totalPages(periodPage.getTotalPages())
            .hasNext(!periodPage.isLast())
            .hasPrevious(periodPage.hasPrevious())
            .build();
    }

    @Transactional(readOnly = true)
    public KpiPeriodResponse getKpiPeriodById(Integer id) {
        KpiPeriod period = kpiPeriodRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("KPI period not found with id: " + id));

        if (period.getIsDeleted()) {
            throw new ResourceNotFoundException("KPI period not found with id: " + id);
        }

        return mapToKpiPeriodResponse(period);
    }

    @Transactional
    public KpiPeriodResponse createKpiPeriod(KpiPeriodCreateDto createDto) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admin can create KPI periods");
        }

        if (createDto.getEndDate().isBefore(createDto.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        KpiPeriod period = KpiPeriod.builder()
            .periodName(createDto.getPeriodName())
            .startDate(createDto.getStartDate())
            .endDate(createDto.getEndDate())
            .build();

        KpiPeriod savedPeriod = kpiPeriodRepository.save(period);
        return mapToKpiPeriodResponse(savedPeriod);
    }

    @Transactional
    public KpiPeriodResponse updateKpiPeriod(Integer id, KpiPeriodUpdateDto updateDto) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admin can update KPI periods");
        }

        KpiPeriod period = kpiPeriodRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("KPI period not found with id: " + id));

        if (period.getIsDeleted()) {
            throw new ResourceNotFoundException("KPI period not found with id: " + id);
        }

        if (updateDto.getPeriodName() != null) {
            period.setPeriodName(updateDto.getPeriodName());
        }

        if (updateDto.getStartDate() != null) {
            period.setStartDate(updateDto.getStartDate());
        }

        if (updateDto.getEndDate() != null) {
            period.setEndDate(updateDto.getEndDate());
        }

        if (period.getEndDate().isBefore(period.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        KpiPeriod updatedPeriod = kpiPeriodRepository.save(period);
        return mapToKpiPeriodResponse(updatedPeriod);
    }

    @Transactional
    public void deleteKpiPeriod(Integer id) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admin can delete KPI periods");
        }

        KpiPeriod period = kpiPeriodRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("KPI period not found with id: " + id));

        period.setIsDeleted(true);
        kpiPeriodRepository.save(period);
    }

    @Transactional(readOnly = true)
    public List<EmployeeReviewRecordDto> getEmployeeReviewRecords(Integer periodId) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        KpiPeriod period = kpiPeriodRepository.findById(periodId)
            .orElseThrow(() -> new ResourceNotFoundException("KPI period not found with id: " + periodId));

        if (period.getIsDeleted()) {
            throw new ResourceNotFoundException("KPI period not found with id: " + periodId);
        }

        List<KpiResults> results = kpiResultsRepository.findByPeriodId(periodId);
        List<KpiCriteria> allCriteria = kpiCriteriaRepository.findAllActive();

        List<EmployeeReviewRecordDto> records = new ArrayList<>();

        for (KpiResults result : results) {
            Employee employee = employeeRepository.findById(result.getEmpId())
                .orElse(null);

            if (employee == null) {
                continue;
            }

            if (user.getRole() == UserRole.ADMIN) {
            } else {
                Employee currentEmployee = employeeRepository.findByUserId(currentUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

                if (currentEmployee.getRoleInDept() == RoleInDepartment.HEAD) {
                    if (!employee.getDeptId().equals(currentEmployee.getDeptId())) {
                        continue;
                    }
                } else {
                    if (!employee.getId().equals(currentEmployee.getId())) {
                        continue;
                    }
                }
            }

            List<KpiScore> empScores = kpiScoreRepository.findByEmpIdAndPeriodId(employee.getId(), periodId);
            Map<Integer, KpiScore> scoreMap = empScores.stream()
                .collect(Collectors.toMap(KpiScore::getKpiCriteriaId, score -> score));

            List<EmployeeReviewScoreDto> scoreDtos = new ArrayList<>();
            for (KpiCriteria criteria : allCriteria) {
                KpiScore score = scoreMap.get(criteria.getId());
                if (score != null) {
                    scoreDtos.add(EmployeeReviewScoreDto.builder()
                        .criteriaId(criteria.getId())
                        .criteriaName(criteria.getName())
                        .weight(criteria.getWeight())
                        .scoreValue(score.getScoreValue())
                        .build());
                }
            }

            Department department = null;
            if (employee.getDeptId() != null) {
                department = departmentRepository.findById(employee.getDeptId()).orElse(null);
            }

            records.add(EmployeeReviewRecordDto.builder()
                .id(result.getId())
                .empId(employee.getId())
                .empName(employee.getFullName())
                .deptId(employee.getDeptId())
                .deptName(department != null ? department.getDeptName() : null)
                .scores(scoreDtos)
                .averageScore(result.getFinalScore())
                .finalRating(result.getRating().name())
                .comment(result.getComment())
                .build());
        }

        return records;
    }

    @Transactional(readOnly = true)
    public EmployeeReviewRecordDto getEmployeeReviewRecordById(Integer periodId, Integer reviewId) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify period exists
        KpiPeriod period = kpiPeriodRepository.findById(periodId)
            .orElseThrow(() -> new ResourceNotFoundException("KPI period not found with id: " + periodId));

        if (period.getIsDeleted()) {
            throw new ResourceNotFoundException("KPI period not found with id: " + periodId);
        }

        // Get the KPI result by reviewId
        KpiResults result = kpiResultsRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        if (result.getIsDeleted()) {
            throw new ResourceNotFoundException("Review not found with id: " + reviewId);
        }

        // Verify the result belongs to the specified period
        if (!result.getKpiPeriodId().equals(periodId)) {
            throw new RuntimeException("Review does not belong to the specified period");
        }

        // Get employee and verify access permissions
        Employee employee = employeeRepository.findById(result.getEmpId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + result.getEmpId()));

        // Check permissions
        if (user.getRole() != UserRole.ADMIN) {
            Employee currentEmployee = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

            if (currentEmployee.getRoleInDept() == RoleInDepartment.HEAD) {
                // Department heads can only see reviews in their department
                if (!employee.getDeptId().equals(currentEmployee.getDeptId())) {
                    throw new RuntimeException("You don't have permission to view this review");
                }
            } else {
                // Regular employees can only see their own review
                if (!employee.getId().equals(currentEmployee.getId())) {
                    throw new RuntimeException("You don't have permission to view this review");
                }
            }
        }

        // Get all KPI criteria and scores
        List<KpiCriteria> allCriteria = kpiCriteriaRepository.findAllActive();
        List<KpiScore> empScores = kpiScoreRepository.findByEmpIdAndPeriodId(employee.getId(), periodId);
        Map<Integer, KpiScore> scoreMap = empScores.stream()
            .collect(Collectors.toMap(KpiScore::getKpiCriteriaId, score -> score));

        List<EmployeeReviewScoreDto> scoreDtos = new ArrayList<>();
        for (KpiCriteria criteria : allCriteria) {
            KpiScore score = scoreMap.get(criteria.getId());
            if (score != null) {
                scoreDtos.add(EmployeeReviewScoreDto.builder()
                    .criteriaId(criteria.getId())
                    .criteriaName(criteria.getName())
                    .weight(criteria.getWeight())
                    .scoreValue(score.getScoreValue())
                    .build());
            }
        }

        // Get department information
        Department department = null;
        if (employee.getDeptId() != null) {
            department = departmentRepository.findById(employee.getDeptId()).orElse(null);
        }

        return EmployeeReviewRecordDto.builder()
            .id(result.getId())
            .empId(employee.getId())
            .empName(employee.getFullName())
            .deptId(employee.getDeptId())
            .deptName(department != null ? department.getDeptName() : null)
            .scores(scoreDtos)
            .averageScore(result.getFinalScore())
            .finalRating(result.getRating().name())
            .comment(result.getComment())
            .build();
    }

    @Transactional
    public KpiResultResponse submitEmployeeReview(Integer periodId, EmployeeReviewSubmitDto submitDto) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Employee currentEmployee = employeeRepository.findByUserId(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));
        

        if (user.getRole() != UserRole.ADMIN) {
            if (currentEmployee.getRoleInDept() != RoleInDepartment.HEAD) {
                throw new RuntimeException("Only admin and department heads can submit employee reviews");
            }

            Employee targetEmployee = employeeRepository.findById(submitDto.getEmpId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + submitDto.getEmpId()));

            if (!targetEmployee.getDeptId().equals(currentEmployee.getDeptId())) {
                throw new RuntimeException("You can only review employees in your department");
            }
        }

        // Verify period exists
        KpiPeriod period = kpiPeriodRepository.findById(periodId)
            .orElseThrow(() -> new ResourceNotFoundException("KPI period not found with id: " + periodId));

        if (period.getIsDeleted()) {
            throw new ResourceNotFoundException("KPI period not found with id: " + periodId);
        }

        // Check if review already exists
        Optional<KpiResults> existingResult = kpiResultsRepository.findByEmpIdAndPeriodId(
            submitDto.getEmpId(),
            periodId
        );

        if (existingResult.isPresent()) {
            throw new RuntimeException("Review already exists for this employee in this period");
        }

        // Get all criteria and validate scores
        List<KpiCriteria> allCriteria = kpiCriteriaRepository.findAllActive();
        Map<Integer, KpiCriteria> criteriaMap = allCriteria.stream()
            .collect(Collectors.toMap(KpiCriteria::getId, c -> c));

        // Save individual scores
        for (KpiScoreSubmitDto scoreDto : submitDto.getScores()) {
            KpiCriteria criteria = criteriaMap.get(scoreDto.getCriteriaId());
            if (criteria == null) {
                throw new ResourceNotFoundException("KPI criteria not found with id: " + scoreDto.getCriteriaId());
            }

            KpiScore score = KpiScore.builder()
                .empId(submitDto.getEmpId())
                .kpiCriteriaId(scoreDto.getCriteriaId())
                .kpiPeriodId(periodId)
                .scoreValue(scoreDto.getScoreValue())
                .build();

            kpiScoreRepository.save(score);
        }

        // Calculate final score and rating
        BigDecimal totalWeightedScore = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (KpiScoreSubmitDto scoreDto : submitDto.getScores()) {
            KpiCriteria criteria = criteriaMap.get(scoreDto.getCriteriaId());
            if (criteria.getWeight() != null && scoreDto.getScoreValue() != null) {
                totalWeightedScore = totalWeightedScore.add(
                    scoreDto.getScoreValue().multiply(criteria.getWeight()).divide(
                        new BigDecimal("100"), 2, RoundingMode.HALF_UP
                    )
                );
                totalWeight = totalWeight.add(criteria.getWeight());
            }
        }

        BigDecimal finalScore = BigDecimal.ZERO;
        if (totalWeight.compareTo(BigDecimal.ZERO) > 0) {
            finalScore = totalWeightedScore.setScale(2, RoundingMode.HALF_UP);
        }

        RatingEmployeeType rating = calculateRating(finalScore);

        // Save KPI result
        KpiResults kpiResult = KpiResults.builder()
            .empId(submitDto.getEmpId())
            .kpiPeriodId(periodId)
            .finalScore(finalScore)
            .rating(rating)
            .recordedBy(currentEmployee.getId())
            .build();

        // Fix the builder comment setter
        if (submitDto.getComment() != null) {
            kpiResult.setComment(submitDto.getComment());
        }

        KpiResults savedResult = kpiResultsRepository.save(kpiResult);

        return mapToKpiResultResponse(savedResult);
    }

    private RatingEmployeeType calculateRating(BigDecimal finalScore) {
        if (finalScore.compareTo(new BigDecimal("4.5")) >= 0) {
            return RatingEmployeeType.A;
        } else if (finalScore.compareTo(new BigDecimal("4")) >= 0) {
            return RatingEmployeeType.B;
        } else if (finalScore.compareTo(new BigDecimal("3")) >= 0) {
            return RatingEmployeeType.C;
        } else if (finalScore.compareTo(new BigDecimal("2")) >= 0) {
            return RatingEmployeeType.D;
        } else if (finalScore.compareTo(new BigDecimal("1")) >= 0) {
            return RatingEmployeeType.E;
        } else {
            return RatingEmployeeType.F;
        }
    }

    private KpiResultResponse mapToKpiResultResponse(KpiResults result) {
        Employee employee = employeeRepository.findById(result.getEmpId()).orElse(null);
        KpiPeriod period = kpiPeriodRepository.findById(result.getKpiPeriodId()).orElse(null);
        Employee recordedBy = result.getRecordedBy() != null
            ? employeeRepository.findById(result.getRecordedBy()).orElse(null)
            : null;

        List<KpiScore> empScores = kpiScoreRepository.findByEmpIdAndPeriodId(result.getEmpId(), result.getKpiPeriodId());
        List<KpiCriteria> allCriteria = kpiCriteriaRepository.findAllActive();
        Map<Integer, KpiScore> scoreMap = empScores.stream()
            .collect(Collectors.toMap(KpiScore::getKpiCriteriaId, score -> score));

        List<EmployeeReviewScoreDto> scoreDtos = new ArrayList<>();
        for (KpiCriteria criteria : allCriteria) {
            KpiScore score = scoreMap.get(criteria.getId());
            if (score != null) {
                scoreDtos.add(EmployeeReviewScoreDto.builder()
                    .criteriaId(criteria.getId())
                    .criteriaName(criteria.getName())
                    .weight(criteria.getWeight())
                    .scoreValue(score.getScoreValue())
                    .build());
            }
        }

        return KpiResultResponse.builder()
            .id(result.getId())
            .empId(result.getEmpId())
            .empName(employee != null ? employee.getFullName() : null)
            .kpiPeriodId(result.getKpiPeriodId())
            .periodName(period != null ? period.getPeriodName() : null)
            .scores(scoreDtos)
            .finalScore(result.getFinalScore())
            .rating(result.getRating())
            .comment(result.getComment())
            .recordedBy(result.getRecordedBy())
            .recordedByName(recordedBy != null ? recordedBy.getFullName() : null)
            .createdAt(result.getCreatedAt())
            .updatedAt(result.getUpdatedAt())
            .build();
    }

    @Transactional(readOnly = true)
    public List<KpiCriteriaResponse> getAllKpiCriteria() {
        List<KpiCriteria> criteria = kpiCriteriaRepository.findAllActive();

        return criteria.stream()
            .map(this::mapToKpiCriteriaResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<KpiResultResponse> getCurrentUserReviews() {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        Employee currentEmployee = employeeRepository.findByUserId(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

        List<KpiResults> results = kpiResultsRepository.findByEmpId(currentEmployee.getId());

        return results.stream()
            .map(this::mapToKpiResultResponse)
            .collect(Collectors.toList());
    }

    private KpiCriteriaResponse mapToKpiCriteriaResponse(KpiCriteria criteria) {
        return KpiCriteriaResponse.builder()
            .id(criteria.getId())
            .name(criteria.getName())
            .description(criteria.getDescription())
            .weight(criteria.getWeight())
            .createdAt(criteria.getCreatedAt())
            .updatedAt(criteria.getUpdatedAt())
            .build();
    }

    private KpiPeriodResponse mapToKpiPeriodResponse(KpiPeriod period) {
        return KpiPeriodResponse.builder()
            .id(period.getId())
            .periodName(period.getPeriodName())
            .startDate(period.getStartDate())
            .endDate(period.getEndDate())
            .createdAt(period.getCreatedAt())
            .updatedAt(period.getUpdatedAt())
            .build();
    }
}
