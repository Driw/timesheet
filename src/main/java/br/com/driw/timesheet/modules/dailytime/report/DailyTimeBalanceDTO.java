package br.com.driw.timesheet.modules.dailytime.report;

import br.com.driw.timesheet.Constants;
import br.com.driw.timesheet.modules.dailytime.DailyTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class DailyTimeBalanceDTO {

	public static final LocalTime NO_TIME = LocalTime.of(0, 0, 0);

	private DailyTime dailyTime;

	@JsonFormat(pattern = Constants.JSON_TIME_FORMAT, locale = Constants.JSON_LOCALE)
	private LocalTime normalTime;

	@JsonFormat(pattern = Constants.JSON_TIME_FORMAT, locale = Constants.JSON_LOCALE)
	private LocalTime nightShiftTime;

	@JsonFormat(pattern = Constants.JSON_TIME_FORMAT, locale = Constants.JSON_LOCALE)
	private LocalTime holidayTime;

	@JsonFormat(pattern = Constants.JSON_TIME_FORMAT, locale = Constants.JSON_LOCALE)
	private LocalTime dueTime;
	private Boolean accredit;

	public DailyTimeBalanceDTO(DailyTime dailyTime) {
		this.setDailyTime(dailyTime)
			.setNormalTime(NO_TIME)
			.setNightShiftTime(NO_TIME)
			.setHolidayTime(NO_TIME)
			.setDueTime(NO_TIME)
			.setAccredit(false);
	}
}
