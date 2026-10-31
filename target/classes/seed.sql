
-- 1. Departments
INSERT INTO departments (department_name)
VALUES
    ('Computer Science'),
    ('Information Technology'),
    ('Electronics and Communication'),
    ('Mechanical Engineering')
ON CONFLICT (department_name) DO NOTHING;


-- 2. Students
INSERT INTO students (name, email, department_id, cgpa)
SELECT s.name, s.email, d.department_id, s.cgpa
FROM (VALUES
    ('Arun', 'arun@gmail.com', 'Computer Science', 8.50),
    ('Priya', 'priya@gmail.com', 'Information Technology', 9.00),
    ('Karthik', 'karthik@gmail.com', 'Computer Science', 7.80),
    ('Divya', 'divya@gmail.com', 'Electronics and Communication', 8.20),
    ('Rahul', 'rahul@gmail.com', 'Mechanical Engineering', 7.50)
) AS s(name, email, department_name, cgpa)
JOIN departments d ON d.department_name = s.department_name
ON CONFLICT (email) DO NOTHING;


-- 3. Companies
INSERT INTO companies (company_name)
VALUES
    ('TCS'),
    ('Infosys'),
    ('Wipro'),
    ('Accenture')
ON CONFLICT (company_name) DO NOTHING;


-- 4. Jobs
INSERT INTO jobs (company_id, job_title, minimum_cgpa)
SELECT c.company_id, j.job_title, j.minimum_cgpa
FROM (VALUES
    ('TCS', 'Java Developer', 7.00),
    ('Infosys', 'Web Developer', 7.50),
    ('Wipro', 'Software Engineer', 6.50),
    ('Accenture', 'Python Developer', 7.00)
) AS j(company_name, job_title, minimum_cgpa)
JOIN companies c ON c.company_name = j.company_name
WHERE NOT EXISTS (
    SELECT 1
    FROM jobs existing
    WHERE existing.company_id = c.company_id
      AND existing.job_title = j.job_title
);


-- 5. Job Departments
INSERT INTO job_departments (job_id, department_id)
SELECT j.job_id, d.department_id
FROM (VALUES
    ('TCS', 'Java Developer', 'Computer Science'),
    ('TCS', 'Java Developer', 'Information Technology'),
    ('Infosys', 'Web Developer', 'Computer Science'),
    ('Infosys', 'Web Developer', 'Information Technology'),
    ('Wipro', 'Software Engineer', 'Computer Science'),
    ('Wipro', 'Software Engineer', 'Electronics and Communication'),
    ('Accenture', 'Python Developer', 'Computer Science'),
    ('Accenture', 'Python Developer', 'Information Technology')
) AS mapping(company_name, job_title, department_name)
JOIN companies c ON c.company_name = mapping.company_name
JOIN jobs j ON j.company_id = c.company_id
           AND j.job_title = mapping.job_title
JOIN departments d ON d.department_name = mapping.department_name
ON CONFLICT (job_id, department_id) DO NOTHING;
INSERT INTO applications (student_id, job_id)
SELECT s.student_id, j.job_id
FROM (VALUES
    ('arun@gmail.com', 'Java Developer'),
    ('priya@gmail.com', 'Web Developer'),
    ('karthik@gmail.com', 'Python Developer'),
    ('divya@gmail.com', 'Software Engineer')
) AS app_data(email, job_title)
JOIN students s ON s.email = app_data.email
JOIN jobs j ON j.job_title = app_data.job_title
JOIN job_departments jd
    ON jd.job_id = j.job_id
   AND jd.department_id = s.department_id
WHERE s.cgpa >= j.minimum_cgpa
ON CONFLICT (student_id, job_id) DO NOTHING;
