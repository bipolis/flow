/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.design;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.declarative.DesignContext;

/**
 * Tests that setting local id via DesignContext works as intended.
 * 
 * @since
 * @author Vaadin Ltd
 */
public class DesignContextLocalIdTest {

    @Test
    public void testSetLocalId() throws FileNotFoundException {
        DesignContext ctx = Design.read(new FileInputStream(
                "server/tests/src/com/vaadin/tests/design/local-ids.html"),
                new VerticalLayout());
        TextField tf = (TextField) ctx.getComponentByLocalId("foo");
        Button b = (Button) ctx.getComponentByLocalId("bar");
        // A duplicate id should be handled by removing the id from the old
        // component.
        ctx.setComponentLocalId(b, "foo");
        assertEquals("Found the wrong component by local id.", ctx
                .getComponentByLocalId("foo").getClass(), Button.class);
        assertEquals("Found the wrong component by local id.",
                ctx.getComponentByLocalId("bar"), null);
        // Set an id also for the text field.
        ctx.setComponentLocalId(tf, "bar");
        assertEquals("Found the wrong component by local id.", ctx
                .getComponentByLocalId("foo").getClass(), Button.class);
        assertEquals("Found the wrong component by local id.", ctx
                .getComponentByLocalId("bar").getClass(), TextField.class);
    }
}
