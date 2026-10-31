# Level 02 Notes: Database Normalization and 3NF

This project uses a College Placement Management Portal database defined in `src/main/resources/db/migration/V1__create_schema.sql`. The schema is designed to keep data organized, avoid duplication, and maintain clear relationships between tables.

## 1. Database normalization

Database normalization is the process of organizing tables and data so that the database is efficient, consistent, and free from unnecessary repetition.

The main goals are:

- Remove duplicate data
- Reduce update errors
- Make data easier to understand
- Keep relationships between tables clear
- Improve data integrity

In this portal, the data is split into logical tables such as:

- `departments`
- `students`
- `companies`
- `jobs`
- `job_departments`
- `applications`

This separation is important because each table stores only one kind of information.

Example:

- `departments` stores department names
- `students` stores student details and a reference to their department
- `companies` stores company names
- `jobs` stores job details and the company offering the role
- `applications` stores each student’s application to a job

This avoids putting the same data in multiple places.

## 2. First Normal Form (1NF)

A table is in First Normal Form if:

- each column contains only one value
- each row is unique
- there are no repeating groups or lists in a single cell

In this database, the tables are in 1NF because each row has a single value for each column and each table stores one record per entity.

Examples from the schema:

- `departments` has one row per department:
  - `department_id`
  - `department_name`
- `students` has one row per student:
  - `student_id`
  - `name`
  - `email`
  - `department_id`
  - `cgpa`
- `applications` has one row per application, not multiple repeated application entries in one record.

The `applications` table also has a unique rule on `(student_id, job_id)`, which ensures that a student cannot be recorded as applying to the same job twice in separate rows.

The `job_departments` table is a classic many-to-many link table:

- `job_id`
- `department_id`

This table stores one row for each job-to-department relationship, which is a normal 1NF pattern.

## 3. Second Normal Form (2NF)

A table is in Second Normal Form if:

- it is already in 1NF
- every non-key attribute depends on the whole primary key

This is especially relevant when a table has a composite primary key.

In this database:

- `job_departments` has a composite primary key: `(job_id, department_id)`
- The columns `job_id` and `department_id` together identify a row
- Each attribute is tied to the complete key, not just one part of it

Example:

- A row in `job_departments` says: “This job is open to this department.”
- The meaning depends on both `job_id` and `department_id` together.
- It would not make sense to store the department information separately in that table because that would create repetition.

Another example is `applications`:

- It has a primary key `application_id`
- The fields `student_id`, `job_id`, `status`, and `applied_at` all describe the application record itself
- These values depend on the application row, not on only part of a key

So the schema avoids partial dependency problems by keeping each table focused on one entity and using keys to link related records.

## 4. Third Normal Form (3NF)

A table is in Third Normal Form if:

- it is in 2NF
- no non-key column depends on another non-key column

In simple terms, a table should not store information that can be derived from another table through a foreign key.

Example from this database:

- `students` stores `department_id`, not the full department name
- The actual department name is stored in `departments.department_name`
- This avoids repeating the same department name across many student records

Another example:

- `jobs` stores `company_id`, not the whole company name
- The company name is stored in `companies.company_name`

This means the database records the relationship through keys, while the descriptive data remains in the related parent table.

## 5. How this database satisfies 3NF

The College Placement Management Portal database satisfies 3NF because data is split into separate tables based on meaning and relationships.

### Example 1: Departments

`departments` table:

- `department_id` is the primary key
- `department_name` is stored once here

`students` table:

- has `department_id` as a foreign key
- does not repeat the department name

So the department name is not duplicated in every student row.

### Example 2: Companies and jobs

`companies` table:

- `company_id` is the primary key
- `company_name` is stored once here

`jobs` table:

- has `company_id` as a foreign key
- stores job details such as `job_title` and `minimum_cgpa`
- does not repeat the company name

This is a clean 3NF design because the company’s name is stored only in `companies`.

### Example 3: Student application records

`applications` table:

