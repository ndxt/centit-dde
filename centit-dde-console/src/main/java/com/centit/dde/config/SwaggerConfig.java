package com.centit.dde.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author zhf
 */
// @EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public Docket buildDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(buildApiInf())
            .select()
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class) )
            //.apis(RequestHandlerSelectors.basePackage("com.centit.framework.system.controller"))//controller路径
            //.apis(RequestHandlerSelectors.basePackage("com.otherpackage.controller"))//controller路径
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo buildApiInf(){
        return new ApiInfoBuilder()
            .title("数据交换接口")
            .termsOfServiceUrl("https://ndxt.github.io")
            .description("数据交换接口")
            .contact(new Contact(
                "codefan", "https://ndxt.github.io", "codefan@centit.com"))
            .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
