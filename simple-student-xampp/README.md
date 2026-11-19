# Simple Student API - XAMPP + Postman

## 1. Database (XAMPP)
1. Open **XAMPP Control Panel** -> Start **Apache** and **MySQL**.
2. Open http://localhost/phpmyadmin
3. Click **Import** -> choose `database/student_db.sql` -> **Go**.
   This creates database `student_db`, both tables and 7 sample students.

## 2. Run the project
1. `src/main/resources/application.properties` is already set for XAMPP
   (user `root`, empty password, port 3306). Change it only if you set a MySQL password.
2. From this folder run:  `mvn spring-boot:run`
3. API is at http://localhost:8080/api/students

## 3. Postman
Import `postman/Student-API.postman_collection.json` (Import -> Upload Files).
It contains every request below, ready to send. For POST/PUT the body is already filled in.

| Action | Method + URL |
|---|---|
| Add student | POST `/api/students` |
| All students | GET `/api/students` |
| By ID | GET `/api/students/101` |
| Update scores + skills | PUT `/api/students/101` |
| Delete | DELETE `/api/students/101` |
| Merit list | GET `/api/students/merit-list` |
| Search | GET `/api/students/search?id=101` or `?email=...` or `?course=CSE` |
| Same skills | GET `/api/students/same-skills` |
| Weak programming, maths >= 75 | GET `/api/students/programming-support` |

POST/PUT body must be JSON (Body -> raw -> JSON). PUT needs `programmingScore`, `mathematicsScore`, `technicalSkills`.
Duplicate ID/email -> 409, score outside 0-100 -> 400, unknown ID -> 404.

## 4. Files
`Student.java` (Q1 class) | `StudentRepository.java` (JDBC/SQL) | `StudentService.java` (rules + collections) | `StudentController.java` (REST)

## Common problems
- **Access denied for user 'root'** -> you set a password in XAMPP; put it in `spring.datasource.password`.
- **Communications link failure / Connection refused** -> MySQL is not started in XAMPP.
- **Unknown database 'student_db'** -> you skipped the phpMyAdmin import in step 1.
- **Port 8080 in use** -> add `server.port=8081` to application.properties (and change the port in Postman).
- **MySQL port is not 3306** (XAMPP shows it in the Control Panel) -> change it in the URL.
