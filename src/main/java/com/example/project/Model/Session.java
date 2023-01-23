package com.example.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter @Setter
public class Session {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id private String id;

    private String year;

    private String session;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp createdAt;

//    @ToString.Exclude
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Student> students;

//    @ToString.Exclude
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Department> departments;
}
