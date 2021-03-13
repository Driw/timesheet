package br.com.driw.timesheet.builder;

import br.com.driw.timesheet.api.ApiConstraintViolation;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

public class ApiConstraintViolationBuilder {

	private ApiConstraintViolationBuilder() {
	}

	public static <T> ApiConstraintViolation build(ConstraintViolation<T> constraintViolation) {
		return new ApiConstraintViolation()
			.setPropertyPath(constraintViolation.getPropertyPath().toString())
			.setMessage(constraintViolation.getMessage())
			.setInvalidValue(constraintViolation.getInvalidValue());
	}

	public static List<ApiConstraintViolation> asList(ConstraintViolationException e) {
		return e.getConstraintViolations().stream()
			.map(ApiConstraintViolationBuilder::build)
			.collect(Collectors.toList());
	}
}
