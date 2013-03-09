package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

/**
 * Date: 3/9/13
 * Time: 12:11 PM
 */
public class PhotoBuilderImplTest extends AppUserOwnedObjectBuilderImplTest {
    private final Photo photo = factory.newPhoto(null);
    private final ClassList classList = factory.newClassList(null);
    private final PhotoBuilderImpl builder = new PhotoBuilderImpl(photo);

    @Test
    public void testWithPhotoFor() throws Exception {
        assertNull(photo.getPhotoFor());
        assertSame(builder, builder.withPhotoFor(classList));
        assertSame(classList, photo.getPhotoFor());
    }

    @Test
    public void testWithDescription() throws Exception {
        final String desc = "description";
        assertEquals("", photo.getDescription());
        assertSame(builder, builder.withDescription(desc));
        assertEquals(desc, photo.getDescription());
    }

    @Test
    public void testWithTimestamp() throws Exception {
        final LocalDateTime dateTime = new LocalDateTime();
        photo.setTimestamp(dateTime.minusMonths(21));
        assertSame(builder, builder.withTimestamp(dateTime));
        assertEquals(dateTime, photo.getTimestamp());
    }
}
