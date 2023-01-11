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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id private String id;

    @Transient
    private String matric;

    private String course;

    private double ca;

    private double practical;

    private double exam;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double total;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double grade;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "studentId")
    @ManyToOne(cascade = CascadeType.ALL)
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
                ", student=" + student +
                '}';
    }

    //    @ManyToOne
//    @JsonIgnore
//    private Session session;
}
