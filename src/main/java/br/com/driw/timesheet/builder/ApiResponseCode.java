package br.com.driw.timesheet.builder;

public class ApiResponseCode {

	public static final int SUCCESSFULLY = 0x01;
	public static final int UNEXPECTED_EXCEPTION = 0x02;
	public static final int CONSTRAINT_VIOLATION = 0x03;

	private ApiResponseCode() { }
}
