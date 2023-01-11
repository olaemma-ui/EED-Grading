package com.example.project.Service;

import com.example.project.Message.Response;
import com.example.project.Model.Scores;
import com.example.project.Repository.DepartmentRepo;
import com.example.project.Repository.SessionRepo;
import com.example.project.Utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    @Autowired
    ScoreService(Utils utils, DepartmentRepo departmentRepo, SessionRepo sessionRepo, ObjectMapper mapper){
        this.utils = utils;
        this.departmentRepo = departmentRepo;
        this.sessionRepo = sessionRepo;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper = mapper;
    }

    public ResponseEntity<Response> uploadSCore(JsonNode data){
        reset();
        try{
            if (
                    data.has("level") &&
                    data.has("session") &&
                    data.has("department") &&
                    data.has("data")
            ){
                if (data.get("data").isArray()){
                    if (data.get("level").asText().equalsIgnoreCase("ND") || data.get("level").asText().equalsIgnoreCase("HND")){
                        if (data.get("level").asText().equalsIgnoreCase("ND")){
                            data.get("data").forEach(
                                    e->{
                                        Scores scores = mapper.convertValue(e, Scores.class);
                                        System.out.println("scores = "+scores.toString());

                                    }
                            );
                        }else{
                            data.get("data").forEach(
                                    e->{
                                        System.out.println("matric = "+e.get("matric")+" score = "+e.get("score"));
                                    }
                            );
                        }
                    }else message = "Invalid level!";
                }else message = "Invalid value for [data]!";
            }else message = "All fields required and valid!";
        }catch (Exception e){
            e.printStackTrace();
            message = "Something went wrong";
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
