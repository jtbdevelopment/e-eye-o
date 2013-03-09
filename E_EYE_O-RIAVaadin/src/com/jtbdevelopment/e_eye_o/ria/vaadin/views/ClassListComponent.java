package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;

/**
 * Date: 3/3/13
 * Time: 10:37 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Theme(Reindeer.THEME_NAME)
public class ClassListComponent extends CustomComponent {
    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private IdObjectFactory idObjectFactory;

    @Override
    public void attach() {
        super.attach();
        setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();

        final AppUser appUser = getSession().getAttribute(AppUser.class);
        Set<ClassList> classLists = readWriteDAO.getEntitiesForUser(ClassList.class, appUser);
        BeanItemContainer<ClassList> beanItemContainer = new BeanItemContainer<>(ClassList.class);
        HierarchicalContainer treeContainer = new HierarchicalContainer();
        beanItemContainer.addAll(classLists);

        Table table = new Table(null, beanItemContainer);
        verticalLayout.addComponent(table);
        table.setHeight(null);
        table.setSelectable(true);

        table.setConverter("archived", new Converter<String, Boolean>() {
            @Override
            public Boolean convertToModel(final String value, final Locale locale) throws ConversionException {
                return Boolean.parseBoolean(value);
            }

            @Override
            public String convertToPresentation(final Boolean value, final Locale locale) throws ConversionException {
                return value ? "Yes" : "No";
            }

            @Override
            public Class<Boolean> getModelType() {
                return Boolean.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
        table.setConverter("modificationTimestamp", new Converter<String, DateTime>() {
            @Override
            public DateTime convertToModel(final String value, final Locale locale) throws ConversionException {
                return DateTime.parse(value);
            }

            @Override
            public String convertToPresentation(final DateTime value, final Locale locale) throws ConversionException {
                return value.toString("MMM dd YYYY hh:mm:ss a");
            }

            @Override
            public Class<DateTime> getModelType() {
                return DateTime.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
        table.setVisibleColumns(new Object[]{"description", "archived", "modificationTimestamp"});
        table.setColumnAlignment("archived", Table.Align.CENTER);
        table.setColumnAlignment("modificationTimestamp", Table.Align.CENTER);
        table.setColumnHeaders(new String[]{"Description", "Archived", "Modified"});

        table.addStyleName("components-inside");
        table.setPageLength(Math.min(10, Math.max(1, classLists.size())));

        verticalLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        verticalLayout.setMargin(true);
        setCompositionRoot(verticalLayout);
    }
}
