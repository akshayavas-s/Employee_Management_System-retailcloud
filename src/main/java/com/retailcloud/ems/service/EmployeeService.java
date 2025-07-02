package com.retailcloud.ems.service;

import com.retailcloud.ems.entity.Department;
import com.retailcloud.ems.entity.Employee;
import com.retailcloud.ems.exceptions.ResourceNotFoundException;
import com.retailcloud.ems.payload.EmployeeDTO;
import com.retailcloud.ems.payload.EmployeeLookupDTO;
import com.retailcloud.ems.repository.DepartmentRepository;
import com.retailcloud.ems.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    //Create employee
    public EmployeeDTO createEmployee(
            EmployeeDTO dto
    ){
        Employee employee = convertDtoToEntity(dto);

        if(dto.getDepartmentId() != null){
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(()-> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));
            employee.setDepartment(department);
        }

        if (dto.getReportingManagerId() != null){
            Employee manager = employeeRepository.findById(dto.getReportingManagerId())
                    .orElseThrow(()-> new ResourceNotFoundException("Manager not found with ID: " + dto.getReportingManagerId()));
            employee.setReportingManager(manager);
         }

        Employee saved = employeeRepository.save(employee);
        return convertEntityToDto(saved);
    }

    //Update Employee
    public EmployeeDTO updateEmployee(
            Long id, EmployeeDTO dto
    ){
        Optional<Employee> optional = employeeRepository.findById(id);

        if(optional.isPresent()){
            Employee employee = optional.get();

            employee.setName(dto.getName());
            employee.setAddress(dto.getAddress());
            employee.setDateOfBirth(dto.getDateOfBirth());
            employee.setSalary(dto.getSalary());
            employee.setRole(dto.getRole());
            employee.setJoiningDate(dto.getJoiningDate());
            employee.setYearlyBonusPercentage(dto.getYearlyBonusPercentage());

            if(dto.getDepartmentId() != null){
                Department dept = departmentRepository.findById(dto.getDepartmentId())
                        .orElseThrow(()-> new ResourceNotFoundException("Department not found with ID: "+ dto.getDepartmentId()));
                employee.setDepartment(dept);
            }

            if(dto.getReportingManagerId() != null){
                Employee manager = employeeRepository.findById(dto.getReportingManagerId())
                        .orElseThrow(()-> new ResourceNotFoundException("Manager not found with ID: " + dto.getReportingManagerId()));
                employee.setReportingManager(manager);
            }

            Employee updated = employeeRepository.save(employee);
            return convertEntityToDto(updated);
        }
        return null;
    }

    Employee convertDtoToEntity(
            EmployeeDTO dto
    ){
        return modelMapper.map(dto, Employee.class);
    }

    EmployeeDTO convertEntityToDto(
            Employee emp
    ){
        EmployeeDTO dto = modelMapper.map(emp, EmployeeDTO.class);

        if(emp.getDepartment() != null){
            dto.setDepartmentId(emp.getDepartment().getId());
        }

        if(emp.getReportingManager() != null ){
            dto.setReportingManagerId(emp.getReportingManager().getId());
        }

        return dto;
    }

    //Moving Employee to another Dept
    public void moveToAnotherDepartment(
            Long empId, Long deptId
    ) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(()-> new ResourceNotFoundException("Employee not found with ID: " + empId));

        Department newDept = departmentRepository.findById(deptId)
                .orElseThrow(()-> new ResourceNotFoundException("Department not found with ID: " + deptId));

        employee.setDepartment(newDept);
        employeeRepository.save(employee);
    }

    public List<EmployeeLookupDTO> getEmployeeNameIdOnly() {
        List<Employee> all = employeeRepository.findAll();
        return all.stream()
                .map(emp -> {
                    EmployeeLookupDTO dto = new EmployeeLookupDTO();
                    dto.setId(emp.getId());
                    dto.setName(emp.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Page<EmployeeDTO> getAllEmployeesPaginated(
            int page, int size
    ) {
        Page<Employee> employeePage = employeeRepository.findAll(PageRequest.of(page, size));
        return employeePage.map(this::convertEntityToDto);
    }
}
