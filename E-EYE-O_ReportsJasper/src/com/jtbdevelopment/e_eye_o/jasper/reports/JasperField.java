package com.jtbdevelopment.e_eye_o.jasper.reports;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;

/**
 * Date: 8/28/13
 * Time: 6:57 AM
 */
public class JasperField implements JRField {
    private String name;
    private String description;
    private Class<?> valueClass;

    public JasperField(String name, String description, Class<?> valueClass) {
        this.name = name;
        this.description = description;
        this.valueClass = valueClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public Class<?> getValueClass() {
        return valueClass;
    }

    @Override
    public String getValueClassName() {
        return valueClass.getName();
    }

    @Override
    public boolean hasProperties() {
        return false;
    }

    @Override
    public JRPropertiesMap getPropertiesMap() {
        return null;
    }

    @Override
    public JRPropertiesHolder getParentProperties() {
        return null;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
