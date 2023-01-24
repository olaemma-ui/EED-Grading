package com.example.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Scores {
    @Id
    private String id;

    private String matric;

    private String course;

    private double ca;

    private double practical;

    private double exam;

    public void setTotal() {
        this.total = ca + practical + exam;
    }

    public void setGrade() {
        this.grade = (total < 20)
                ? "HF" :(total < 25) ? "PF"
                :(total < 30) ? "F" :(total < 35) ? "FF"
                :(total < 40) ? "EF" :(total < 45) ? "E"
                :(total < 50) ? "DE" :(total < 55) ? "D"
                :(total < 60) ? "CD" :(total < 65) ? "C"
                :(total < 70) ? "BC" :(total < 75) ? "B"
                :(total < 80) ? "AB" :(total <= 100) ? "A"
                :"--";
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double total;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String grade;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "studentId")
    @ManyToOne(cascade = CascadeType.DETACH)
    private Student student;

    @Override
    public String toString() {
        return "Scores{" +
                "id='" + id + '\'' +
                ", matric='" + matric + '\'' +
                ", course='" + course + '\'' +
                ", ca=" + ca +
                ", practical=" + practical +
                ", exam=" + exam +
                ", total=" + total +
                ", grade=" + grade +
                '}';
    }

    //    @ManyToOne
//    @JsonIgnore
//    private Session session;
}
