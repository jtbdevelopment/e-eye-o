package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.serialization.IdObjectSerializer;
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
public class DeletedItemsGenerator implements PreDeleteEventListener {
    private final DAOIdObjectWrapperFactory daoIdObjectWrapperFactory;
    private final IdObjectFactory idObjectFactory;
    private final SessionFactory sessionFactory;
    private final IdObjectSerializer idObjectSerializer;

    @Autowired
    public DeletedItemsGenerator(final DAOIdObjectWrapperFactory daoIdObjectWrapperFactory, final IdObjectFactory idObjectFactory, final SessionFactory sessionFactory, final IdObjectSerializer idObjectSerializer) {
        this.daoIdObjectWrapperFactory = daoIdObjectWrapperFactory;
        this.idObjectFactory = idObjectFactory;
        this.sessionFactory = sessionFactory;
        this.idObjectSerializer = idObjectSerializer;
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
            final DeletedObject deletedObject = idObjectFactory.newDeletedObjectBuilder(entity.getAppUser()).withDeletedId(entity.getId()).build();
            final DeletedObject wrap = daoIdObjectWrapperFactory.wrap(deletedObject);
            event.getSession().save(wrap);
            //  TODO - logic duped with R/W DAO
            if (!(entity instanceof AppUserSettings) && !(entity instanceof TwoPhaseActivity)) {
                final HibernateHistory hibernateHistory = new HibernateHistory();
                hibernateHistory.setAppUser(wrap.getAppUser());
                hibernateHistory.setSerializedVersion(idObjectSerializer.writeEntity(wrap));
                hibernateHistory.setModificationTimestamp(wrap.getModificationTimestamp());
                event.getSession().save(hibernateHistory);
            }
        }
        return false;
    }
}
