# Level 1 Demo — College Placement Management Portal

## 1. Successful Workflows

### Workflow 1: Student Registration

**Input:**
- Student ID: S100
- Name: Hareesh
- Department: CSE
- CGPA: 7.69

**Expected Result:** Student registered successfully.

### Workflow 2: Add a Company and Job

**Input:**
- Company ID: C101
- Company Name: TCS
- Job ID: J101
- Role: Software Developer
- Minimum CGPA: 7.50
- Allowed Departments: CSE, IT

**Expected Result:** Company and job are added successfully.

### Workflow 3: Apply for a Job

**Input:**
- Student ID: S100
- Job ID: J101

**Expected Result:** If the student's CGPA meets the minimum requirement and their department is eligible, the application is submitted with a unique application ID.

### Workflow 4: Update Application Status

**Input:**
- Select the application for job J101.
- Choose status SHORTLISTED.

**Expected Result:** Application status is updated to SHORTLISTED.

## 2. Rejected Inputs

### Rejection 1: Duplicate Student ID

**Input:** Register another student using an existing student ID, such as S100.

**Expected Result:** The portal rejects the duplicate student ID.

### Rejection 2: Invalid CGPA

**Input:** Enter a CGPA below 0 or above 10, such as 11.

**Expected Result:** The portal rejects the invalid CGPA.

### Rejection 3: Ineligible Student

**Input:** Apply for a job when the student's CGPA is below the job's minimum CGPA, or the student's department is not in the allowed departments.

**Expected Result:** The portal rejects the application because the student is not eligible.

### Rejection 4: Duplicate Application

**Input:** Apply again for job J101 using the same student ID.

**Expected Result:** The portal rejects the duplicate application.

### Rejection 5: Invalid Student or Job ID

**Input:** Apply using a student ID or job ID that does not exist.

**Expected Result:** The portal displays an appropriate not-found message and rejects the application.

## 3. Limitations

- Records are stored in memory and are not retained after the application exits.
- The application uses a command-line interface.
- Database integration, a graphical user interface, and network features are outside the scope of Level 1.
