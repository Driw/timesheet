package br.com.driw.timesheet.api;

import org.springframework.http.HttpStatus;

public class ApiUtils {

	private ApiUtils() { }

	public static HttpStatus parseHttpStatus(Object object) {
		if (!(object instanceof ImHttpStatus)) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return ((ImHttpStatus) object).getHttpStatus();
	}

}
