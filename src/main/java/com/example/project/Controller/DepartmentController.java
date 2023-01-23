package com.example.project.Controller;

import com.example.project.Message.Response;
import com.example.project.Model.Department;
import com.example.project.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/eed_grading/department/")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    DepartmentController(DepartmentService departmentService){
        this.departmentService = departmentService;
    }

    @PostMapping("add")
    private ResponseEntity<Response> addDept(@RequestBody Department department){
        return this.departmentService.addDepartment(department);
    }

    @DeleteMapping("delete")
    private ResponseEntity<Response> deleteDept(@RequestParam String deptId){
        return this.departmentService.deleteDepartment(deptId);
    }

    @GetMapping("list")
    private ResponseEntity<Response> getAllDept(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam String sessionId){
        return this.departmentService.getAllDepartment(pageNo, pageSize, sessionId);
    }

}
