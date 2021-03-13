package br.com.driw.timesheet.modules.dailytime.report;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Duration;

@Data
@Accessors(chain = true)
public class DailyTimeBalanceReportDTO implements Serializable {

	private Duration workTime;
	private Duration nightShiftTime;
	private Duration additionalNightShiftTime;
	private Duration holidayTime;
	private Duration additionalHolidayTime;
	private Duration dueTime;
	private Duration balanceTime;

}
