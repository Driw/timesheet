package br.com.driw.timesheet.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class LocalTimeUtils {

	private LocalTimeUtils() { }

	public static LocalTime between(LocalDateTime from, LocalDateTime at) {
		long seconds = ChronoUnit.SECONDS.between(from, at);

		return LocalTime.ofSecondOfDay(seconds);
	}

	public static LocalTime between(LocalTime from, LocalTime at) {
		long seconds = ChronoUnit.SECONDS.between(from, at);

		return LocalTime.ofSecondOfDay(seconds);
	}
}
