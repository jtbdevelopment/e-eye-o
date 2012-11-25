package com.jtbdevelopment.e_eye_o.DerbyDAO;

import com.jtbdevelopment.e_eye_o.HibernateDAO.AbstractDataProviderTest;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Date: 11/18/12
 * Time: 11:18 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-unit-context.xml")
public class DerbyDataProviderImplTest extends AbstractDataProviderTest {
}
