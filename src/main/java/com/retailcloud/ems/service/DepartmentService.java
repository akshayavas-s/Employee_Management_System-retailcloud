package com.retailcloud.ems.service;

import com.retailcloud.ems.entity.Department;
import com.retailcloud.ems.entity.Employee;
import com.retailcloud.ems.exceptions.ResourceNotFoundException;
import com.retailcloud.ems.payload.DepartmentDTO;
import com.retailcloud.ems.payload.EmployeeDTO;
import com.retailcloud.ems.repository.DepartmentRepository;
import com.retailcloud.ems.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    //Create Dept
    public DepartmentDTO createDepartment(
            DepartmentDTO departmentDTO
    ) {
        Department dept = convertDtoToEntity(departmentDTO);

        if (departmentDTO.getDepartmentHeadId()!=null){
            Employee head = employeeRepository.findById(departmentDTO.getDepartmentHeadId())
                    .orElseThrow(()-> new ResourceNotFoundException("Manager not found with ID: " + departmentDTO.getDepartmentHeadId()));
            dept.setDepartmentHead(head);
        }

        Department saved = departmentRepository.save(dept);
        return convertEntityToDto(saved);
    }


    //Update Dept
    public DepartmentDTO updateDepartment(
            Long id, DepartmentDTO departmentDTO
    ){
        Department dept = departmentRepository.findById(id)
                        .orElseThrow(()->new ResourceNotFoundException("Department not found with ID: " + id));

            dept.setName(departmentDTO.getName());
            dept.setCreationDate(departmentDTO.getCreationDate());

            if(departmentDTO.getDepartmentHeadId()!=null){
                Employee head = employeeRepository.findById(departmentDTO.getDepartmentHeadId())
                        .orElseThrow(()->new ResourceNotFoundException("Manager not Found with the ID: " + departmentDTO.getDepartmentHeadId()));
                dept.setDepartmentHead(head);
            }

            Department updated = departmentRepository.save(dept);
            return convertEntityToDto(updated);
    }


    //Delete dept
    public boolean deleteDepartment(
            Long id
    ){
        Optional<Department> optional = departmentRepository.findById(id);

        if(optional.isPresent()){

            Department dept = optional.get();
            if (dept.getEmployees() != null && !dept.getEmployees().isEmpty()){
                return false;
            }
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    Department convertDtoToEntity(
            DepartmentDTO dto
    ){
        Department dept = modelMapper.map(dto, Department.class);

        if(dept.getId() != null && dept.getId()==0){
            dept.setId(null);
        }
        return dept;
    }

    DepartmentDTO convertEntityToDto(
            Department dpt
    ){
        DepartmentDTO dto = modelMapper.map(dpt, DepartmentDTO.class);
        if(dpt.getDepartmentHead() != null){
            dto.setDepartmentHeadId(dpt.getDepartmentHead().getId());
        }
        return dto;
    }

    public Page<DepartmentDTO> getAllDepartmentsPaginated(
            int page, int size
    ) {
        Page<Department> deptPage = departmentRepository.findAll(PageRequest.of(page, size));
        return deptPage.map(this::convertEntityToDto);
    }

    public Map<String, Object> getAllDepartmentsWithEmployees(Long id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with ID: " + id));

        DepartmentDTO deptDto = convertEntityToDto(dept);
        List<EmployeeDTO> employeeDTOs = dept.getEmployees().stream()
                .map(this::convertEmployeeEntityToDTO).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("department", deptDto);
        response.put("employees", employeeDTOs);
        return response;
    }

    EmployeeDTO convertEmployeeEntityToDTO(
            Employee employee
    ){
        EmployeeDTO dto = modelMapper.map(employee, EmployeeDTO.class);
        if (employee.getDepartment() != null){
            dto.setDepartmentId(employee.getDepartment().getId());
        }
        if (employee.getReportingManager() != null){
            dto.setReportingManagerId(employee.getReportingManager().getId());
        }
        return dto;
    }

    public DepartmentDTO getDepartmentOnly(Long id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with ID: " + id));
        return convertEntityToDto(dept);
    }
}
