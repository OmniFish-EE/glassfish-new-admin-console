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

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.glassfish.adminguing.LogInLogOutIT.TypeOfBrowser.CHROMIUM;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Ondro Mihalyi
 */
public class LogInLogOutIT {

    private static final boolean SHOW_BROWSER = false;
    private static final TypeOfBrowser BROWSER = CHROMIUM;

    private static final String APP_CONTEXT_ROOT = "http://localhost:8080/admingui-ng/";

    static TestDriver testDriver;

    @BeforeAll
    public static void setUpAll() {
        testDriver = new TestDriver();
    }

    @Test
    public void hello() {
        given()
                .userNotLoggedIn()
                //
                .when()
                .loadIndexPage()
                //
                .then()
                .loginPageLoaded()
                //
                .when()
                .adminUser("admin")
                .and()
                .pressLoginButton()
                //
                .then()
                .indexPageLoaded()
                //
                .when()
                .pressLogoutButton()
                //
                .then()
                .loginPageLoaded();

    }

    TestDriver given() {
        return testDriver;
    }

    static class TestDriver {

        Playwright playwright = Playwright.create();

        Browser browser;

        {
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
            if (SHOW_BROWSER) {
                options.setHeadless(false).setSlowMo(500);
            }
            BrowserType browserType = BROWSER.getBrowserType(playwright);
            browser = browserType.launch(options);
        }

        Page page = browser.newPage();

        TestDriver when() {
            return this;
        }

        TestDriver then() {
            return this;
        }

        private TestDriver and() {
            return this;
        }

        private TestDriver loginPageLoaded() {
            assertThat(page).hasTitle("Login to continue");
            return this;
        }

        private TestDriver loadIndexPage() {
            page.navigate(APP_CONTEXT_ROOT);
            return this;
        }

        private TestDriver adminUser(String username) {
            page.getByLabel("Username:").fill(username);
            return this;
        }

        private TestDriver pressLoginButton() {
            page.click("input[type=submit]");
            return this;
        }

        private TestDriver indexPageLoaded() {
            assertThat(page).hasTitle("GlassFish Admin Console NG");
            return this;
        }

        private TestDriver pressLogoutButton() {
            page.click("input[type=submit]");
            return this;
        }

        private TestDriver userNotLoggedIn() {
            return this;
        }

    }

    enum TypeOfBrowser {
        CHROMIUM {
            @Override
            BrowserType getBrowserType(Playwright playwright) {
                return playwright.chromium();
            }

        };

        abstract BrowserType getBrowserType(Playwright playwright);
    }

}
