package com.example.employee_management.repo;


import com.example.employee_management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByDepartment_Id(Long departmentId, Pageable pageable);

    boolean existsByEmail(String email);
}
