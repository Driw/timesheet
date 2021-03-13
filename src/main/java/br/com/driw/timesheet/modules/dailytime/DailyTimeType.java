package br.com.driw.timesheet.modules.dailytime;

public enum DailyTimeType {
	NONE(false),
	NORMAL(true),
	ACCREDIT(false),
	MEDICAL_APPOINTMENT(false),
	MISSED_ENTRY_DEPARTURE(false),
	SPECIAL(true),
	MISSED_DAILY(true);

	public final boolean balance;

	DailyTimeType(boolean balance) {
		this.balance = balance;
	}
}
