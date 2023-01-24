package com.example.project.Service;

import com.example.project.Message.Response;
import com.example.project.Model.Scores;
import com.example.project.Model.Student;
import com.example.project.Repository.DepartmentRepo;
import com.example.project.Repository.ScoreRepo;
import com.example.project.Repository.SessionRepo;
import com.example.project.Repository.StudentRepo;
import com.example.project.Utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ScoreService {
    private boolean success;
    private String responseCode;
    private String message;
    private Object error;
    private Object data;

    private final Utils utils;
    private final DepartmentRepo departmentRepo;
    private final SessionRepo sessionRepo;
    private final ObjectMapper mapper;
    private final ScoreRepo scoreRepo;
    private final StudentRepo studentRepo;
    private String msg = null;
    boolean failed = false;
    List<Scores> scoresList = new ArrayList<>();
    List<Student> studentScoreList = new ArrayList<>();

    @Autowired
    ScoreService(Utils utils, DepartmentRepo departmentRepo, SessionRepo sessionRepo, ObjectMapper mapper, ScoreRepo scoreRepo, StudentRepo studentRepo){
        this.utils = utils;
        this.departmentRepo = departmentRepo;
        this.sessionRepo = sessionRepo;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper = mapper;
        this.scoreRepo = scoreRepo;
        this.studentRepo = studentRepo;
    }

    public ResponseEntity<Response> uploadSCore(JsonNode data){
        scoresList = new ArrayList<>();
        List<HashMap<String, String>> errorList = new ArrayList<>();

        reset();
        failed = false;
        msg = null;
        try{
            if (
                    data.has("level") &&
                    data.has("sessionId") &&
                    data.has("course") &&
                    data.has("departmentId") &&
                    data.has("data")
            ){
                error = errorList;
                message = "Invalid sessionId!";
                sessionRepo.findById(data.get("sessionId").asText()).ifPresent(
                        session -> {
                            message = "Invalid departmentId";
                            departmentRepo.findById(data.get("departmentId").asText()).ifPresent(
                                    department -> {
                                        if (data.get("data").isArray()){
                                            if (data.get("level").asText().equalsIgnoreCase("ND") || data.get("level").asText().equalsIgnoreCase("HND")){

                                                boolean process =
                                                        (data.get("level").asText().equalsIgnoreCase("ND")
                                                                && (data.get("course").asText().equalsIgnoreCase("EED 126") || data.get("course").asText().equalsIgnoreCase("EED 216")))
                                                                || (data.get("level").asText().equalsIgnoreCase("HND")
                                                                && (data.get("course").asText().equalsIgnoreCase("EED 324") || data.get("course").asText().equalsIgnoreCase("EED 414")));
                                                if (process){
                                                    data.get("data").forEach(
                                                            e->{
                                                                msg = null;
                                                                Scores scores = mapper.convertValue(e, Scores.class);
                                                                scores.setCourse(data.get("course").asText().toUpperCase());
                                                                Optional<Student> student = studentRepo.findByMatric(scores.getMatric());
                                                                if (student.isPresent()){
                                                                    if (student.get().getSession().getId().equalsIgnoreCase(data.get("sessionId").asText())){

                                                                        if (student.get().getLvl().equalsIgnoreCase(data.get("level").asText())){

                                                                            if(student.get().getDepartment().getId().equalsIgnoreCase(data.get("departmentId").asText())){
                                                                                scores.setStudent(student.get());
                                                                                student.get().getScores().forEach(
                                                                                        scr->{
                                                                                            if(scr.getCourse().equalsIgnoreCase(scores.getCourse())){

                                                                                                scores.setId(scr.getId());
                                                                                                scores.setTotal();
                                                                                                scores.setGrade();
                                                                                                scores.setMatric(student.get().getMatric());
                                                                                                scores.setCa(scr.getCa() > 0 ? scr.getCa() : scores.getCa());
                                                                                                scores.setExam(scr.getExam() > 0 ? scr.getExam() : scores.getExam());
                                                                                                scores.setPractical(scr.getPractical() > 0 ? scr.getPractical() : scores.getPractical());

                                                                                               if (scores.getTotal() > 100) {
                                                                                                   msg = "Total score can not be greater than 100";
                                                                                                   failed = true;
                                                                                               }
                                                                                               if (scores.getCa() < 0 || scores.getExam() < 0 || scores.getPractical() < 0){
                                                                                                   failed = true;
                                                                                                   msg = "CA, Exam, Practical score can not be less than 0!";
                                                                                               }
                                                                                            }
                                                                                            scoresList.add(scores);
                                                                                            message = errorList.isEmpty() ? "SUCCESS" : "Some records are invalid!";
                                                                                        }
                                                                                );
                                                                            }else {
                                                                                msg = "Student not in selected department!";
                                                                                failed = false;
                                                                            }
                                                                        }else {
                                                                            msg = "Matric not in selected level!";
                                                                            failed = false;
                                                                        }
                                                                    }else {
                                                                        msg = "Matric not in the selected session!";
                                                                        failed = false;
                                                                    }
                                                                }else {
                                                                    msg = "Invalid matric!";
                                                                    failed = false;
                                                                }
                                                                if (!failed){
                                                                    scoreRepo.saveAll(scoresList);
                                                                    success(data);
                                                                }else message = "All data mus be valid before processing";
                                                                errorList.add(new HashMap<String, String>(){{
                                                                    put("matric", scores.getMatric());
                                                                    put("message", msg);
                                                                }});
                                                            }
                                                    );
                                                }
                                                else {
                                                    failed = true;
                                                    message = "The selected course does not match with the level. " +
                                                            "Note: course can only be any of these [EED 126, EED 216, EED 324, EED 414]";
                                                }
                                            }else {
                                                failed = true;
                                                message = "Invalid level!";
                                            }
                                        }else {
                                            failed = true;
                                            message = "Invalid value for data, expects ";
                                        }
                                    }
                            );
                        }
                );
            }
            else message = "All fields required and valid!";
        }catch (Exception e){
            e.printStackTrace();
            responseCode = "500";
            message = "Something went wrong";
        }
        return new ResponseEntity<>(new Response(success, responseCode, message, error, data), HttpStatus.OK);
    }


    public ResponseEntity<Response> updateScore(Scores scores){
        reset();

        data = scores;
        Object[] validate = utils.validate(scores, new String[]{"student", "grade", "total"});
        error = validate[1];

        if (Boolean.parseBoolean(validate[0].toString())){
            double total = scores.getExam() + scores.getCa() + scores.getPractical();
            if (total <= 100 || total >= 0){
                try{
                    Optional<Scores> optionalScores = scoreRepo.findById(scores.getId());
                    message = "Invalid score for student";
                    optionalScores.ifPresent(
                            score->{
                                if (score.getCourse().equalsIgnoreCase(scores.getCourse())){
                                    score.setPractical(scores.getPractical());
                                    score.setExam(scores.getExam());
                                    score.setCa(scores.getCa());
                                    score.setTotal();
                                    score.setGrade();

                                    scoreRepo.save(score);
                                    success(score);
                                }else message = "Course not matching with ID";
                            }
                    );
                }catch (Exception e){
                    e.printStackTrace();
                    responseCode = "500";
                    message = "Something went wrong!";
                }
            }else message = "Total score can only be from [0-100]";
        }else message = "Invalid/required fields!";

        return new ResponseEntity<>(new Response(success, responseCode, message, error, data), HttpStatus.OK);
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
