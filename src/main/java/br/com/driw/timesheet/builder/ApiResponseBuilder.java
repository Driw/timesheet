package br.com.driw.timesheet.builder;

import br.com.driw.timesheet.api.ApiResponse;

public class ApiResponseBuilder {

	private ApiResponseBuilder() {
	}

	public static <T> ApiResponse<T> build(int code, String message, T result) {
		return new ApiResponse<T>()
			.setCode(code)
			.setMessage(message)
			.setResult(result)
			.setTimestamp(System.currentTimeMillis());
	}

}
