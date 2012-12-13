package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import java.security.InvalidParameterException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Date: 12/8/12
 * Time: 2:58 PM
 */
public class PhotoImplTest extends AbstractIdObjectTest {

    @Test
    public void testConstructors() {
        checkDefaultAndAppUserConstructorTests(PhotoImpl.class);
    }

    @Test
    public void testNewPhotoDefaultTimestamp() throws Exception {
        LocalDateTime before = new LocalDateTime();
        Thread.sleep(1);
        PhotoImpl photo = new PhotoImpl();
        Thread.sleep(1);
        LocalDateTime after = new LocalDateTime();
        assertTrue(before.compareTo(photo.getTimestamp()) < 0);
        assertTrue(after.compareTo(photo.getTimestamp()) > 0);
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testExceptionOnNullTimestamp() {
        new PhotoImpl().setTimestamp(null);
    }

    @Test
    public void testSetGetTimestamp() {
        LocalDateTime now = new LocalDateTime();
        assertEquals(now, new PhotoImpl().setTimestamp(now).getTimestamp());
    }

    @Test
    public void testSetGetDescription() {
        checkStringSetGetsWithNullsSavedAsBlanks(PhotoImpl.class, "description");
    }

}
