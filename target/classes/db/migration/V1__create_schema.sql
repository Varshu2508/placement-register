CREATE TABLE departments (
    department_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE students (
    student_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    department_id BIGINT NOT NULL REFERENCES departments(department_id),
    cgpa NUMERIC(3,2) NOT NULL CHECK (cgpa >= 0 AND cgpa <= 10)
);

CREATE TABLE companies (
    company_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE jobs (
    job_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(company_id),
    job_title VARCHAR(150) NOT NULL,
    minimum_cgpa NUMERIC(3,2) NOT NULL
        CHECK (minimum_cgpa >= 0 AND minimum_cgpa <= 10)
);

CREATE TABLE job_departments (
    job_id BIGINT NOT NULL REFERENCES jobs(job_id) ON DELETE CASCADE,
    department_id BIGINT NOT NULL REFERENCES departments(department_id),
    PRIMARY KEY (job_id, department_id)
);

CREATE TABLE applications (
    application_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(student_id),
    job_id BIGINT NOT NULL REFERENCES jobs(job_id),
    status VARCHAR(20) NOT NULL DEFAULT 'APPLIED'
        CHECK (status IN ('APPLIED', 'SHORTLISTED', 'REJECTED')),
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (student_id, job_id)
);

