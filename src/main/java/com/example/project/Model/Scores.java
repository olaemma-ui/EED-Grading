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

//    @ManyToOne
//    @JsonIgnore
//    private Session session;
}
