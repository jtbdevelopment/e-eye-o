package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.testng.AssertJUnit.*;

/**
 * Date: 3/9/13
 * Time: 12:17 PM
 */
public class StudentBuilderImplTest extends AppUserOwnedObjectBuilderImplTest {
    private final ClassList cl1 = factory.newClassList(null);
    private final ClassList cl2 = factory.newClassList(null);
    private final Student student = factory.newStudent(null);
    private final StudentBuilderImpl builder = new StudentBuilderImpl(student);

    @Test
    public void testWithFirstName() throws Exception {
        assertEquals("", student.getFirstName());
        final String value = "f";
        assertSame(builder, builder.withFirstName(value));
        assertEquals(value, student.getFirstName());
    }

    @Test
    public void testWithLastName() throws Exception {
        assertEquals("", student.getLastName());
        final String value = "l";
        assertSame(builder, builder.withLastName(value));
        assertEquals(value, student.getLastName());
    }

    @Test
    public void testAddClassList() throws Exception {
        synchronized (student) {
            student.setClassLists(Collections.EMPTY_SET);
            assertSame(builder, builder.addClassList(cl1));
            assertTrue(student.getClassLists().contains(cl1));
            assertEquals(1, student.getClassLists().size());
        }
    }

    @Test
    public void testWithClassLists() throws Exception {
        synchronized (student) {
            student.setClassLists(Collections.EMPTY_SET);
            Set<ClassList> classes = new HashSet<>();
            classes.add(cl1);
            classes.add(cl2);
            assertSame(builder, builder.withClassLists(classes));
            assertTrue(student.getClassLists().containsAll(classes));
            assertEquals(classes.size(), student.getClassLists().size());
        }
    }
}
