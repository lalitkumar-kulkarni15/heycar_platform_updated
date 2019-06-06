package com.heycar.platform.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import static com.heycar.platform.constants.VehicleListingConstants.HEADER_ACCEPT;
import static com.heycar.platform.constants.VehicleListingConstants.MEDIA_TYP_TXT_CSV;

public interface ITestUtils {

    public static String createURLWithPort(final String uri, final String host,
                                           final String port) {
        return "http://" + host + ":" + port + uri;
    }

    public static String readFileAsString(final String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public static HttpEntity<?> getHttpEntityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity<>(headers);
    }

    public static HttpHeaders getHttpHeaderCsv(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MEDIA_TYP_TXT_CSV));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
