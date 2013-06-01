package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Date: 12/9/12
 * Time: 9:47 AM
 */
public class ClassListImplTest extends AbstractAppUserOwnedObjectTest<ClassListImpl> {

    public ClassListImplTest() {
        super(ClassListImpl.class);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testSetDescription() throws Exception {
        checkStringSetGetsAndValidateNullsAndBlanksAsError("description", ClassList.CLASS_LIST_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR);
    }

    @Test
    public void testDescriptionSize() throws Exception {
        checkStringSizeValidation("description", TOO_LONG_FOR_DESCRIPTION, ClassList.CLASS_LIST_DESCRIPTION_SIZE_ERROR);
    }

    @Test
    public void testSummaryDescription() {
        final ClassList classList = new ClassListImpl(USER1);
        final String desc = "A Description";
        classList.setDescription(" " + desc + " ");
        assertEquals(desc, classList.getSummaryDescription());
    }
}
