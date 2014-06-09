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
 */
public interface IdObjectDeserializer {
    /**
     * @param input input text
     * @param <T>   expected output
     * @return returns POJOs instantiated and reference fixed
     */
    <T> T readAsObjects(final String input);

    /**
     * @param input input text
     * @param <T>   expected output
     * @return returns maps, lists, doms etc for serialization type, not POJOs
     */
    <T> T readRaw(final String input);
}
