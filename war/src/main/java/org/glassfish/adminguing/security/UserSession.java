/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package org.glassfish.adminguing.security;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import org.glassfish.adminguing.Views;

/**
 *
 * @author Ondro Mihalyi
 */
@SessionScoped
@Named
public class UserSession implements Serializable {
    private String glassfishSessionToken;

    @Inject
    private HttpSession httpSession;

    @Inject
    HttpServletRequest httpRequest;

    public String logOut() throws ServletException {
        httpSession.invalidate();
        httpRequest.logout();
        return Views.LOGOUT.getViewId();
    }
}
