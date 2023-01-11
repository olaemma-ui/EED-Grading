package com.example.project.Controller;

import com.example.project.Message.Response;
import com.example.project.Model.Student;
import com.example.project.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/eed_grading/student/")
public class StudentController {
    private final StudentService studentService;
    @Autowired
    StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @PostMapping("add")
    private ResponseEntity<Response> addStudent(@RequestBody Student student){
        return studentService.addStudents(student);
    }

    @PostMapping("single/add")
    public ResponseEntity<Response> addSingleStudent(Map<String, String> studentData){
        return studentService.addSingleStudent(studentData);
    }

    @GetMapping("list")
    private ResponseEntity<Response> getAllStudent(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam String deptId, @RequestParam String level){
        return studentService.getAllStudent(pageNo, pageSize, deptId, level);
    }

    @DeleteMapping("delete")
    private ResponseEntity<Response> deleteStudent(@RequestParam String studentId){
        return studentService.deleteStudent(studentId);
    }
}
