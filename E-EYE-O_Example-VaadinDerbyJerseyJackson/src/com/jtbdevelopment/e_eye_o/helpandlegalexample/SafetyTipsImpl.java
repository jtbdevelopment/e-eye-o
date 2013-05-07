package com.jtbdevelopment.e_eye_o.helpandlegalexample;

import com.jtbdevelopment.e_eye_o.helpandlegal.SafetyTips;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Date: 5/2/13
 * Time: 9:08 PM
 */
@Component
@SuppressWarnings("unused")
public class SafetyTipsImpl implements SafetyTips {
    private static final BigDecimal VERSION = new BigDecimal("1.0");
    private static final String LABEL = "Safety Tips";
    private static final String TEXT =
            "<H2><CENTER>Privacy Tips</H2></CENTER>" +
                    "<p>The world has some ugly predators in it." +
                    "<p>While we do our best to keep your information safe, we'd like to suggest a few things to help keep you and your students safe should a privacy breach occur." +
                    "<p><ul>" +
                    "<li>We do not require you to put in student's last names.  We recommend you put in a little necessary for you to identify them." +
                    "<li>We specifically do not allow you to add student profile pictures." +
                    "<li>We recommend you do not include pictures of the children in any student observation photos you upload." +
                    "<li>If you do upload any photos to the class observations that contain pictures of the children, do not provide any identifying comments." +
                    "</ul>" +
                    "<p>Stay safe!";

    @Override
    public String getLabel() {
        return LABEL;
    }

    @Override
    public String getText() {
        return TEXT;
    }

    @Override
    public BigDecimal getVersion() {
        return VERSION;
    }
}
