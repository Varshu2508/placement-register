
package com.placement;

import org.junit.jupiter.api.Test;

public class StudentRepositoryTest {

    @Test
    public void testFindAllStudents() {
        StudentRepository repository = new StudentRepository();
        repository.findAllStudents();
    }
}
