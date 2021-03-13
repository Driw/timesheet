package br.com.driw.timesheet.config;

import static java.util.Collections.singletonList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

import br.com.driw.timesheet.Constants;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class SwaggerConfig {

	@Autowired
	private TypeResolver typeResolver;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.any())
			.build()
			.pathMapping("/")
			.directModelSubstitute(LocalDate.class, String.class)
			.directModelSubstitute(LocalDateTime.class, String.class)
			.directModelSubstitute(LocalTime.class, String.class)
			.genericModelSubstitutes(ResponseEntity.class)
			.alternateTypeRules(
				newRule(typeResolver.resolve(DeferredResult.class,
					typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
					typeResolver.resolve(WildcardType.class)))
			.useDefaultResponseMessages(false)
			.securitySchemes(singletonList(apiKey()))
			.securityContexts(singletonList(securityContext()))
			.enableUrlTemplating(true)
			.additionalModels(typeResolver.resolve(ResponseEntity.class));
	}

	private ApiKey apiKey() {
		return new ApiKey(Constants.SWAGGER_API_KEY, "api_key", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
			.securityReferences(defaultAuth())
			.forPaths(PathSelectors.regex("/anyPath.*"))
			.build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;

		return singletonList(new SecurityReference(Constants.SWAGGER_API_KEY, authorizationScopes));
	}

	@Bean
	SecurityConfiguration security() {
		return SecurityConfigurationBuilder.builder()
			.clientId("test-app-client-id")
			.clientSecret("test-app-client-secret")
			.realm("test-app-realm")
			.appName("test-app")
			.scopeSeparator(",")
			.additionalQueryStringParams(null)
			.useBasicAuthenticationWithAccessCodeGrant(false)
			.enableCsrfSupport(false)
			.build();
	}

	@Bean
	UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder()
			.deepLinking(true)
			.displayOperationId(false)
			.defaultModelsExpandDepth(1)
			.defaultModelExpandDepth(1)
			.defaultModelRendering(ModelRendering.EXAMPLE)
			.displayRequestDuration(false)
			.docExpansion(DocExpansion.NONE)
			.filter(false)
			.maxDisplayedTags(null)
			.operationsSorter(OperationsSorter.ALPHA)
			.showExtensions(false)
			.showCommonExtensions(false)
			.tagsSorter(TagsSorter.ALPHA)
			.supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
			.validatorUrl(null)
			.build();
	}

}
