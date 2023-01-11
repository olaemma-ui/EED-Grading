package com.example.project.Repository;

import com.example.project.Model.Student;
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
public interface StudentRepo extends JpaRepository<Student, String> {

    @Query(value = "SELECT u.id FROM Student u")
    List<String> allId();

    @Query("SELECT u FROM Student u WHERE u.matric=:matric")
    Optional<Student> findByMatric(String matric);

    @Transactional
    @Modifying
    @Query("DELETE FROM Student u WHERE u.session.id =:id OR u.department.id =:id")
    void deleteBySessionOrDepartment(String id);

    @Query("SELECT u FROM Student u WHERE u.department.id=:department AND u.session.id=:session AND u.lvl=:level")
    List<Student> findAllBySessionAndDepartment(String level, String session, String department);

    @Query("SELECT u FROM Student u WHERE u.department.id=:department AND u.lvl=:level")
    Page<Student> findAllBySessionAndDepartment(String department, String level, Pageable pageable);
}
