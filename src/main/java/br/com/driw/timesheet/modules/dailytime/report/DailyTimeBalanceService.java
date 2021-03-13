package br.com.driw.timesheet.modules.dailytime.report;

import br.com.driw.timesheet.modules.dailytime.DailyTime;

public interface DailyTimeBalanceService {
	DailyTimeBalanceDTO balanceNormalDailyTime(DailyTime dailyTime);
	DailyTimeBalanceDTO balanceAccredit(DailyTime dailyTime);
	DailyTimeBalanceDTO balanceMedicalAppointment(DailyTime dailyTime);
	DailyTimeBalanceDTO balanceMissedEntryOrDeparture(DailyTime dailyTime);
	DailyTimeBalanceDTO balanceSpecial(DailyTime dailyTime);
	DailyTimeBalanceDTO balanceMissedDaily(DailyTime dailyTime);
}
