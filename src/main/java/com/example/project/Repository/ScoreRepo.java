package com.example.project.Repository;

import com.example.project.Model.Scores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ScoreRepo extends JpaRepository<Scores, String> {

    @Query(value = "SELECT u.id FROM Scores u")
    List<String> allId();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Scores u SET u.ca=:ca, " +
            "u.practical=:practical, " +
            "u.exam=:exam, " +
            "u.total=:total, " +
            "u.grade=:grade " +
            "WHERE u.matric=:matric AND " +
            "u.id=:scoreId")
    int updateScore(double ca, double practical,
                    double exam, double total,
                    String grade, String matric,
                    String scoreId);
}
