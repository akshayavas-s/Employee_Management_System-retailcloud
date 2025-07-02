# Employee Management System - Retailcloud
This is a backend REST API developed using Spring Boot as part of the Retailcloud hiring assessment. It manages employees and departments with features like CRUD operations, reporting structure, department-level constraints, and pagination.

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Postman
- ModelMapper

## Features
- Add, update, and list employees and departments
- Prevent department deletion if employees are assigned
- Assign department heads and reporting managers
- Move employees between departments
- Paginated employee listing
- Lookup view with employee names and IDs
- Expand departments to show all employees

## Setup
1. Clone the repository:
   git clone https://github.com/akshayavas-s/Employee_Management_System-retailcloud.git

2. Update database credentials in `application.properties`.

3. Run the application using:
   ./mvnw spring-boot:run

## Database
- See `db/ems-schema.sql` for table structure.
- Contains two tables: `employee` and `department`.

## Postman
- Collection file: `postman/EMS.postman_collection.json`

## Notes
- Screenshots are available in the `Sql Table Screenshots/` folder.
