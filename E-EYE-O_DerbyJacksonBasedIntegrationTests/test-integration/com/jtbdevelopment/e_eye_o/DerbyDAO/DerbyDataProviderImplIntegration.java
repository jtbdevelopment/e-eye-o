package com.jtbdevelopment.e_eye_o.DerbyDAO;

import com.jtbdevelopment.e_eye_o.DAO.AbstractDataProviderIntegration;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

/**
 * Date: 11/18/12
 * Time: 11:18 PM
 */
@ContextConfiguration("/test-integration-context.xml")
@Test(groups = {"integration"})
public class DerbyDataProviderImplIntegration extends AbstractDataProviderIntegration {
}
