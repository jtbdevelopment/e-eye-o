package com.jtbdevelopment.e_eye_o.hibernate.DAO

import com.jtbdevelopment.e_eye_o.DAO.AbstractArchiveIntegration
import org.springframework.test.context.ContextConfiguration
import org.testng.annotations.Test

/**
 * Date: 12/28/13
 * Time: 9:11 PM
 */
@ContextConfiguration("/test-integration-context.xml")
@Test(groups = ["integration"])
class HibernateJacksonJerseyArchiveIntegration extends AbstractArchiveIntegration {
}
