package com.jtbdevelopment.e_eye_o.ria.security.authentication.vaadin;

/*
 * #%L
 * Vaadin 7 Spring Security Integration
 * %%
 * Copyright (C) 2012 Nicolas Fränkel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.jtbdevelopment.e_eye_o.ria.security.authentication.util.RequestHolder;
import com.vaadin.server.VaadinServlet;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Date: 2/24/13
 * Time: 8:24 PM
 * <p/>
 * Largely based on Spring Security Example by Nicolas Frankel at
 * https://github.com/nfrankel/More-Vaadin/tree/master/springsecurity-integration
 */
public class RequestHolderApplicationServlet extends VaadinServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        RequestHolder.setRequest(request);

        super.service(request, response);

        // We remove the request from the thread local, there's no reason to keep it once the work is done
        RequestHolder.clean();

        SecurityContextHolder.clearContext();
    }
}
