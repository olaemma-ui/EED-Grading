package com.example.project.Service;

import com.example.project.Message.Response;
import com.example.project.Model.Scores;
import com.example.project.Model.Student;
import com.example.project.Repository.DepartmentRepo;
import com.example.project.Repository.ScoreRepo;
import com.example.project.Repository.StudentRepo;
import com.example.project.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class StudentService {
    private boolean success;
    private String responseCode;
    private String message;
    private Object error;
    private Object data;

    private final Utils utils;
    private final StudentRepo studentRepo;
    private final DepartmentRepo departmentRepo;
    private final ScoreRepo scoreRepo;

    @Autowired
    StudentService(Utils utils, StudentRepo studentRepo, DepartmentRepo departmentRepo, ScoreRepo scoreRepo){
        this.utils = utils;
        this.studentRepo = studentRepo;
        this.departmentRepo = departmentRepo;
        this.scoreRepo = scoreRepo;
    }

    public ResponseEntity<Response> addStudents(Student student){
        reset();
        try{
            Object[] validate = utils.validate(student, new String[]{"id", "scores", "department", "createdAt", "matric"});
            error = validate[1];
            data = student;
            if (Boolean.parseBoolean(validate[0].toString())){
                if (student.getLvl().equalsIgnoreCase("ND") || student.getLvl().equalsIgnoreCase("HND")){
                    message = "Invalid department!";
                    departmentRepo.findById(student.getDeptId()).ifPresent(
                        department -> {
                            student.setSession(department.getSession());

                            student.setDepartment(department);
                            student.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                            int stdCount = studentRepo.findAllBySessionAndDepartment(student.getLvl(), student.getSession().getId(), student.getDepartment().getId()).size();
                            for (int i = stdCount+1; i <= stdCount+student.getTotalStudent(); i++) {
                                List<Scores> scores = (student.getLvl().equalsIgnoreCase("ND"))
                                        ?Arrays.asList(setScore("EED 126", student), setScore("EED 216", student))
                                        :Arrays.asList(setScore("EED 324", student), setScore("EED 414", student));

                                student.setId(utils.genId(studentRepo.allId(), new int[]{3, 8}));
                                student.setMatric(i);
                                student.setScores(scores);
//                                scoreRepo.saveAll(scores);
                                studentRepo.save(student);
                            }
                            success(null);
                    });
                }else message = "Invalid level!";
            }else message = "Invalid fields!";
        }catch (Exception e){
            e.printStackTrace();
            message = "Something went wrong";
            responseCode = "500";
        }
        return new ResponseEntity<>(new Response(success, responseCode, message, error, data), HttpStatus.OK);
    }


    public ResponseEntity<Response> addSingleStudent(Map<String, String> studentData){
        reset();
        try{
//            Object[] validate = utils.validate(student, new String[]{"id", "scores", "department", "createdAt", "matric"});
//            error = validate[1];
//            data = student;
            if (studentData.containsKey("matric") && studentData.containsKey("level") && studentData.containsKey("departmentId")){
                if (studentData.get("level").equalsIgnoreCase("ND") || studentData.get("level").equalsIgnoreCase("HND")){
                    message = "Invalid department!";
                    departmentRepo.findById(studentData.get("departmentId")).ifPresent(
                            department -> {
                                if (Pattern.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})", studentData.get("matric"))) {

                                    if (studentRepo.findByMatric(studentData.get("matric")).isPresent()){
                                        Student student = new Student();
                                        student.setSession(department.getSession());
//
                                        student.setDepartment(department);
                                        student.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                                        student.setMatric(studentData.get("matric"));
                                        List<Scores> scores = (studentData.get("level").equalsIgnoreCase("ND"))
                                            ?Arrays.asList(setScore("EED 126", student), setScore("EED 216", student))
                                            :Arrays.asList(setScore("EED 324", student), setScore("EED 414", student));

                                        student.setId(utils.genId(studentRepo.allId(), new int[]{3, 8}));
                                        student.setScores(scores);
//                                        scoreRepo.saveAll(scores);
                                        studentRepo.save(student);
                                        success(student);
                                    }else message = "Matric already exist!";
                                }else message = "Invalid matric [00/00/0000]";
                            });
                }else message = "Invalid level!";
            }else message = "All fields are required!";
        }catch (Exception e){
            e.printStackTrace();
            message = "Something went wrong";
            responseCode = "500";
        }
        return new ResponseEntity<>(new Response(success, responseCode, message, error, data), HttpStatus.OK);
    }


    public ResponseEntity<Response> getAllStudent(Integer pageNo, Integer pageSize, String deptId, String level){
        reset();
        try{
            message = "Department is required!";
            Optional.ofNullable(deptId).ifPresent(
                    id->{
                        message = "Level is required!";
                        Optional.ofNullable(level).ifPresent(
                                lvl->{
                                    message = "Invalid department!";
                                    departmentRepo.findById(deptId).ifPresent(
                                            department->{
                                                Page<?> data = studentRepo.findAllBySessionAndDepartment(deptId, level, PageRequest.of(
                                                        Optional.ofNullable(pageNo).orElse(0),
                                                        Optional.ofNullable(pageSize).orElse(10),
                                                        Sort.by(Sort.Direction.ASC, "matric")
                                                ));
                                                success(new HashMap<String, Object>(){{
                                                    put("content", data.getContent());
                                                    put("pageNumber", data.getNumber());
                                                    put("totalPages", data.getTotalPages());
                                                    put("totalElements", data.getTotalElements());
                                                    put("last", data.isLast());
                                                }});
                                            }
                                    );
                                }
                        );
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
            message = "Something went wrong";
            responseCode = "500";
        }
        return new ResponseEntity<>(new Response(success, responseCode, message, error, data), HttpStatus.OK);
    }


    public ResponseEntity<Response> deleteStudent(String studentId){
        reset();
        try {
//            message = "Student does not exist!";
            studentRepo.findById(studentId).ifPresent(studentRepo::delete);
            success(null);
        }catch (Exception e){
            e.printStackTrace();
            message = "Something went wrong";
            responseCode = "500";
        }
        return new ResponseEntity<>(new Response(success, responseCode, message, error, data), HttpStatus.OK);
    }


    private Scores setScore(String course, Student student){
        return new Scores(
                utils.genId(scoreRepo.allId(), new int[]{3,3}),
                student.getMatric(),
                ""+course+"",
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                student
//                , student.getSession()
        );
    }

    private void success(Object data){
        this.success = true;
        this.responseCode = "100";
        this.error = null;
        this.data = data;
        this.message = "Success";
    }

    private void reset(){
        success = false;
        responseCode = "99";
        message = "Failed";
        error = null;
        data = null;
    }
}
