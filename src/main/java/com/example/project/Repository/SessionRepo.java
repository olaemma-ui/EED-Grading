package com.example.project.Repository;

import com.example.project.Model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session, String> {

    @Query(value = "SELECT u FROM Session u WHERE u.year=:year")
    Optional<Session> findByYear(String year);

    @Query(value = "SELECT u.id FROM Session u")
    List<String> allId();
}
