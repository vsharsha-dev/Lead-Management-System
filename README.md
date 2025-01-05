# Lead-Management-System

A Lead Management System (LMS) is a tool or software designed to help businesses capture, track, manage, and nurture potential leads (customers) throughout the sales pipeline. It streamlines and optimizes the lead acquisition and conversion process, enabling businesses to maximize their sales opportunities and improve customer relationships.

This repository implements an LMS for Key Account Managers (KAM) to track and optimize the relation with Leads.

### Environment
1. Java JDK 21.0.5
2. Spring Boot 3.4.1 (can be installed via maven packages)
3. IntelliJ IDEA or Eclipse IDE
4. PostgreSQL (The project uses local postgres database)

### Project setup
1. Git clone the repository, or download the zip file.
2. Open the "lms" folder in IntelliJ IDEA (or Eclipse)
3. Modify "application.properties" present in "src/main/resources" according to your local Postgres configuration (DB name, username, password).
4. spring.datasource.url=jdbc:postgresql://localhost:5432/DB_NAME - Database name should be added in this way.
5. After all the configurations are set, provide "mvn clean" command using maven build tool in the IDE
6. Reload the Maven Projects
7. Provide "mvn compile" command using maven
8. In "src/main/java/com/harsha/lms" directory, right click on "LmsApplication" and click on "Run LmsApplication.main()"
9. You can observe the Spring boot project running in the console and server initiated at port 8080.

## ER Model:
<img src="https://github.com/vsharsha-dev/Lead-Management-System/blob/master/LMS-ER.jpeg" alt="ermodel" width="600" height="450" />

