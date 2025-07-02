package com.retailcloud.ems.controller;

import com.retailcloud.ems.payload.DepartmentDTO;
import com.retailcloud.ems.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/create")
    public ResponseEntity<DepartmentDTO> createDepartment(
            @RequestBody DepartmentDTO departmentDTO
    ){
        DepartmentDTO created = departmentService.createDepartment(departmentDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentDTO dto
    ){
        DepartmentDTO updated = departmentService.updateDepartment(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDepartment(
            @PathVariable Long id
    ){
        boolean deleted = departmentService.deleteDepartment(id);
        if(deleted){
            return new ResponseEntity<>("Department Deleted Successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot delete department: Employees are still assigned.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllDept")
    public ResponseEntity<?> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        return new ResponseEntity<>(departmentService.getAllDepartmentsPaginated(page,size), HttpStatus.OK);
    }

    @GetMapping("/empList/{id}")
    public ResponseEntity<?> getDepartmentById(
            @PathVariable Long id,
            @RequestParam(required = false) String expand
    ){
        if("employee".equalsIgnoreCase(expand)){
            return new ResponseEntity<>(departmentService.getAllDepartmentsWithEmployees(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(departmentService.getDepartmentOnly(id), HttpStatus.OK);
    }
}
