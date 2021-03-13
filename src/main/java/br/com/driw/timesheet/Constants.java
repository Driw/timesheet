package br.com.driw.timesheet;

public class Constants {

	public static final long SECONDS_TO_MILLISECONDS = 1000;
	public static final long MINUTE_TO_SECONDS = 60;

	public static final long NO_ID = 0L;
	public static final int MAX_PAGE_LENGTH = 2000;

	public static final String API_PATH = "/api";
	public static final String DAILY_TIME_REPORT_PATH = API_PATH+ "/dailyTimeReport";

	public static final String LOCALE = "America/Sao_Paulo";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_FORMAT = "HH:mm:ss";

	public static final String SWAGGER_API_KEY = "ba831c1c-824f-4c46-8c3f-362cc2c6aa4d";
	public static final String JSON_DATE_FORMAT = DATE_FORMAT;
	public static final String JSON_DATE_TIME_FORMAT = DATE_TIME_FORMAT;
	public static final String JSON_TIME_FORMAT = TIME_FORMAT;
	public static final String JSON_LOCALE = LOCALE;

	private Constants() { 	}

}
