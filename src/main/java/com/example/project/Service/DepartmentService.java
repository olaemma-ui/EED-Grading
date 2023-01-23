package com.example.project.Service;

import com.example.project.Message.Response;
import com.example.project.Model.Department;
import com.example.project.Repository.DepartmentRepo;
import com.example.project.Repository.SessionRepo;
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
public class DepartmentService {

    private boolean success;
    private String responseCode;
    private String message;
    private Object error;
    private Object data;

    private final Utils utils;
    private final DepartmentRepo departmentRepo;
    private final SessionRepo sessionRepo;

    @Autowired
    DepartmentService(Utils utils, DepartmentRepo departmentRepo, SessionRepo sessionRepo){
        this.utils = utils;
        this.departmentRepo = departmentRepo;
        this.sessionRepo = sessionRepo;
    }


    public ResponseEntity<Response> addDepartment(Department department){
        reset();

        try{
            Object[] validate = utils.validate(department, new String[]{"id", "session", "createdAt", "students"});
            error = validate[1];
            data = department;
            if (Boolean.parseBoolean(validate[0].toString())){
                message = "Invalid session!";
                sessionRepo.findById(department.getSessionId()).ifPresent(
                        session -> {
                            if (!departmentRepo.findDepartment(department.getSessionId(), department.getDeptName(), department.getDeptCode()).isPresent()){
                                department.setDeptCode(
                                        (department.getDeptCode().length() < 2 )
                                                ? "0"+department.getDeptCode()
                                                : department.getDeptCode()
                                );
                                department.setId(utils.genId(departmentRepo.allId(), new int[]{3,5}));
                                department.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                                department.setSession(session);
                                departmentRepo.save(department);
                                success(department);
                            }else message = "Department code or name already exist";
                        }
                );
            }else message = "Invalid fields!";
        }catch (Exception e){
            e.printStackTrace();
            message = "Something went wrong";
            responseCode = "500";
        }

        return new ResponseEntity<>(new Response(success, responseCode, message, error, data), HttpStatus.OK);
    }


    public ResponseEntity<Response> deleteDepartment(String deptId){
        reset();
        try{
            message = "Department is required!";
            Optional.ofNullable(deptId).ifPresent(
                    id->{
                        message = "Invalid Department!";
                        departmentRepo.findById(id).ifPresent(
                                department->{
                                    departmentRepo.delete(department);
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


    public ResponseEntity<Response> getAllDepartment(Integer pageNo, Integer pageSize, String sessionId){
        reset();
        try{
            success(departmentRepo.findAllDeptBySession(
                sessionId,
                PageRequest.of(
                    Optional.of(pageNo).orElse(0),
                    Optional.of(pageSize).orElse(10)
            )));
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
