package br.com.driw.timesheet.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ApiConstraintViolation implements Serializable {

	private static final long serialVersionUID = 5155269149689129975L;

	private String propertyPath;
	private String message;
	private Object invalidValue;

}
