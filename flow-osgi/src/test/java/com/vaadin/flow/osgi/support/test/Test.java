/**
 *
 */
package com.vaadin.flow.osgi.support.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.osgi.support.OsgiVaadinContributor;
import com.vaadin.flow.osgi.support.OsgiVaadinStaticResource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.runtime.HttpServiceRuntime;
import org.osgi.service.http.runtime.dto.RequestInfoDTO;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * @author stbischof
 *
 */
@ExtendWith({ BundleContextExtension.class, ServiceExtension.class,
    ConfigurationExtension.class })
public class Test {

    @InjectBundleContext
    BundleContext bc;
    @InjectService(timeout = 1000)
    HttpService httpService;
    @InjectService(timeout = 1000)
    HttpServiceRuntime httpServiceRuntime;

    static OsgiVaadinStaticResource vsr(String path) {
        return new OsgiVaadinStaticResource() {

            @Override
            public String getPath() {
                return "/" + path;
            }

            @Override
            public String getAlias() {
                return "/alias/" + path;
            }
        };

    }

    @org.junit.jupiter.api.Test

    void test1() throws Exception {

        // printServletContexts();
        RequestInfoDTO riDto = httpServiceRuntime
                .calculateRequestInfoDTO("/alias/foo");

        assertThat(riDto.servletDTO).isNull();

        bc.registerService(OsgiVaadinStaticResource.class, vsr("foo"),
                new Hashtable<String, Object>());

        riDto = httpServiceRuntime.calculateRequestInfoDTO("/alias/foo");
        assertThat(riDto.servletDTO).isNotNull();

        // printServletContexts();
    }

    @org.junit.jupiter.api.Test

    void test2() throws Exception {

        // printServletContexts();
        RequestInfoDTO riDto = httpServiceRuntime
                .calculateRequestInfoDTO("/alias/foo");

        assertThat(riDto.servletDTO).isNull();

        bc.registerService(OsgiVaadinContributor.class,
                new OsgiVaadinContributor() {

            @Override
            public List<OsgiVaadinStaticResource> getContributions() {

                return Collections.singletonList(vsr("foo"));
            }
        }, new Hashtable<String, Object>());

        riDto = httpServiceRuntime.calculateRequestInfoDTO("/alias/foo");
        assertThat(riDto.servletDTO).isNotNull();

        // printServletContexts();
    }

    // only helper while dev
    private void printServletContexts() {
        System.out.println("-------------------------------------------");
        Stream.of(httpServiceRuntime.getRuntimeDTO().servletContextDTOs)
        .forEach(scdto -> {
            System.out.println(scdto);
        });
        System.out.println("-------------------------------------------");
    }

    static HttpURLConnection connect(String sUrl) throws IOException {
        URL url = new URL(sUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection;
    }
}
