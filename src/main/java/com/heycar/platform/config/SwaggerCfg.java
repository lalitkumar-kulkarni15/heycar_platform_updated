package com.heycar.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 *  This class contains all the swagger documentation related configurations.
 * </p>
 *
 * @since   01-06-2019
 * @author  Lalitkumar Kulkarni
 * @version 1.0
 */
@EnableSwagger2
@Configuration
public class SwaggerCfg {

    private static final String BASE_PACKAGE = "com.heycar.platform.controller";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage(BASE_PACKAGE))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Vehicle listing API.")
                .description("This API provides the rest endpoints to perform C.R.U.D. operations on the " +
                        "vehicle listings. Consumers of this API can :- " +
                        "1) Upload a new vehicle listing to the portal in CSV format. " +
                        "2) Upload a new vehicle listing to the portal in json format. " +
                        "3) Search all the vehicle listings. "
                        +"4)Search the vehicle listings based on the vehicle parameters ")

                .contact(new Contact("Lalitkumar Kulkarni", null, "lalitkulkarniofficial@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }

}
