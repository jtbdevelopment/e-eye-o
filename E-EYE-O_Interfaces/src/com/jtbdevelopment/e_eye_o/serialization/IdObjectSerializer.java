package com.jtbdevelopment.e_eye_o.serialization;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

import java.util.Collection;
import java.util.Map;

/**
 * Date: 1/26/13
 * Time: 10:29 PM
 * <p/>
 * The primary contract we are defining is:
 * Writes:
 * a) all top level objects will contain a entityType field describing which interface is implemented as opposed to implementing class
 * b) all referenced IdObjects by primary object will only contain entityType and id field
 * <p/>
 * Reads:
 * a) when instantiating the objects use the object factory
 * b) load referenced subobjects from dao
 * <p/>
 * So for
 */
public interface IdObjectSerializer {
    String writeEntity(final IdObject entity);

    String writeEntities(final Collection<? extends IdObject> entities);

    String writeMap(final Map<String, Object> map);

    <T> T read(final String input);

    <T> T readNoPOJO(final String input);
}
