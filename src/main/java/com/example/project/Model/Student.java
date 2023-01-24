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
public class Student {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String matric;

    public void setMatric(int no) {
        String year = String.valueOf(this.getDepartment().getSession().getYear());
        year = year.substring(year.length()-2);
        String num = (no < 10) ? "000"+no :(no < 100) ?"00"+no :(no < 1000) ? "0"+no : ""+no;
        this.matric = year+"/"+this.department.getDeptCode()+"/"+num;
    }

    public void setMatric(String matric){
        this.matric = matric;
    }
    @Transient
    private String deptId;

    private String lvl;

    public String getDeptId() {
        return (department != null) ? department.getId() : deptId;
    }

    @Transient
    private Integer totalStudent;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp createdAt;

    @JoinColumn(name = "deptId")
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH})
    private Department department;

    @OneToMany(
            mappedBy = "student",
            cascade = {CascadeType.ALL},
            orphanRemoval = true)
    private List<Scores> scores;

    @ManyToOne
    @JsonIgnore
    private Session session;

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", matric='" + matric + '\'' +
                ", deptId='" + deptId + '\'' +
                ", lvl='" + lvl + '\'' +
                ", totalStudent=" + totalStudent +
                ", createdAt=" + createdAt +
                ", department=" + department +
                ", scores=" + scores +
                '}';
    }
}
