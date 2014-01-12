package com.jtbdevelopment.e_eye_o.jasper.reports.designer;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.jasper.reports.JasperField;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import net.sf.jasperreports.engine.*;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 8/28/13
 * Time: 6:39 AM
 */
public abstract class JasperDataSourceProvider implements JRDataSourceProvider {
    protected final IdObjectReflectionHelper reflectionHelper;
    protected final List<Class<? extends AppUserOwnedObject>> groupBy;

    public JasperDataSourceProvider(final IdObjectReflectionHelper reflectionHelper, final List<Class<? extends AppUserOwnedObject>> groupBy) {
        this.reflectionHelper = reflectionHelper;
        this.groupBy = groupBy;
    }

    @Override
    public boolean supportsGetFieldsOperation() {
        return true;
    }

    @Override
    public JRField[] getFields(final JasperReport jasperReport) throws JRException, UnsupportedOperationException {
        List<JRField> fields = new LinkedList<>();
        Class<Photo> photoClass = Photo.class;
        for (Class<? extends IdObject> groupClass : groupBy) {
            final String pre = reflectionHelper.getIdObjectInterfaceForClass(groupClass).getAnnotation(IdObjectEntitySettings.class).singular().toUpperCase() + "_";
            addFieldsForObject(pre, groupClass, fields);
            for (int i = 0; i < 2; ++i) {
                final String prePhoto = pre + photoClass.getSimpleName() + "-" + i + "_";
                addFieldsForObject(prePhoto, photoClass, fields);
            }
        }
        return fields.toArray(new JRField[fields.size()]);
    }

    private void addFieldsForObject(String pre, Class<? extends IdObject> objectClass, List<JRField> fields) {
        for (Map.Entry<String, Method> get : reflectionHelper.getAllGetMethods(objectClass).entrySet()) {
            final Class<?> returnType = get.getValue().getReturnType();
            if (IdObject.class.isAssignableFrom(returnType) || Collection.class.isAssignableFrom(returnType) || Array.class.isAssignableFrom(returnType)) {
                continue;
            }
            fields.add(new JasperField(pre + get.getKey(), get.getKey(), returnType));
        }
    }

    @Override
    public void dispose(final JRDataSource jrDataSource) throws JRException {
    }
}
