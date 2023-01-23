package com.example.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter @Setter
public class Department {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id private String id;

    private String deptName;

    private String deptCode;

    @Transient
    private String sessionId;

    public String getSessionId() {
        return this.session.getId();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp createdAt;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "sessionId")
    @JsonIgnore
    private Session session;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Student> students;
}
