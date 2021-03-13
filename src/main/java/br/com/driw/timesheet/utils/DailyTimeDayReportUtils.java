package br.com.driw.timesheet.utils;

import br.com.driw.timesheet.Constants;
import br.com.driw.timesheet.modules.dailytime.DailyTimeType;
import br.com.driw.timesheet.modules.dailytime.report.DailyTimeDayReportDTO;

import java.time.Duration;

public class DailyTimeDayReportUtils {

	private DailyTimeDayReportUtils() { }

	public static boolean canBalance(DailyTimeDayReportDTO dailyTimeDayReport) {
		if (!dailyTimeDayReport.getDailyTime().getType().balance)
			return false;

		if (DailyTimeDayReportUtils.canBalanceDuration(dailyTimeDayReport.getBalanceTime()))
			return true;

		return dailyTimeDayReport.getNightShiftTime().getSeconds() > 0;
	}

	private static boolean canBalanceDuration(Duration duration) {
		long seconds = Math.abs(duration.getSeconds());

		return seconds > Constants.MINUTE_TO_SECONDS * 5;
	}

}
