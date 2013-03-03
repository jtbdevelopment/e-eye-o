package com.jtbdevelopment.e_eye_o.ria.security.authentication.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 2/24/13
 * Time: 8:24 PM
 * <p/>
 * Largely based on Spring Security Example by Nicolas Frankel at
 * https://github.com/nfrankel/More-Vaadin/tree/master/springsecurity-integration
 */
public class RequestHolder {
    private static final ThreadLocal<HttpServletRequest> THREAD_LOCAL = new ThreadLocal<HttpServletRequest>();

    public static HttpServletRequest getRequest() {
        return THREAD_LOCAL.get();
    }

    public static void setRequest(HttpServletRequest request) {
        THREAD_LOCAL.set(request);
    }

    public static void clean() {
        THREAD_LOCAL.remove();
    }
}
