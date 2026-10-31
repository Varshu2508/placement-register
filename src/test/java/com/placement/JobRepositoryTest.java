
package com.placement;

import org.junit.jupiter.api.Test;

public class JobRepositoryTest {

    @Test
    public void testFindAllJobs() {
        JobRepository repository = new JobRepository();
        repository.findAllJobs();
    }
}
