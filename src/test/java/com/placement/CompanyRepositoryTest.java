
package com.placement;

import org.junit.jupiter.api.Test;

public class CompanyRepositoryTest {

    @Test
    public void testFindAllCompanies() {
        CompanyRepository repository = new CompanyRepository();
        repository.findAllCompanies();
    }
}
