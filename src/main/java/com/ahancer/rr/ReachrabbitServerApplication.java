package com.ahancer.rr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
@ComponentScan
public class ReachrabbitServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReachrabbitServerApplication.class, args);
	}
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/")
				.apiInfo(apiInfo())
				.enableUrlTemplating(true);
				//.securitySchemes(Arrays.asList(apiKey()));
	}
	
	public SecurityScheme apiKey() {
		return new ApiKey("X-Auth-Token", "X-Auth-Token", "header");
	}
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Reachrabbit API")
				.description("Reaching you before you know it")
				.version("0.0.1")
				.license("AHA License")
				.licenseUrl("www.ahancer.com")
				.contact(new Contact("Laki", "www.ahancer.com", "laki7877@gmail.com"))
				.build();
	}
	
	@Bean
	public SecurityConfiguration securityInfo() {
		return new SecurityConfiguration("","","","reachrabbit","",ApiKeyVehicle.HEADER, "X-Auth-Token", ",");
	}
    @Bean
    public UiConfiguration uiConfig() {
	    return new UiConfiguration(
	        null,// url
	        "list",       // docExpansion          => none | list
	        "alpha",      // apiSorter             => alpha
	        "schema",     // defaultModelRendering => schema
	        false,        // enableJsonEditor      => true | false
	        true);        // showRequestHeaders    => true | false
  	}
}