- `application_id` is the primary key
- `student_id` references the student
- `job_id` references the job
- `status` and `applied_at` describe the application itself

No application record stores duplicate values from the `students` or `jobs` tables. The application table keeps only the relationship and the application-specific data.

### Example 4: Job-to-department mapping

`job_departments` table:

- `job_id` and `department_id` together form the primary key
- this table links a job to the departments it is relevant for
- it does not store any extra descriptive data that should belong elsewhere

Because the database uses lookup tables and foreign keys instead of repeating data, it follows the principles of 3NF.

## 6. Purpose of primary keys, foreign keys, and the unique constraint on `(student_id, job_id)`

### Primary keys

A primary key uniquely identifies each row in a table.

Examples in this schema:

- `departments.department_id`
- `students.student_id`
- `companies.company_id`
- `jobs.job_id`
- `applications.application_id`
- `job_departments` uses a composite primary key: `(job_id, department_id)`

Primary keys prevent duplicate rows and allow each record to be referenced reliably.

### Foreign keys

A foreign key creates a relationship between two tables by storing the primary key of another table.

Examples:

- `students.department_id` references `departments.department_id`
- `jobs.company_id` references `companies.company_id`
- `applications.student_id` references `students.student_id`
- `applications.job_id` references `jobs.job_id`
- `job_departments.job_id` references `jobs.job_id`
- `job_departments.department_id` references `departments.department_id`

Foreign keys enforce referential integrity. This means a row cannot reference a non-existing record. For example, an application cannot point to a student or job that does not exist.

### Unique constraint on `(student_id, job_id)`

The `applications` table includes:

`UNIQUE (student_id, job_id)`

This ensures that the same student cannot apply to the same job more than once.

Example:

- Student 12 applying to Job 7 is allowed once
- If another row tries to insert Student 12 and Job 7 again, the database rejects it

This is valuable because the business rule is usually: one student can have one application record per job. It prevents duplicate applications while still allowing the same student to apply to different jobs.

In summary, the unique constraint protects the business logic, while the primary key identifies each application row and the foreign keys maintain valid relationships between the tables.

## Database Configuration

This project connects to a PostgreSQL database named `placement_portal`.

### 1. Database name

The database is named:

- `placement_portal`

This is the PostgreSQL database the application expects to connect to.

### 2. JDBC URL format

The Java application uses a JDBC connection string in this format:

- `jdbc:postgresql://localhost:5432/placement_portal`

This means:

- `jdbc:postgresql://` = PostgreSQL JDBC driver prefix
- `localhost` = the database server is running on the local machine
- `5432` = the default PostgreSQL port
- `placement_portal` = the database name

### 3. Environment variables

The project reads the database connection details from environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

These are used by the application to connect securely without storing credentials directly in the code.

### 4. Setting variables in Windows PowerShell

In Windows PowerShell, you can set the environment variables for the current terminal session like this:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/placement_portal"
$env:DB_USERNAME = "your_username"
$env:DB_PASSWORD = "your_password_here"
```

Replace the placeholder values with your own local PostgreSQL username and password. The password should be treated as a secret and not shared in code, logs, or documentation.

You can also verify them with:

```powershell
$env:DB_URL
$env:DB_USERNAME
$env:DB_PASSWORD
```

### 5. Security note

Passwords must never be hardcoded into Java source files, SQL scripts, configuration files, or committed to the project repository.

This is important because:

- credentials can be exposed to other developers
- secrets may be uploaded to Git or shared accidentally
- production or local database access could be compromised

Use placeholder values such as `your_password_here` or a secure environment variable setup instead of writing a real password into the project.

The application expects these values to be supplied from the environment, and the actual password should remain outside the repository.

---

## Final summary

This schema is a good example of normalized database design. It keeps each table responsible for a single concept, avoids repeating data, and uses keys to link related information.

The design supports clear relationships between:

- students and departments
- jobs and companies
- jobs and departments
- students and job applications

This is exactly what makes the database consistent with 3NF.
