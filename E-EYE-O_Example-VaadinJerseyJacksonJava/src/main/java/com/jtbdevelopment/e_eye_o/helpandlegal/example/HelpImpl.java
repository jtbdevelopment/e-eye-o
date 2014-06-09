package com.jtbdevelopment.e_eye_o.helpandlegal.example;

import com.jtbdevelopment.e_eye_o.helpandlegal.Help;
import org.springframework.stereotype.Component;

/**
 * Date: 5/7/13
 * Time: 10:31 AM
 */
@Component
@SuppressWarnings("unused")
public class HelpImpl implements Help {
    private static final String LABEL = "General Help";
    private static final String TEXT =
            "<H2><CENTER>Help</H2></CENTER>" +
                    "<H3><CENTER>FAQ</CENTER></H3>" +
                    "" +
                    "<p><u>Archiving versus Deleting</u>" +
                    "<p>When you archive something, you simply mark it as inactive.  It will no longer be available on mobile devices and will not show up on reports.  Using the default preferences, it will also be filtered out of your views on the website.  You can re-activate an archived item." +
                    "<p>When you delete something, it is removed permanently." +
                    "<p>When you archive or delete something, all related items are generally also archived or deleted. For example, if you archive a student, all observations and photos for that student are also archived or deleted.  Specifically:<ul>" +
                    "<li>Archiving a student will archive all observations and observation related photos.</li>" +
                    "<li>Archiving a class list will archive class specific observations and photos as well as all students in the class (and their observations and photos).</li>" +
                    "<li>Archiving a semester will archive all observations in the semester's time period.</li>" +
                    "<li>Deleting a student will also delete all observations and photos.</li>" +
                    "<li>Deleting a class list will delete class observations and photos but WILL NOT delete students, they will be updated to no longer be in the class.</li>" +
                    "<li>Deleting a category will update any observation on that category to no longer be on that category.</li>" +
                    "<li>Deleting a semester WILL NOT delete all observations for the semester's time period.</li>" +
                    "</ul>" +
                    "" +
                    "<p><u>Suggesting more help topics</u>" +
                    "<p>Drop us a note using the Contact Us link in the upper right with an area that seems confusing.";

    @Override
    public String getLabel() {
        return LABEL;
    }

    @Override
    public String getText() {
        return TEXT;
    }
}
