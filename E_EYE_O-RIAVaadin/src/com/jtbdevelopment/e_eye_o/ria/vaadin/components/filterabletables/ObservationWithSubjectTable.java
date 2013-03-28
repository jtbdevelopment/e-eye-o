package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.ConverterCollection;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.StringAppUserOwnedObjectConverter;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/17/13
 * Time: 4:45 PM
 */
@Component
@Scope("prototype")
public class ObservationWithSubjectTable extends ObservationTable {

    @Autowired
    public ObservationWithSubjectTable(final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final EventBus eventBus, final ConverterCollection converterCollection) {
        super(readWriteDAO, idObjectFactory, eventBus, converterCollection);
    }

    private static final List<HeaderInfo> headersWithSubject;

    static {
        headersWithSubject = new LinkedList<>(Arrays.asList(new HeaderInfo("observationSubject", "Subject", Table.Align.LEFT, true)));
        headersWithSubject.addAll(headers);
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headersWithSubject;
    }

    @Override
    protected Container.Filter generateFilter(String searchFor) {
        //  TODO - this doesn't actually work due to the property search of objects
        return new Or(super.generateFilter(searchFor), new SimpleStringFilter("observationSubject", searchFor, true, false));
    }

    @Override
    protected String getDefaultSortField(List<String> properties) {
        return "observationTimestamp";
    }

    @Override
    protected void addColumnConverters() {
        super.addColumnConverters();
        //  TODO
        entityTable.setConverter("observationSubject", new StringAppUserOwnedObjectConverter());
    }


}
