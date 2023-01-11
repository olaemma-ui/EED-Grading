package com.example.project.Controller;

import com.example.project.Message.Response;
import com.example.project.Model.Session;
import com.example.project.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/eed_grading/session/")
public class SessionController {

    @Autowired
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("add")
    private ResponseEntity<Response> addSession(@RequestBody Session session){
        return sessionService.addSession(session);
    }

    @DeleteMapping("delete")
    private ResponseEntity<Response> deleteSession(@RequestParam String session){
        return sessionService.deleteSession(session);
    }

    @GetMapping("list")
    private ResponseEntity<Response> getAllSession(@RequestParam Integer pageNo, @RequestParam Integer pageSize){
        return sessionService.getAllSession(pageNo, pageSize);
    }
}
