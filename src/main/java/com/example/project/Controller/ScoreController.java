package com.example.project.Controller;

import com.example.project.Message.Response;
import com.example.project.Model.Scores;
import com.example.project.Service.ScoreService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/eed_grading/score/")
public class ScoreController {
    private final ScoreService scoreService;
    @Autowired
    ScoreController(ScoreService scoreService){
        this.scoreService = scoreService;
    }

    @PostMapping("upload")
    private ResponseEntity<Response> uploadScore(@RequestBody JsonNode node){
        return scoreService.uploadSCore(node);
    }

    @PutMapping("update")
    private ResponseEntity<Response> uploadScore(@RequestBody Scores scores){
        return scoreService.updateScore(scores);
    }
}
