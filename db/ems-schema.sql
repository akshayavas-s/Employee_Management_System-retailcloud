CREATE TABLE department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    creation_date DATE,
    department_head_id BIGINT,
    CONSTRAINT fk_department_head
        FOREIGN KEY (department_head_id)
        REFERENCES employee(id)
);

CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    date_of_birth DATE,
    salary DOUBLE,
    address VARCHAR(255),
    role VARCHAR(255),
    joining_date DATE,
    yearly_bonus_percentage DOUBLE,
    department_id BIGINT,
    manager_id BIGINT,
    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id)
        REFERENCES department(id),
    CONSTRAINT fk_employee_manager
        FOREIGN KEY (manager_id)
        REFERENCES employee(id)
);


SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE employee;
TRUNCATE TABLE department;
SET FOREIGN_KEY_CHECKS = 1;
