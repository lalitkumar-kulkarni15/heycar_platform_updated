package com.heycar.platform.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface ITestUtils {

    public static String createURLWithPort(final String uri, final String host,
                                           final String port) {
        return "http://" + host + ":" + port + uri;
    }

    public static String readFileAsString(final String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
