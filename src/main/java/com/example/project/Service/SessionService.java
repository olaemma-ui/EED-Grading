package com.example.project.Service;

import com.example.project.Message.Response;
import com.example.project.Model.Session;
import com.example.project.Repository.DepartmentRepo;
import com.example.project.Repository.ScoreRepo;
import com.example.project.Repository.SessionRepo;
import com.example.project.Repository.StudentRepo;
import com.example.project.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessionService {

    private boolean success;
    private String responseCode;
    private String message;
    private Object error;
    private Object data;


    private final Utils utils;
    private final SessionRepo sessionRepo;
    private final StudentRepo studentRepo;
    private final DepartmentRepo departmentRepo;
    private final ScoreRepo scoreRepo;

    @Autowired
    SessionService(Utils utils, SessionRepo sessionRepo, StudentRepo studentRepo, DepartmentRepo departmentRepo, ScoreRepo scoreRepo){
        this.utils = utils;
        this.sessionRepo = sessionRepo;
        this.studentRepo = studentRepo;
        this.departmentRepo = departmentRepo;
        this.scoreRepo = scoreRepo;
    }


    public ResponseEntity<Response> addSession(Session session){
        reset();
        try{
            Object[] validate = utils.validate(session, new String[]{"id", "session", "createdAt", "departments", "students"});
            error = validate[1];
            data = session;
            if (Boolean.parseBoolean(validate[0].toString())){
                if (!sessionRepo.findByYear(session.getYear()).isPresent()){
                    session.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    session.setId(utils.genId(sessionRepo.allId(), new int[]{3,3}));
                    session.setSession(session.getYear()+"/"+(Integer.parseInt(session.getYear())+1)+"");
                    sessionRepo.save(session);
                    success(session);
                }else message = "This session already exist";
            }else message = "Invalid fields!";
        }catch (Exception e){
            e.printStackTrace();
            message = "Something went wrong";
            responseCode = "500";
        }
        return new ResponseEntity<>(new Response(success, responseCode, message, error, data), HttpStatus.OK);
    }


    public ResponseEntity<Response> deleteSession(String sessionId){
        reset();
        try{
            message = "Session is required!";
            Optional.ofNullable(sessionId).ifPresent(
                id->{
                    message = "Invalid session!";
                    sessionRepo.findById(sessionId).ifPresent(
                            session->{
//                                scoreRepo.deleteBySessionOrStudent(sessionId);
//                                studentRepo.deleteBySessionOrDepartment(sessionId);
//                                departmentRepo.deleteBySession(sessionId);
                                sessionRepo.delete(session);
                                success(null);
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


    public ResponseEntity<Response> getAllSession(Integer pageNo, Integer pageSize){
        reset();
        try{
            success(
                sessionRepo.findAll(
                    PageRequest.of(
                        Optional.of(pageNo).orElse(0),
                        Optional.of(pageSize).orElse(10)
                    )
                )
            );
        }catch (Exception e){
            e.printStackTrace();
            message = "Something went wrong";
            responseCode = "500";
        }
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
