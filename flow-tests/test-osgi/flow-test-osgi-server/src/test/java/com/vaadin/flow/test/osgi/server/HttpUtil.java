package com.vaadin.flow.test.osgi.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.osgi.service.http.runtime.HttpServiceRuntime;

public class HttpUtil {

	public static HttpURLConnection connect(HttpServiceRuntime httpServiceRuntime, String suffix)
			throws MalformedURLException, IOException, ProtocolException {

		URL url = new URL("http://localhost:" + port(httpServiceRuntime) + suffix);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		return connection;
	}

	public static int port(HttpServiceRuntime httpServiceRuntime) {

		return Integer.valueOf(
				httpServiceRuntime.getRuntimeDTO().serviceDTO.properties.get("org.osgi.service.http.port")
				.toString());
	}
}