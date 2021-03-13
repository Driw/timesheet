package br.com.driw.timesheet.utils;

import br.com.driw.timesheet.Constants;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.LocalTime;

public class DurationUtils {

	private static final int MINUTE_SECONDS = 60;
	private static final int HOUR_SECONDS = 3600;

	private DurationUtils() { }

	public static Duration zero() {
		return Duration.ofSeconds(0);
	}

	public static Duration from(LocalTime a) {
		int seconds = a.getSecond() + (a.getMinute() * MINUTE_SECONDS) + (a.getHour() * HOUR_SECONDS);

		return Duration.ofSeconds(seconds);
	}

	public static String format(Duration duration) {
		return DurationUtils.format(duration, Constants.JSON_TIME_FORMAT);
	}


	public static String format(Duration duration, String format) {
		long durationMillis = duration.getSeconds() * Constants.SECONDS_TO_MILLISECONDS;
		String signal = durationMillis < 0 ? "-" : "";

		return signal.concat(DurationFormatUtils.formatDuration(Math.abs(durationMillis), format));
	}

	public static String describe(Duration duration) {
		if (duration.getSeconds() > 0)
			return String.format("%s CREDIT", DurationUtils.format(duration));

		return String.format("%s DEBIT", DurationUtils.format(duration).substring(1));
	}
}
