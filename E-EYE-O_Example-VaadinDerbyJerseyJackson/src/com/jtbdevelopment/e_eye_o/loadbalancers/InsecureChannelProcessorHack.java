package com.jtbdevelopment.e_eye_o.loadbalancers;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.channel.InsecureChannelProcessor;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;

/**
 * Date: 10/15/13
 * Time: 9:31 PM
 * <p/>
 * From:
 * http://stackoverflow.com/questions/8002272/offloading-https-to-load-balancers-with-spring-security
 */

@Component
public class InsecureChannelProcessorHack extends InsecureChannelProcessor {

    @Override
    public void decide(FilterInvocation invocation, Collection<ConfigAttribute> config) throws IOException, ServletException {
        for (ConfigAttribute attribute : config) {
            if (supports(attribute)) {
                if ("https".equals(invocation.getHttpRequest().getHeader("X-Forwarded-Proto"))) {
                    getEntryPoint().commence(invocation.getRequest(),
                            invocation.getResponse());
                }
            }
        }
    }
}
