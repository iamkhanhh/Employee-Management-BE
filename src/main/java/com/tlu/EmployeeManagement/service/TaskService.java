package com.tlu.EmployeeManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tlu.EmployeeManagement.dto.request.TaskAssignDto;
import com.tlu.EmployeeManagement.dto.request.TaskCreateDto;
import com.tlu.EmployeeManagement.util.SecurityUtils;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.entity.Task;
import com.tlu.EmployeeManagement.entity.TaskAssignment;
import com.tlu.EmployeeManagement.entity.Department;
import com.tlu.EmployeeManagement.dto.response.TaskResponse;
import com.tlu.EmployeeManagement.dto.response.TaskAssignmentResponse;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;
import com.tlu.EmployeeManagement.enums.TaskStatus;
import com.tlu.EmployeeManagement.exception.ResourceNotFoundException;
import com.tlu.EmployeeManagement.exception.ValidationException;
import com.tlu.EmployeeManagement.repository.TaskAssignmentRepository;
import com.tlu.EmployeeManagement.repository.TaskRepository;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;    
import com.tlu.EmployeeManagement.repository.DepartmentRepository;
import java.time.LocalDate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
        private final TaskRepository taskRepository;
        private final TaskAssignmentRepository taskAssignmentRepository;
        private final EmployeeRepository employeeRepository;
        private final DepartmentRepository departmentRepository;

        public TaskResponse createTask(TaskCreateDto dto) {
                Integer currentUserId = SecurityUtils.getCurrentUserId();
                if (currentUserId == null) throw new RuntimeException("Unauthenticated");

                Employee emp = employeeRepository.findByUserId(currentUserId)
                        .orElseThrow(() -> new RuntimeException("Employee not found for current user"));

                Department dept = departmentRepository.findById(emp.getDeptId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));

                var headOpt = employeeRepository.findFirstByDeptIdAndRoleInDept(dept.getId(), RoleInDepartment.HEAD);
                if (headOpt.isEmpty() || !headOpt.get().getId().equals(emp.getId())) {
                        throw new RuntimeException("Forbidden: only department head can create tasks");
                }

                Task task = new Task();
                task.setTitle(dto.getTitle());
                task.setDescription(dto.getDescription());
                task.setDueDate(dto.getDueDate());
                task.setCreatedBy(emp.getId());
                task.setStatus(TaskStatus.PENDING);
                Task savedTask = taskRepository.save(task);
                if (dto.getEmployeeIds() != null && !dto.getEmployeeIds().isEmpty()) {
                        for (Integer empId : dto.getEmployeeIds()) {
                        Employee e = employeeRepository.findById(empId)
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + empId));
                        if (!e.getDeptId().equals(emp.getDeptId())) {
                                throw new ValidationException("Employee " + empId + " is not in this department");
                        }
                        taskAssignmentRepository.findByTaskIdAndEmpId(savedTask.getId(), empId)
                                .ifPresent(a -> { throw new ValidationException("Employee " + empId + " already assigned"); });
                        TaskAssignment assignment = new TaskAssignment();
                        assignment.setTaskId(savedTask.getId());
                        assignment.setEmpId(empId);
                        assignment.setAssignedDate(LocalDateTime.now());
                        assignment.setCompletedDate(null);
                        taskAssignmentRepository.save(assignment);
                        }
                }
                return toTaskResponse(savedTask);
        }


        @Transactional(readOnly = true)
        public List<TaskResponse> getTasksForCurrentUser(Integer userId, LocalDate startDate, LocalDate endDate, TaskStatus status) {
                if (userId == null) throw new RuntimeException("Unauthenticated");

                Employee emp = employeeRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Employee not found for current user"));

                List<Task> tasks;
                if (emp.getRoleInDept() == RoleInDepartment.HEAD) {
                        tasks = taskRepository.findByCreatedBy(emp.getId());
                } 
                else {
                        tasks = taskAssignmentRepository.findActiveAssignmentsByEmpId(emp.getId()).stream()
                                .map(a -> taskRepository.findById(a.getTaskId()).orElse(null))
                                .filter(t -> t != null)
                                .collect(Collectors.toList());
                }

                if (startDate != null && endDate == null) {
                endDate = LocalDate.now();
                }

                if (startDate != null) {
                LocalDate finalEndDate = endDate;
                tasks = tasks.stream()
                        .filter(t -> {
                                if (t.getCreatedAt() == null) return false;

                                LocalDate created = t.getCreatedAt().toLocalDate(); 

                                boolean afterStart = !created.isBefore(startDate); 
                                boolean beforeEnd = finalEndDate == null || !created.isAfter(finalEndDate); 

                                return afterStart && beforeEnd;
                        })
                        .collect(Collectors.toList());
                }
                if (status != null) {
                        tasks = tasks.stream()
                                .filter(t -> t.getStatus() == status)
                                .collect(Collectors.toList());
                }
                return tasks.stream()
                        .map(this::toTaskResponse)
                        .collect(Collectors.toList());
        }


        @Transactional
        public TaskResponse updateTaskStatus(Integer taskId, TaskStatus newStatus, Integer userId) {
                if (userId == null) throw new RuntimeException("Unauthenticated");
                Employee emp = employeeRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Employee not found for current user"));

                Task task = taskRepository.findById(taskId)
                                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
                if (emp.getRoleInDept() == RoleInDepartment.HEAD) {
                        if (!task.getCreatedBy().equals(emp.getId())) throw new ValidationException("Forbidden: not task owner");
                        task.setStatus(newStatus);
                        taskRepository.save(task);
                        return toTaskResponse(task);
                }
                boolean assigned = taskAssignmentRepository.findByTaskIdAndEmpId(taskId, emp.getId()).isPresent();
                if (!assigned) throw new ValidationException("Forbidden: not assigned to this task");
                if (newStatus == TaskStatus.COMPLETED) {
                        TaskAssignment assignment = taskAssignmentRepository.findByTaskIdAndEmpId(taskId, emp.getId())
                                        .orElseThrow(() -> new ValidationException("Assignment not found"));
                        assignment.setCompletedDate(LocalDateTime.now());
                        taskAssignmentRepository.save(assignment);
                        long total = taskAssignmentRepository.findByTaskId(taskId).size();
                        long completed = taskAssignmentRepository.countCompletedByTaskId(taskId);
                        if (total > 0 && completed == total) {
                                task.setStatus(TaskStatus.COMPLETED);
                                taskRepository.save(task);
                        }

                        return toTaskResponse(task);
                }


                if (newStatus == TaskStatus.IN_PROGRESS) {
                        task.setStatus(TaskStatus.IN_PROGRESS);
                        taskRepository.save(task);
                        return toTaskResponse(task);
                }

                throw new ValidationException("Forbidden status change");
        }

        @Transactional
        public void deleteTask(Integer taskId, Integer userId) {
                if (userId == null) throw new RuntimeException("Unauthenticated");
                Employee emp = employeeRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Employee not found for current user"));

                Task task = taskRepository.findById(taskId)
                                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

                if (emp.getRoleInDept() != RoleInDepartment.HEAD || !task.getCreatedBy().equals(emp.getId())) {
                        throw new ValidationException("Forbidden: only the head who created the task can delete it");
                }

                
                try {
                        taskRepository.deleteById(taskId);
                } catch (Exception ex) {
                        throw new RuntimeException("Failed to delete task: " + ex.getMessage());
                }
        }
        private TaskResponse toTaskResponse(Task task) {
                List<TaskAssignmentResponse> assignments = taskAssignmentRepository.findByTaskId(task.getId()).stream()
                                .map(a -> {
                                        Employee emp = employeeRepository.findById(a.getEmpId()).orElse(null);
                                        String empName = (emp != null) ? emp.getFullName() : "Unknown";
                                        return new TaskAssignmentResponse(a.getId(), a.getEmpId(), empName,
                                                        a.getAssignedDate(), a.getCompletedDate());
                                })
                                .collect(Collectors.toList());

              
                Employee creator = employeeRepository.findById(task.getCreatedBy()).orElse(null);
                String creatorName = (creator != null) ? creator.getFullName() : "Unknown";

                
                return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
                                task.getCreatedBy(), creatorName, task.getStatus().name(), assignments, task.getCreatedAt());
        }
}