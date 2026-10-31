# College Placement Management Portal

## Project Overview

The College Placement Management Portal is a Java-based command-line application that manages student registrations, companies, job opportunities, job applications, and application statuses.

## Technologies Used

- Java
- Apache Maven
- Java Collections Framework
- Visual Studio Code

## Features

1. Add, list, and update student records.
2. Validate student IDs, names, departments, and CGPA values.
3. Add, list, and update company records.
4. Add, list, and update job opportunities.
5. Define job eligibility using minimum CGPA and allowed departments.
6. Allow eligible students to apply for jobs.
7. Prevent duplicate applications for the same student and job.
8. List applications submitted for a specific job.
9. Update application status from APPLIED to SHORTLISTED or REJECTED.
10. Validate mandatory inputs and reject invalid IDs and eligibility failures.

## Prerequisites

- Java JDK
- Apache Maven
- Visual Studio Code (optional)

## How to Run

Open a terminal in the project root directory.

### Build the project

```powershell
& "C:\Program Files\Apache\Maven\apache-maven-3.10.0\bin\mvn.cmd" clean package
```

### Run the application

```powershell
& "C:\Program Files\Apache\Maven\apache-maven-3.10.0\bin\mvn.cmd" exec:java "-Dexec.mainClass=com.placement.App"
```

## Usage

Run the application and select an option from the terminal menu. Follow the prompts to add or view students, companies, and jobs; apply for jobs; list applications; and update application statuses.

## Data Storage

This version stores records in memory using Java collections. Records are cleared when the application exits.

## Project Scope

This is Level 1 of the College Placement Management Portal. It uses a command-line interface and in-memory storage. Database integration, a graphical user interface, and network features are outside the scope of this version.

## Demo Documentation

See [`docs/level-01-demo.md`](docs/level-01-demo.md) for successful workflows, rejected-input examples, and project limitations.
