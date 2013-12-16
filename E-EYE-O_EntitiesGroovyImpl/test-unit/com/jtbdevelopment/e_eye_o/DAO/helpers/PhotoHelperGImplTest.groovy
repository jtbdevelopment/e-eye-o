package com.jtbdevelopment.e_eye_o.DAO.helpers

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/16/13
 * Time: 2:49 PM
 */
//  TODO - need static mocking working to make this reasonable to test
class PhotoHelperGImplTest extends GroovyTestCase {
    PhotoHelperGImpl photoHelper;

    @BeforeMethod
    public void setUp() {
        photoHelper = new PhotoHelperGImpl()
    }

    @Test
    public void testMimeTypeOfNullIsFalse() {
        assert !photoHelper.isMimeTypeSupported(null)
    }

    @Test
    public void testMimeTypeOfBlankIsFalse() {
        assert !photoHelper.isMimeTypeSupported("")
    }

    @Test
    public void testMimeTypeOfNonImageIsFalse() {
        assert !photoHelper.isMimeTypeSupported("appl")
        assert !photoHelper.isMimeTypeSupported("zip")
    }

    @Test
    public void testMimeTypeNotSupportedIsFalse() {
        assert !photoHelper.isMimeTypeSupported("image/nosupport")
    }

    @Test
    public void testMimeTypeSupportedIsTrue() {
        assert photoHelper.isMimeTypeSupported("image/jpeg")
    }


}
