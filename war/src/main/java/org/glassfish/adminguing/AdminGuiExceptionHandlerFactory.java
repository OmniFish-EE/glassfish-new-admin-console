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
package org.glassfish.adminguing;

/**
 *
 * @author Ondro Mihalyi
 */
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.application.ViewExpiredException;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerFactory;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import java.util.Iterator;
import java.util.Optional;

public class AdminGuiExceptionHandlerFactory extends ExceptionHandlerFactory {

    public AdminGuiExceptionHandlerFactory(ExceptionHandlerFactory exceptionHandlerFactory) {
        super(exceptionHandlerFactory);
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new AdminGuiExceptionHandler(getWrapped().getExceptionHandler());
    }

    public static class AdminGuiExceptionHandler extends ExceptionHandlerWrapper {

        public AdminGuiExceptionHandler(ExceptionHandler wrapped) {
            super(wrapped);
        }

        @Override
        public void handle() {
            for (Iterator<ExceptionQueuedEvent> iEvents = getUnhandledExceptionQueuedEvents().iterator();
                    iEvents.hasNext(); /* next */) {
                ExceptionQueuedEvent event = iEvents.next();
                getExceptionCauseByClass(event.getContext().getException(), ViewExpiredException.class)
                        .ifPresent(exception -> {
                    try {
                        FacesContext facesContext = event.getContext().getContext();
                        redirectToOriginalView(facesContext, exception);
                    } finally {
                        iEvents.remove();
                            }
                        });
            }
            // Continue handling unhandled exceptions
            getWrapped().handle();
        }

        private void redirectToOriginalView(FacesContext fc, ViewExpiredException exception) {
            NavigationHandler nav = fc.getApplication().getNavigationHandler();
            nav.handleNavigation(fc, "viewExpired", exception.getViewId());
        }

        private <T extends Throwable> Optional<T> getExceptionCauseByClass(Throwable exception, Class<T> aClass) {
            Throwable exceptionItem = exception;
            while (!aClass.isInstance(exceptionItem)) {
                if (exceptionItem.getCause() == null) {
                    return Optional.empty();
                }
                exceptionItem = exceptionItem.getCause();
            }
            return Optional.of((T) exceptionItem);

        }

    }
}
