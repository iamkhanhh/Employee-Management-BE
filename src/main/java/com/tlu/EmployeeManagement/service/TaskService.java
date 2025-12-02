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
                Task saved = taskRepository.save(task);
                return toTaskResponse(saved);
        }

        public TaskResponse assignTask(Integer taskId, TaskAssignDto dto) {
                Task task = taskRepository.findById(taskId)
                                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

                Employee employee = employeeRepository.findById(dto.getEmployeeId())
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

                Employee creator = employeeRepository.findById(task.getCreatedBy())
                                .orElseThrow(() -> new ResourceNotFoundException("Task creator employee not found"));

                if (creator.getDeptId() == null || !creator.getDeptId().equals(employee.getDeptId())) {
                        throw new ValidationException("Employee must be in the same department as task creator");
                }
                taskAssignmentRepository.findByTaskIdAndEmpId(taskId, dto.getEmployeeId())
                                .ifPresent(a -> { throw new ValidationException("Employee already assigned to this task"); });

                TaskAssignment assignment = new TaskAssignment();
                assignment.setTaskId(taskId);
                assignment.setEmpId(dto.getEmployeeId());
                assignment.setAssignedDate(LocalDateTime.now());
                assignment.setCompletedDate(null);
                taskAssignmentRepository.save(assignment);
        
                return toTaskResponse(task);
        }

        @Transactional(readOnly = true)
        public List<TaskResponse> getTasksForCurrentUser(Integer userId) {
                if (userId == null) throw new RuntimeException("Unauthenticated");
                Employee emp = employeeRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Employee not found for current user"));

                if (emp.getRoleInDept() == RoleInDepartment.HEAD) {
                        return taskRepository.findByCreatedBy(emp.getId()).stream().map(this::toTaskResponse).collect(Collectors.toList());
                } else {
                        return taskAssignmentRepository.findActiveAssignmentsByEmpId(emp.getId()).stream()
                                        .map(a -> taskRepository.findById(a.getTaskId()).orElse(null))
                                        .filter(t -> t != null)
                                        .map(this::toTaskResponse)
                                        .collect(Collectors.toList());
                }
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