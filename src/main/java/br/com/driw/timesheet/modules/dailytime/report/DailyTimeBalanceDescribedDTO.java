package br.com.driw.timesheet.modules.dailytime.report;

import br.com.driw.timesheet.utils.DurationUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Duration;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DailyTimeBalanceDescribedDTO implements Serializable {

	private String workTime;
	private String nightShiftTime;
	private String additionalNightShiftTime;
	private String holidayTime;
	private String additionalHolidayTime;
	private String dueTime;
	private String balanceTime;

	public DailyTimeBalanceDescribedDTO(DailyTimeBalanceReportDTO dailyTimeBalanceReport) {
		this.setWorkTime(DurationUtils.format(dailyTimeBalanceReport.getWorkTime()))
			.setNightShiftTime(DurationUtils.format(dailyTimeBalanceReport.getNightShiftTime()))
			.setAdditionalNightShiftTime(DurationUtils.format(dailyTimeBalanceReport.getAdditionalNightShiftTime()))
			.setHolidayTime(DurationUtils.format(dailyTimeBalanceReport.getHolidayTime()))
			.setAdditionalHolidayTime(DurationUtils.format(dailyTimeBalanceReport.getAdditionalHolidayTime()))
			.setDueTime(DurationUtils.format(dailyTimeBalanceReport.getDueTime()))
			.setBalanceTime(DurationUtils.describe(dailyTimeBalanceReport.getBalanceTime()));
	}

}
