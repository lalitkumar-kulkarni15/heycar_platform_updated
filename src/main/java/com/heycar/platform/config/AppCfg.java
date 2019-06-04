package com.heycar.platform.config;

import com.heycar.platform.converter.CsvHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import java.util.List;

/**
 * <p>
 *  This class contains all the application related configurations.
 *  All the application specific beans etc would go here.
 * </p>
 *
 * @since 01-06-2019
 * @author  Lalitkumar Kulkarni
 * @version 1.0
 */
@Configuration
public class AppCfg extends WebMvcConfigurerAdapter {

    // For accepting the data in csv format we have added a custom csv http converter.
    @Override
    public void extendMessageConverters (List<HttpMessageConverter<?>> converters) {
        converters.add(new CsvHttpMessageConverter<>());
    }

}
