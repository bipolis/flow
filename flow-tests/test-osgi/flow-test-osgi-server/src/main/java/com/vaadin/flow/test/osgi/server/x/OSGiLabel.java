package com.vaadin.flow.test.osgi.server.x;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
// Need to explicitly declare the Lumo until https://github.com/vaadin/flow/issues/4847 is fixed
@PWA(name = "Project Base for Vaadin Flow", shortName = "Project Base")
public class OSGiLabel extends Label {

	public OSGiLabel() {

		super("OSGi");

	}
}
