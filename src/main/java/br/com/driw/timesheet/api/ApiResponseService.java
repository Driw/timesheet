package br.com.driw.timesheet.api;

import br.com.driw.timesheet.builder.ApiResponseBuilder;
import br.com.driw.timesheet.builder.ApiResponseCode;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiResponseService {

	private final MessageService messageService;

	public ApiResponseService(MessageService messageService) {
		this.messageService = messageService;
	}

	public ApiResponse<String[]> fromException(Exception e) {
		return ApiResponseBuilder.build(ApiResponseCode.UNEXPECTED_EXCEPTION, this.messageService.get(ApiCommonMessages.UNEXPECTED_MESSAGE), ExceptionUtils.getStackFrames(e));
	}

	public ApiResponse<List<ApiConstraintViolation>> fromConstraintViolations(List<ApiConstraintViolation> constraintViolations) {
		return ApiResponseBuilder.build(ApiResponseCode.CONSTRAINT_VIOLATION, this.messageService.get(ApiCommonMessages.CONSTRAINT_VIOLATION), constraintViolations);
	}

}
