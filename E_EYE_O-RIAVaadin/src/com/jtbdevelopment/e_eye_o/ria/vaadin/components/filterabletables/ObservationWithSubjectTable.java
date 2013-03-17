package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AllItemsBeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Table;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Date: 3/17/13
 * Time: 4:45 PM
 */
public abstract class ObservationWithSubjectTable extends ObservationTable {
    public ObservationWithSubjectTable(final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final EventBus eventBus, final AppUser appUser, final AllItemsBeanItemContainer<Observation> entities) {
        super(readWriteDAO, idObjectFactory, eventBus, appUser, entities);
    }

    private static final List<HeaderInfo> headersWithSubject;

    static {
        headersWithSubject = Arrays.asList(new HeaderInfo("observationSubject", "Subject", Table.Align.LEFT));
        headersWithSubject.addAll(headers);
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headersWithSubject;
    }

    @Override
    protected void addColumnConverters() {
        super.addColumnConverters();
        entityTable.setConverter("observationSubject", new Converter<String, AppUserOwnedObject>() {
            @Override
            public AppUserOwnedObject convertToModel(final String value, final Locale locale) throws ConversionException {
                //  TODO
                return null;
            }

            @Override
            public String convertToPresentation(final AppUserOwnedObject value, final Locale locale) throws ConversionException {
                return value == null ? null : value.getViewableDescription();
            }

            @Override
            public Class<AppUserOwnedObject> getModelType() {
                return AppUserOwnedObject.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
    }


}
