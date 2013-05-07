package com.jtbdevelopment.e_eye_o.helpandlegal;

import java.math.BigDecimal;

/**
 * Date: 5/7/13
 * Time: 10:02 AM
 * <p/>
 * You must provide an implementation.  If you do not wish to have one, then return a blank text and a version of 0.0
 * Text should be an html fragment
 */
public interface CookiesPolicy {
    String getLabel();

    String getText();

    BigDecimal getVersion();
}
