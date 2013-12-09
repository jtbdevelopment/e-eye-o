package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectGImpl
import org.joda.time.DateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 3:57 PM
 */
class IdObjectBuilderGImplTest {

    static class LocalEntityImpl extends IdObjectGImpl {
    }

    def entity;
    def builder;

    @BeforeMethod
    def setUp() {
        entity = new LocalEntityImpl()
        builder = new IdObjectBuilderGImpl(entity: entity)
    }

    @Test
    public void testBuild() throws Exception {
        assert entity.is(builder.build())
    }

    protected testSetField(final String singular, def String plural, def value) {
        String add = "add" + singular[0].toUpperCase() + singular.substring(1)
        String with = "with" + plural[0].toUpperCase() + plural.substring(1)

        assert entity."$plural".isEmpty()
        assert builder.is(builder."$add"(value))
        assert ([value] as Set) == entity."$plural"

        entity."$plural" = [] as Set
        assert entity."$plural".isEmpty()
        assert builder.is(builder."$with"([value] as Set))
        assert ([value] as Set) == entity."$plural"
    }

    protected testStringField(final String field) {
        testField(field, "StringValue")
    }

    protected testField(final String field, final def value) {
        String with = "with" + field[0].toUpperCase() + field.substring(1)
        assert builder.is(builder."$with"(value))
        assert value == entity."$field"
    }

    @Test
    public void testWithId() throws Exception {
        testStringField("id")
    }

    @Test
    public void testWithModificationTimestamp() throws Exception {
        testField("modificationTimestamp", DateTime.now())
    }
}
