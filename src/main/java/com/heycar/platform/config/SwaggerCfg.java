package com.heycar.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerCfg {

    @Value("${application.title}")
    private String title;

    @Value("${application.description}")
    private String description;

    @Value("${application.version}")
    private String version;

    @Value("${application.name}")
    private String name;

    @Value("${application.profileLink}")
    private String profileLink;

    @Value("${application.liscence}")
    private String liscence;

    @Value("${application.basePackage}")
    private String basePackage;

    @Value("${application.email}")
    private String email;

    private static final String BLANK = "";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build().apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {

        List<VendorExtension> vendorExtensions = new ArrayList<>();
        return new ApiInfo(title,description, version,BLANK,
                new Contact(name, profileLink, email),
                liscence,BLANK);
    }

}
