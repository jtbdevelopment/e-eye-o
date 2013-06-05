package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeStringConverter;
import com.vaadin.ui.Table;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 6/4/13
 * Time: 10:31 PM
 */
public abstract class GeneratedIdObjectTable<T extends AppUserOwnedObject> extends IdObjectTable<T> {

    private List<HeaderInfo> headers;

    @Autowired
    private IdObjectInterfaceResolver idObjectInterfaceResolver;


    @Autowired
    private LocalDateTimeStringConverter localDateTimeStringConverter;

    public GeneratedIdObjectTable(Class<T> entityType) {
        super(entityType);
    }

    @Override
    protected void addColumnConverters() {
        applyFunctionToFields(new Callback() {
            @Override
            public void apply(String fieldName, Method readMethod, IdObjectFieldPreferences field) {
                Class<?> returnType = readMethod.getReturnType();
                if (DateTime.class.equals(returnType)) {
                    entityTable.setConverter(fieldName, dateTimeStringConverter);
                } else if (LocalDateTime.class.equals(returnType)) {
                    entityTable.setConverter(fieldName, localDateTimeStringConverter);
                }

            }
        });
    }

    protected List<String> getTableFields() {
        return new LinkedList<String>() {{
            add("modificationTimestamp");
            add("archived");
        }};
    }

    @Override
    protected synchronized List<HeaderInfo> getHeaderInfo() {
        if (headers == null) {
            headers = new LinkedList<>();
            applyFunctionToFields(new Callback() {
                @Override
                public void apply(String fieldName, Method readMethod, IdObjectFieldPreferences field) {
                    headers.add(new HeaderInfo(fieldName, field.label(), getAlignment(field)));
                }
            });
            headers.add(new HeaderInfo("actions", "Actions", Table.Align.RIGHT));
        }

        return headers;
    }

    protected Table.Align getAlignment(IdObjectFieldPreferences field) {
        Table.Align align = Table.Align.LEFT;
        switch (field.alignment()) {
            case CENTER:
                align = Table.Align.CENTER;
                break;
            case RIGHT:
                align = Table.Align.RIGHT;
                break;
        }
        return align;
    }

    private static interface Callback {
        public void apply(final String fieldName, final Method readMethod, final IdObjectFieldPreferences field);
    }

    private void applyFunctionToFields(final Callback callback) {
        Map<String, Method> fields = idObjectInterfaceResolver.getAllGetMethods(entityType);
        for (String fieldName : getTableFields()) {
            Method readMethod = fields.get(fieldName);
            if (readMethod == null) {
                continue;
            }
            IdObjectFieldPreferences field = readMethod.getAnnotation(IdObjectFieldPreferences.class);
            if (field == null || !field.viewable()) {
                continue;
            }
            callback.apply(fieldName, readMethod, field);
        }
    }

}
