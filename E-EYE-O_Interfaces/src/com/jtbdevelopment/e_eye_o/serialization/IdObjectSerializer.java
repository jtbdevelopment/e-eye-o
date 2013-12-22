package com.jtbdevelopment.e_eye_o.serialization;

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
    /**
     * @param entity accepts:
     *               IdObject -> single entity json
     *               Collection<? extends IdObject> -> array of json
     *               PaginatedIdObjectList -> entity with fields + internal array of objects
     *               Everything else is an exception
     * @return json string
     */
    String write(final Object entity);

    <T> T readAsObjects(final String input);

    <T> T readRaw(final String input);
}
