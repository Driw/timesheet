package br.com.driw.timesheet.modules;

import br.com.driw.timesheet.api.ApiConstraintViolation;
import br.com.driw.timesheet.api.ApiError;
import br.com.driw.timesheet.api.ApiResponse;
import br.com.driw.timesheet.api.ApiResponseService;
import br.com.driw.timesheet.api.ApiUtils;
import br.com.driw.timesheet.builder.ApiConstraintViolationBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private final MessageSource messageSource;
	private final ApiResponseService apiResponseService;

	@Autowired
	public RestExceptionHandler(MessageSource messageSource, ApiResponseService apiResponseService) {
		this.messageSource = messageSource;
		this.apiResponseService = apiResponseService;
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<ApiResponse<List<ApiConstraintViolation>>> handleConstraintViolationException(ConstraintViolationException e) {
		List<ApiConstraintViolation> constraintViolations = ApiConstraintViolationBuilder.asList(e);
		ApiResponse<List<ApiConstraintViolation>> body = this.apiResponseService.fromConstraintViolations(constraintViolations);

		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(body);
	}

	@ExceptionHandler({ ModuleRuntimeException.class })
	public ResponseEntity<ApiResponse<ApiError>> handleModuleRuntimeException(ModuleRuntimeException e) {
		HttpStatus httpStatus = ApiUtils.parseHttpStatus(e);
		String message = getMessageSource(e.getAutoMessageConfig(), e.getArguments());

		return ResponseEntity.status(httpStatus)
			.body(this.generateApiResponse(e, httpStatus, message));
	}

	private String getMessageSource(AutoMessageConfig autoMessageConfig, Object[] arguments) {
		String messageProperty = this.generateMessageProperty(autoMessageConfig);

		try {
			return this.messageSource.getMessage(messageProperty, arguments, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	private String generateMessageProperty(AutoMessageConfig autoMessageConfig) {
		if (!autoMessageConfig.getClass().isEnum()) {
			return autoMessageConfig.toString();
		}

		return String.format("%s.%s", autoMessageConfig.getClass().getName(), autoMessageConfig.toString());
	}

	private ApiResponse<ApiError> generateApiResponse(ModuleRuntimeException e, HttpStatus httpStatus, String message) {
		return new ApiResponse<ApiError>()
			.setCode(httpStatus.value())
			.setMessage(message)
			.setTimestamp(System.currentTimeMillis())
			.setResult(this.generateApiError(e));
	}

	private ApiError generateApiError(ModuleRuntimeException e) {
		ApiError apiError = new ApiError()
			.setException(e.getClass().getName())
			.setMessage(e.getMessage());

		Optional.ofNullable(ExceptionUtils.getRootCause(e))
			.ifPresent(cause -> this.fillApiErrorWithCause(apiError, cause));

		return apiError;
	}

	private void fillApiErrorWithCause(ApiError apiError, Throwable cause) {
		apiError.setCause(cause.getClass().getName())
			.setCauseMessage(cause.getMessage());
	}
}
