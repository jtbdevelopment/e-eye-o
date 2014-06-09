package com.jtbdevelopment.e_eye_o.hibernate.DAO

import com.jtbdevelopment.e_eye_o.DAO.AbstractUserMaintenanceIntegration
import com.jtbdevelopment.e_eye_o.entities.IdObject
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

/**
 * Date: 1/5/14
 * Time: 3:05 PM
 */
@ContextConfiguration("/test-integration-context.xml")
class HibernateJacksonJerseyUserMaintenanceIntegration extends AbstractUserMaintenanceIntegration {
    @Autowired
    SessionFactory sessionFactory

    @Override
    void saveDirectly(final IdObject entity) {
        Session session = sessionFactory.openSession()
        Transaction trans = session.beginTransaction()
        session.saveOrUpdate(entity)
        trans.commit()
        session.close()
    }
}
