package com.example.project.Repository;

import com.example.project.Model.Scores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScoreRepo extends JpaRepository<Scores, String> {

    @Query(value = "SELECT u.id FROM Scores u")
    List<String> allId();
}
