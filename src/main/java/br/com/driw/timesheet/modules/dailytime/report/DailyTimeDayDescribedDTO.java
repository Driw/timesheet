package br.com.driw.timesheet.modules.dailytime.report;

import br.com.driw.timesheet.modules.dailytime.DailyTime;
import br.com.driw.timesheet.utils.DailyTimeDayReportUtils;
import br.com.driw.timesheet.utils.DurationUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Duration;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DailyTimeDayDescribedDTO implements Serializable {

	private DailyTime dailyTime;
	private Boolean lunch;
	private String workTime;
	private String nightShiftTime;
	private String additionalNightShiftTime;
	private String holidayTime;
	private String additionalHolidayTime;
	private String dueTime;
	private String balanceTime;

	public DailyTimeDayDescribedDTO(DailyTimeDayReportDTO dailyTimeDayReport) {
		this.setDailyTime(dailyTimeDayReport.getDailyTime())
			.setLunch(dailyTimeDayReport.getLunch())
			.setWorkTime(DurationUtils.format(dailyTimeDayReport.getWorkTime()))
			.setNightShiftTime(DurationUtils.format(dailyTimeDayReport.getNightShiftTime()))
			.setAdditionalNightShiftTime(DurationUtils.format(dailyTimeDayReport.getAdditionalNightShiftTime()))
			.setHolidayTime(DurationUtils.format(dailyTimeDayReport.getHolidayTime()))
			.setAdditionalHolidayTime(DurationUtils.format(dailyTimeDayReport.getAdditionalHolidayTime()))
			.setDueTime(DurationUtils.format(dailyTimeDayReport.getDueTime()))
			.setBalanceTime(DailyTimeDayReportUtils.canBalance(dailyTimeDayReport) ? DurationUtils.describe(dailyTimeDayReport.getBalanceTime()) : null);
	}

}
