package com.jtbdevelopment.e_eye_o.serialization;

/**
 * Date: 2/10/13
 * Time: 11:50 AM
 * <p/>
 * Useful marker interface
 */
public interface JSONIdObjectSerializer extends IdObjectSerializer {
    public static final String ENTITY_TYPE_FIELD = "entityType";
    public static final String ID_FIELD = "id";
    public static final String ENTITIES_FIELD = "entities";
    public static final String MORE_FIELD = "more";
}
