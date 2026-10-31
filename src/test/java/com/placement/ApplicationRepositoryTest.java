
package com.placement;

import org.junit.jupiter.api.Test;

public class ApplicationRepositoryTest {

    @Test
    public void testFindAllApplications() {
        ApplicationRepository repository = new ApplicationRepository();
        repository.findAllApplications();
    }
}
