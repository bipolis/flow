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

import static org.assertj.core.api.Assertions.assertThat;

import com.vaadin.flow.server.osgi.VaadinBundleTracker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

@ExtendWith({ BundleContextExtension.class, ServiceExtension.class })
public class HttpServerTest {

	@InjectBundleContext
	public BundleContext bc;

	@Test
	void test_VaadinBundleTracker_Exist(
			@InjectService(timeout = 1000, cardinality = 1, filter = "(component.name=com.vaadin.flow.server.osgi.VaadinBundleTracker)") Object vaadinBundleTracker)
					throws Exception {

		assertThat(vaadinBundleTracker).isNotNull().isInstanceOf(VaadinBundleTracker.class);

	}

}
