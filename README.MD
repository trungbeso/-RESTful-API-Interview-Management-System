# Interview Management System

## Description

The Interview Management System is designed to streamline the interview process for candidates and interviewers. It provides functionalities for managing interviews, candidates, departments, and skills, ensuring a smooth and efficient workflow.

## Key Features

- Manage interviews, including scheduling and status updates.
- Handle candidate information and their associated skills.
- Organize departments and their respective roles.
- Send reminders to interviewers.
- User authentication and registration.
- Email notifications for account information and password resets.

## Tech Stack

- **Framework:**
  - Spring Boot 3.4.1
  - Spring Data JPA
  - Hibernate
- **Security:** Spring Security with JWT
- **Java Version:** 21
- **Additional Libraries:**
    - Lombok
    - Mapstruct
    - Apache POI
    - JJWT
    - Spring HATEOAS
    - Thymeleaf
    - Swagger for API documentation

## Installation Instructions

1. Clone the repository.
2. Navigate to the project directory.
3. Run the application using your preferred method (e.g., `mvn spring-boot:run`).

## Team Leadership

As the team leader of a 5-member team, I am responsible for connecting with my teammates and guiding them through the Agile Scrum workflow. This involves facilitating communication, organizing sprints, and ensuring that the team adheres to best practices in software development. Besides coding, I play a crucial role in mentoring and supporting my team to achieve our project goals.


## Authentication

The authentication functionality is implemented in the [`AuthService`](src/main/java/com/interviewmanagementsystem/services/auth/AuthService.java) class. It includes methods for user registration and login. The `register` method handles user registration, ensuring that the user does not already exist, generating a username, and encoding the password. The `login` method authenticates the user and generates a JWT token

![login](https://i.imgur.com/SG6CyVP.png)
![register](https://i.imgur.com/sF8qf91.png)


## Email Sender

The email sender functionality is also part of the [`AuthService`](src/main/java/com/interviewmanagementsystem/services/auth/AuthService.java) class. It uses the `EmailService` to send account information and password reset emails. The email templates are defined in the `src/main/resources/templates` directory, such as the [`account-information.html`](src/main/resources/templates/account-information.html) template.

![Information email](https://i.imgur.com/4FThdrT.png)

## Usage

The system provides a RESTful API for managing interviews, candidates, departments, and skills. Below are the key API endpoints:

### Interviews

- `GET /api/interviews`: Retrieve all interviews.
- `POST /api/interviews`: Create a new interview.
- `GET /api/interviews/{id}`: Get interview by id.
- `PUT /api/interviews/{id}`: Update interview by id.
- `DELETE /api/interviews/{id}`: Delete interview by id.
- `GET /api/interviews/search`: Search interviews with pagination.
- `PATCH /api/interviews/{id}/status`: Update interview status by id.
- `PATCH /api/interviews/{id}/result`: Update interview result and note by id.
- `POST /api/interviews/{id}/send-reminder`: Send reminder email to interviewers.

~[interview](https://i.imgur.com/qp3BKLk.png)

### Candidates

- `GET /api/candidates`: Retrieve all candidates.
- `POST /api/candidates/create`: Create a new candidate.
- `GET /api/candidates/{id}`: Get candidate by id.
- `PUT /api/candidates/{id}`: Update candidate by id.
- `DELETE /api/candidates/{id}`: Delete candidate by id.
- `GET /api/candidates/search`: Search candidates.
- `GET /api/candidates/searchByFullName`: Search candidates by full name.
- `PATCH /api/candidates/{id}/status`: Update candidate status by id.

![candidate](https://i.imgur.com/rhHzkLR.png)

### Offers

- `GET /api/offers`: Retrieve all offers.
- `POST /api/offers`: Create a new offer.
- `GET /api/offers/{id}`: Get offer by id.
- `PUT /api/offers/{id}`: Update offer by id.
- `DELETE /api/offers/{id}`: Delete offer by id.
- `GET /api/offers/search`: Search offers.
- `POST /api/offers/{id}/approve`: Approve offer.
- `POST /api/offers/{id}/reject`: Reject offer.
- `PATCH /api/offers/{id}/mark-sent`: Mark offer as sent to candidate.
- `POST /api/offers/{id}/status`: Update offer status by id.
- `GET /api/offers/export`: Export offers.

![offer](https://i.imgur.com/9oTRx1L.png)

### Jobs

- `GET /api/jobs`: Retrieve all jobs.
- `POST /api/jobs`: Create a new job.
- `GET /api/jobs/{id}`: Get job by id.
- `PUT /api/jobs/{id}`: Update job by id.
- `DELETE /api/jobs/{id}`: Delete job by id.
- `GET /api/jobs/search`: Search jobs.
- `GET /api/jobs/status`: Search jobs by status with pagination.
- `GET /api/jobs/statuses`: Get job statuses.
- `POST /api/jobs/upload`: Upload jobs from Excel file.

![job](https://i.imgur.com/eZZqUAJ.png)

### Employees

- `GET /api/employees`: Retrieve all employees.
- `POST /api/employees`: Create a new employee.
- `GET /api/employees/{id}`: Get employee by id.
- `PUT /api/employees/{id}`: Update employee by id.
- `DELETE /api/employees/{id}`: Delete employee by id.
- `GET /api/employees/search`: Search employees with pagination.
- `GET /api/employees/searchByKeyword`: Search employees by keyword.
- `POST /api/employees/change-password`: Change employee password.

![employee](https://i.imgur.com/FHL5Rzj.png)

### Authentication

- `POST /api/auth/login`: Login.
- `POST /api/auth/register`: Register.

### Forgot Password

- `POST /forgotPassword/request-reset-password`: Request reset password.
- `POST /forgotPassword/validate-reset-password-token`: Validate reset password token.
- `POST /forgotPassword/change-password`: Change password.

### Roles

- `GET /api/roles`: Retrieve all roles.
- `GET /api/roles/{id}`: Get role by id.
- `GET /api/roles/searchByKeyword`: Search roles by keyword.


### Departments

- `GET /api/departments`: Retrieve all departments.
- `POST /api/departments`: Create a new department.
- `GET /api/departments/{id}`: Get department by id.
- `PUT /api/departments/{id}`: Update department by id.
- `POST /api/departments/{id}/delete`: Delete department by id.
- `GET /api/departments/name`: Get department by name.

### Skills

- `GET /api/skills`: Retrieve all skills.
- `POST /api/skills`: Create a new skill.

## Contributing

Contributions to the project are welcome! Please follow the standard guidelines for contributing.
