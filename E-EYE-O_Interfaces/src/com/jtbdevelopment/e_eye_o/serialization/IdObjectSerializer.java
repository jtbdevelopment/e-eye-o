package com.jtbdevelopment.e_eye_o.serialization;

/**
 * Date: 1/26/13
 * Time: 10:29 PM
 *
 * The primary contract we are defining is:
 * Writes:
 *  a) all top level objects will contain a entityType field describing which interface is implemented as opposed to implementing class
 *  b) all referenced IdObjects by primary object will only contain entityType and id field
 *
 * Reads:
 *  a) when instantiating the objects use the object factory
 *  b) load referenced subobjects from dao
 *
 *  So for
 */
public interface IdObjectSerializer {
    String write(final Object entity);

}
