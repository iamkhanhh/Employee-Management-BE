package com.tlu.EmployeeManagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tlu.EmployeeManagement.dto.request.NotificationCreateDto;
import com.tlu.EmployeeManagement.dto.request.NotificationFilterDto;
import com.tlu.EmployeeManagement.dto.request.NotificationUpdateDto;
import com.tlu.EmployeeManagement.dto.response.NotificationResponse;
import com.tlu.EmployeeManagement.dto.response.PagedResponse;
import com.tlu.EmployeeManagement.entity.Department;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.entity.Notification;
import com.tlu.EmployeeManagement.entity.User;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;
import com.tlu.EmployeeManagement.enums.UserRole;
import com.tlu.EmployeeManagement.repository.DepartmentRepository;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;
import com.tlu.EmployeeManagement.repository.NotificationRepository;
import com.tlu.EmployeeManagement.repository.UserRepository;
import com.tlu.EmployeeManagement.specification.NotificationSpecification;
import com.tlu.EmployeeManagement.util.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {

    NotificationRepository notificationRepository;
    EmployeeRepository employeeRepository;
    UserRepository userRepository;
    DepartmentRepository departmentRepository;

    public PagedResponse<NotificationResponse> getNotifications(NotificationFilterDto filterDto) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Specification<Notification> spec = NotificationSpecification.filterNotifications(
            filterDto.getDeptId(),
            filterDto.getSearch()
        );

        if (currentUser.getRole() == UserRole.ADMIN) {
            if (filterDto.getDeptId() != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("deptId"), filterDto.getDeptId()));
            }
        } else {
            Employee employee = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

            Integer userDeptId = employee.getDeptId();

            spec = spec.and((root, query, cb) ->
                cb.or(
                    cb.equal(root.get("deptId"), userDeptId),
                    cb.isNull(root.get("deptId"))
                )
            );
        }

        Pageable pageable = PageRequest.of(
            filterDto.getPage(),
            filterDto.getPageSize(),
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Notification> notificationPage = notificationRepository.findAll(spec, pageable);

        List<NotificationResponse> notificationResponses = notificationPage.getContent().stream()
            .map(this::toNotificationResponse)
            .collect(Collectors.toList());

        return PagedResponse.<NotificationResponse>builder()
            .content(notificationResponses)
            .currentPage(notificationPage.getNumber())
            .pageSize(notificationPage.getSize())
            .totalElements(notificationPage.getTotalElements())
            .totalPages(notificationPage.getTotalPages())
            .hasNext(notificationPage.hasNext())
            .hasPrevious(notificationPage.hasPrevious())
            .build();
    }

    public List<NotificationResponse> viewNotifications(Integer offset) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(offset / 10, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Notification> notifications;

        if (currentUser.getRole() == UserRole.ADMIN) {
            notifications = notificationRepository.findTopNotifications(pageable);
        } else {
            Employee employee = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

            Specification<Notification> spec = (root, query, cb) ->
                cb.and(
                    cb.equal(root.get("isDeleted"), false),
                    cb.or(
                        cb.equal(root.get("deptId"), employee.getDeptId()),
                        cb.isNull(root.get("deptId"))
                    )
                );

            notifications = notificationRepository.findAll(spec, pageable).getContent();
        }

        return notifications.stream()
            .map(this::toNotificationResponse)
            .collect(Collectors.toList());
    }

    public NotificationResponse getNotificationById(Integer id) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        if (notification.getIsDeleted()) {
            throw new RuntimeException("Notification has been deleted");
        }

        User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getRole() != UserRole.ADMIN) {
            Employee employee = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

            if (notification.getDeptId() != null && !notification.getDeptId().equals(employee.getDeptId())) {
                throw new RuntimeException("You don't have permission to view this notification");
            }
        }

        return toNotificationResponse(notification);
    }

    public NotificationResponse createNotification(NotificationCreateDto createDto) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getRole() != UserRole.ADMIN) {
            Employee employee = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

            if (employee.getRoleInDept() != RoleInDepartment.HEAD) {
                throw new RuntimeException("Only admin or department head can create notifications");
            }

            if (createDto.getDeptId() == null) {
                throw new RuntimeException("Department heads must specify a department ID");
            }

            if (!employee.getDeptId().equals(createDto.getDeptId())) {
                throw new RuntimeException("You can only create notifications for your department");
            }
        }

        // Validate department exists if deptId is provided
        if (createDto.getDeptId() != null) {
            departmentRepository.findById(createDto.getDeptId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + createDto.getDeptId()));
        }

        Notification notification = Notification.builder()
            .title(createDto.getTitle())
            .content(createDto.getContent())
            .deptId(createDto.getDeptId())
            .createdBy(currentUserId)
            .build();
        notification.setIsDeleted(false);

        Notification savedNotification = notificationRepository.save(notification);
        return toNotificationResponse(savedNotification);
    }

    public NotificationResponse updateNotification(Integer id, NotificationUpdateDto updateDto) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        if (notification.getIsDeleted()) {
            throw new RuntimeException("Cannot update deleted notification");
        }

        User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getRole() != UserRole.ADMIN) {
            Employee employee = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

            if (employee.getRoleInDept() != RoleInDepartment.HEAD) {
                throw new RuntimeException("Only admin or department head can update notifications");
            }

            // Head can only update notifications for their department (not global notifications)
            if (notification.getDeptId() == null || !employee.getDeptId().equals(notification.getDeptId())) {
                throw new RuntimeException("You can only update notifications for your department");
            }
        }

        if (updateDto.getTitle() != null) {
            notification.setTitle(updateDto.getTitle());
        }
        if (updateDto.getContent() != null) {
            notification.setContent(updateDto.getContent());
        }

        Notification updatedNotification = notificationRepository.save(notification);
        return toNotificationResponse(updatedNotification);
    }

    public void deleteNotification(Integer id) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getRole() != UserRole.ADMIN) {
            Employee employee = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

            if (employee.getRoleInDept() != RoleInDepartment.HEAD) {
                throw new RuntimeException("Only admin or department head can delete notifications");
            }
            if (notification.getDeptId() == null || !employee.getDeptId().equals(notification.getDeptId())) {
                throw new RuntimeException("You can only delete notifications for your department");
            }
        }

        notification.setIsDeleted(true);
        notificationRepository.save(notification);
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        String departmentName = null;
        if (notification.getDeptId() != null) {
            departmentName = departmentRepository.findById(notification.getDeptId())
                .map(Department::getDeptName)
                .orElse(null);
        } else {
            departmentName = "All Departments";
        }

        String createdByName = null;
        if (notification.getCreatedBy() != null) {
            createdByName = employeeRepository.findByUserId(notification.getCreatedBy())
                .map(Employee::getFullName)
                .orElse(null);
        }

        return NotificationResponse.builder()
            .id(notification.getId())
            .title(notification.getTitle())
            .content(notification.getContent())
            .departmentName(departmentName)
            .createdByName(createdByName)
            .createdAt(notification.getCreatedAt())
            .updatedAt(notification.getUpdatedAt())
            .build();
    }
}
