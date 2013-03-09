package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.DeletedObject;
import com.jtbdevelopment.e_eye_o.entities.utilities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 2/17/13
 * Time: 8:39 PM
 */
@Component
@SuppressWarnings("unused")
public class HibernatePreDeleteEventListener implements PreDeleteEventListener {
    private final DAOIdObjectWrapperFactory daoIdObjectWrapperFactory;
    private final IdObjectFactory idObjectFactory;
    private final SessionFactory sessionFactory;

    @Autowired
    public HibernatePreDeleteEventListener(final DAOIdObjectWrapperFactory daoIdObjectWrapperFactory, final IdObjectFactory idObjectFactory, final SessionFactory sessionFactory) {
        this.daoIdObjectWrapperFactory = daoIdObjectWrapperFactory;
        this.idObjectFactory = idObjectFactory;
        this.sessionFactory = sessionFactory;
    }

    @PostConstruct
    public void registerMe() {
        if (sessionFactory instanceof SessionFactoryImpl) {
            ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(EventListenerRegistry.class).appendListeners(EventType.PRE_DELETE, this);
        }
    }

    @Override
    public boolean onPreDelete(final PreDeleteEvent event) {
        if ((event.getEntity() instanceof AppUserOwnedObject) &&
                !(event.getEntity() instanceof DeletedObject)) {
            AppUserOwnedObject entity = (AppUserOwnedObject) event.getEntity();
            final DeletedObject deletedObject = idObjectFactory.newAppUserOwnedObject(DeletedObject.class, entity.getAppUser()).setDeletedId(entity.getId());
            final DeletedObject wrap = daoIdObjectWrapperFactory.wrap(deletedObject);
            event.getSession().save(wrap);
        }
        return false;
    }
}
