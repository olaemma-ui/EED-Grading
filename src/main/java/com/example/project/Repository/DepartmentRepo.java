package com.example.project.Repository;

import com.example.project.Model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, String> {

    @Query(value = "SELECT u FROM Department u WHERE u.session.id=:session AND (u.deptName=:deptName OR u.deptCode=:deptCode)")
    Optional<Department> findDepartment(String session, String deptName, String deptCode);

    @Query(value = "SELECT u.id FROM Department u")
    List<String> allId();

    @Query(value = "SELECT u FROM Department u WHERE u.session.id =:sessionId")
    Page<Department> findAllDeptBySession(String sessionId, Pageable pageable);

}
