# Hospital Management System

A lightweight command-line Hospital Management System written in Java for managing patients, doctors, and appointments. Intended as a small educational/demo application showing basic JDBC usage with MySQL for CRUD operations.

## Stack
- Language(s): Java (100%)
- Build: Maven (pom.xml present)
- Database: MySQL (JDBC)
- Notable libraries: MySQL Connector/J (mysql-connector-j)

## What this is
A simple CLI application that connects to a MySQL database and lets you:
- Add and view patients
- Add and view doctors
- Book and view appointments

The program is driven by `Main.java` and delegates data operations to `Patient.java`, `Doctor.java`, and `Appointment.java` which use plain JDBC PreparedStatements.

## Project layout (top-level)
```
Appointment.java       // Appointment logic (book / view)
Doctor.java            // Doctor logic (add / view)
Main.java              // CLI entrypoint, DB connection and menu loop
Patient.java           // Patient logic (add / view)
pom.xml                // Maven project descriptor (note: source layout is non-standard)
mysql-connector-j-26.7.0.jar // Bundled JDBC driver (not recommended in repo)
README.md              // This file
.github/               // GitHub metadata
settings.json          // Editor/IDE settings
```

How it fits together: Main.java opens a JDBC Connection to MySQL and creates instances of Patient, Doctor and Appointment passing the same Connection and Scanner. The CLI menu forwards commands to those classes which execute SQL against the `hospital` database tables.

## Requirements
- Java (the pom.xml specifies Java 25 as the source/target - adjust if you don't have Java 25; Java 17+ recommended for stability)
- MySQL server (running on localhost:3306 by default)
- Maven (optional, see notes below)

> Note: The project currently includes a `mysql-connector-j-26.7.0.jar` file in the repository but also declares a MySQL dependency in `pom.xml`. It's best to rely on Maven to download the connector and remove the bundled jar from the repo.

## Database schema
Create the database and tables used by the application. Run these statements in your MySQL shell or a client:

```sql
CREATE DATABASE IF NOT EXISTS hospital;
USE hospital;

CREATE TABLE IF NOT EXISTS patients (
  id INT AUTO_INCREMENT PRIMARY KEY,
  Name VARCHAR(100) NOT NULL,
  Age INT,
  Gender VARCHAR(20),
  Phone VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS doctors (
  id INT AUTO_INCREMENT PRIMARY KEY,
  Name VARCHAR(100) NOT NULL,
  Specialization VARCHAR(100),
  Phone VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS appointments (
  AppointmentId INT AUTO_INCREMENT PRIMARY KEY,
  PatientId INT NOT NULL,
  DoctorId INT NOT NULL,
  Date DATE NOT NULL,
  FOREIGN KEY (PatientId) REFERENCES patients(id) ON DELETE CASCADE,
  FOREIGN KEY (DoctorId) REFERENCES doctors(id) ON DELETE CASCADE
);
```

## Quick run (without Maven)
This compiles and runs the Java files directly. From the repository root:

1. Start MySQL and apply the schema above.
2. Edit `Main.java` and set your DB credentials (username and password) at the top, or provide an alternative connection URL.

Compile and run (Linux / macOS):

```bash
# Compile (output classes to ./out)
javac -d out -cp ./mysql-connector-j-26.7.0.jar Main.java Patient.java Doctor.java Appointment.java

# Run (include classes and the driver on classpath)
java -cp out:./mysql-connector-j-26.7.0.jar com.hospitalmanagementsystem.Main
```

Windows (PowerShell / CMD) replace ':' with ';':

```powershell
java -cp out;./mysql-connector-j-26.7.0.jar com.hospitalmanagementsystem.Main
```

Notes:
- The source files declare package `com.hospitalmanagementsystem`. The commands above will compile them in-place and place class files under `out/com/hospitalmanagementsystem/`.
- You must update the DB credentials in `Main.java` or modify the connection URL accordingly.

## Proper Maven setup (recommended)
The repository includes a `pom.xml` but the Java sources are not in the standard Maven layout (src/main/java). To use Maven, move the Java files into the correct package directory:

```
mkdir -p src/main/java/com/hospitalmanagementsystem
# Move files into that directory (or copy)
# git mv Main.java src/main/java/com/hospitalmanagementsystem/Main.java
# ... repeat for Patient.java, Doctor.java, Appointment.java
```

Then build and run with Maven:

```bash
mvn compile
mvn package
# Run from IDE or with the exec plugin (add plugin or run java -cp target/your-jar.jar ...)
```

If you prefer to keep the current layout, you can also add a build plugin to the pom that compiles from the repository root, but moving files to the standard layout is simpler.

## Usage
When you run the program you'll see a menu with options:
- 1 Add Patient
- 2 View Patient Table
- 3 Add Doctor
- 4 View Doctor Table
- 5 Book Appointment
- 6 Show Appointment Table
- 7 Exit

Follow prompts to enter names, ages, IDs and dates (YYYY-MM-DD).

## Known issues & suggestions
- pom.xml sets Java source/target to `25`. If you don't have JDK 25 installed, change `<maven.compiler.source>` and `<maven.compiler.target>` to your installed JDK (e.g., `17`).
- The repo currently contains a large JDBC jar. It's recommended to remove that file and use the Maven dependency declared in `pom.xml`.
- The code expects the database to be available at `jdbc:mysql://localhost:3306/hospital` and the credentials in `Main.java` are empty strings – update them before running.
- Consider adding input validation and better error handling (e.g., for malformed dates), and move SQL into a separate DAO layer for testability.

## Contributing
This is a small demo project. If you'd like to contribute:
- Open issues for bugs or feature requests
- Send a PR with changes. Prefer to modernize the project layout to standard Maven structure and add a README (this file) to explain setup.

## License
Add a LICENSE file with the license you want to use. If you don't have one yet, consider the MIT license for demo projects.

## Contact
Made by Subhadip Biswas. For questions, open an issue on this repository.
