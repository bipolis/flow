package com.vaadin.flow.test.osgi.server.x;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardResource;

/**
 * Registers icon as a web resource available via HTTP.
 *
 * @author Vaadin Ltd
 *
 */
@Component(immediate = true, service = IconResource.class)
@HttpWhiteboardResource(pattern = "/icons/icon.png", prefix = "META-INF/resources/icons/icon.png")
public class IconResource {

}
