package com.retailcloud.ems.controller;

import com.retailcloud.ems.payload.EmployeeDTO;
import com.retailcloud.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/addEmployee")
    public ResponseEntity<EmployeeDTO> createEmployee(
            @RequestBody EmployeeDTO dto
    ){
        EmployeeDTO created = employeeService.createEmployee(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeDTO dto
    ){
        EmployeeDTO updated = employeeService.updateEmployee(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping("/{empId}/department/{deptId}")
    public ResponseEntity<String> moveEmployeeDepartment(
            @PathVariable Long empId,
            @PathVariable Long deptId
    ){
        employeeService.moveToAnotherDepartment(empId, deptId);
        return new ResponseEntity<>("Employee moved to new department successfully!", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Boolean lookup
    ){
        if(lookup != null && lookup){
            return new ResponseEntity<>(employeeService.getEmployeeNameIdOnly(), HttpStatus.OK);
        }
        return new ResponseEntity<>(employeeService.getAllEmployeesPaginated(page, size), HttpStatus.OK);
    }
}
