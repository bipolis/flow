/*
 * Copyright 2000-2020 Vaadin Ltd.
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
package com.vaadin.flow.test.osgi.server;

import javax.servlet.ServletContext;

import com.vaadin.flow.di.Lookup;
import com.vaadin.flow.server.VaadinServletContext;
import com.vaadin.flow.server.osgi.OSGiAccess;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OSGiAccessTest {

	@Test
	public void osgiContextHasLookup() {
		ServletContext context = OSGiAccess.getInstance()
				.getOsgiServletContext();
		VaadinServletContext vaadinServletContext = new VaadinServletContext(
				context);

		Assertions.assertThat(vaadinServletContext).isNotNull();
		Assertions.assertThat(vaadinServletContext.getAttribute(Lookup.class)).isNotNull();
	}
}

