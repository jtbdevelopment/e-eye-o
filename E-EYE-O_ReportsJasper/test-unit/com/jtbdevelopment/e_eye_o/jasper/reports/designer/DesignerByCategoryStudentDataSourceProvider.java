package com.jtbdevelopment.e_eye_o.jasper.reports.designer;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelperImpl;

import java.util.Collections;

/**
 * Date: 8/29/13
 * Time: 8:28 PM
 */
public class DesignerByCategoryStudentDataSourceProvider extends ByCategoryStudentDataSourceProvider {

    public DesignerByCategoryStudentDataSourceProvider() {
        super(new BaseData.FakeDAO(), new IdObjectReflectionHelperImpl(), BaseData.appUser1, Collections.<ClassList>emptySet(), Collections.<Student>emptySet(), Collections.<ObservationCategory>emptySet(), null, null);
    }

}
