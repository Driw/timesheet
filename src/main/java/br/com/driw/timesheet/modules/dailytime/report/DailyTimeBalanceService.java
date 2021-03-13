package br.com.driw.timesheet.modules.dailytime.report;

import br.com.driw.timesheet.modules.dailytime.DailyTime;

public interface DailyTimeBalanceService {
	DailyTimeBalance balanceNormalDailyTime(DailyTime dailyTime);
	DailyTimeBalance balanceAccredit(DailyTime dailyTime);
	DailyTimeBalance balanceMedicalAppointment(DailyTime dailyTime);
	DailyTimeBalance balanceMissedEntryOrDeparture(DailyTime dailyTime);
	DailyTimeBalance balanceSpecial(DailyTime dailyTime);
	DailyTimeBalance balanceMissedDaily(DailyTime dailyTime);
}
