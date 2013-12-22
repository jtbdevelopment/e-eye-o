package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.jtbdevelopment.e_eye_o.entities.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


/**
 * Date: 2/18/13
 * Time: 5:25 PM
 */
public class JacksonJSONIdObjectSerializerV2Test {

    private final String newline = System.getProperty("line.separator");

    private Mockery context;
    private JacksonIdObjectDeserializerV2 deserializer;
    private JacksonIdObjectSerializer serializer;
    private JacksonJSONIdObjectSerializerV2 readWrite;
    private IdObjectFactory idObjectFactory;

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        serializer = context.mock(JacksonIdObjectSerializer.class);
        deserializer = context.mock(JacksonIdObjectDeserializerV2.class);
        idObjectFactory = context.mock(IdObjectFactory.class);
        readWrite = new JacksonJSONIdObjectSerializerV2(serializer, deserializer, idObjectFactory);
    }

    @Test
    public void testInterrogateSettings() {
        final LocalDate jodaDT = new LocalDate(2012, 4, 15);
        readWrite = new JacksonJSONIdObjectSerializerV2(new JacksonIdObjectSerializer() {
            @Override
            public void serialize(final IdObject value, final JsonGenerator generator) throws IOException {
                //  Confirm JODA module registered
                generator.writeStartObject();

                generator.writeObjectField("jodaTest", jodaDT);
                generator.writeEndObject();

                //  Confirm Pretty Printer and Indentation
                assertTrue(generator.getPrettyPrinter() instanceof DefaultPrettyPrinter);
            }
        }, null, null);
        assertEquals("{" + newline + "  \"jodaTest\" : [ 2012, 4, 15 ]" + newline + "}", readWrite.write(context.mock(AppUser.class)));
    }

    @Test
    public void testWriteSingleEntities() throws Exception {
        final List<IdObject> objects = new ArrayList<>();
        objects.add(context.mock(AppUser.class));
        objects.add(context.mock(Student.class));
        objects.add(context.mock(Observation.class));
        for (final IdObject object : objects) {
            context.checking(new Expectations() {{
                one(serializer).serialize(with(object), with(any(JsonGenerator.class)));
            }});
            Assert.assertEquals("", readWrite.write(object));
        }
    }

    @Test
    public void testWriteCollection() throws Exception {
        final List<IdObject> objects = new ArrayList<>();
        objects.add(context.mock(AppUser.class));
        objects.add(context.mock(Student.class));
        objects.add(context.mock(Observation.class));
        for (final IdObject object : objects) {
            context.checking(new Expectations() {{
                one(serializer).serialize(with(object), with(any(JsonGenerator.class)));
            }});
        }
        assertEquals("[ ]", readWrite.write(objects));
    }

    @Test
    public void testRead() throws Exception {

    }
}
