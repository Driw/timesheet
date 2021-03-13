package br.com.driw.timesheet.modules.dailytime.report;

import br.com.driw.timesheet.modules.dailytime.DailyTime;
import br.com.driw.timesheet.utils.LocalTimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class DailyTimeBalanceServiceImpl implements DailyTimeBalanceService {

	private static final LocalTime NO_TIME = DailyTimeBalance.NO_TIME;
	private static final LocalTime FULL_TIME = LocalTime.of(8, 48, 0);
	private static final LocalTime NIGHT_START = LocalTime.of(22, 0, 0);
	private static final float NIGHT_SHIFT_BONUS = 0.25F;
	private static final float HOLIDAY_BONUS = 2F;

	@Override
	public DailyTimeBalance balanceNormalDailyTime(DailyTime dailyTime) {
		LocalTime normalTime = LocalTimeUtils.between(dailyTime.getEntry(), dailyTime.getDeparture());
		LocalTime nightShift = this.nightShiftBalance(dailyTime);

		return new DailyTimeBalance(dailyTime)
			.setNormal(normalTime)
			.setNightShift(nightShift);
	}

	@Override
	public DailyTimeBalance balanceAccredit(DailyTime dailyTime) {
		return new DailyTimeBalance(dailyTime)
			.setAccredit(true);
	}

	@Override
	public DailyTimeBalance balanceMedicalAppointment(DailyTime dailyTime) {
		return new DailyTimeBalance(dailyTime)
			.setAccredit(true);
	}

	@Override
	public DailyTimeBalance balanceMissedEntryOrDeparture(DailyTime dailyTime) {
		LocalTime normalTime = LocalTimeUtils.between(dailyTime.getEntry(), dailyTime.getDeparture());
		LocalTime nightShift = this.nightShiftBalance(dailyTime);

		return new DailyTimeBalance(dailyTime)
			.setNormal(normalTime)
			.setNightShift(nightShift);
	}

	@Override
	public DailyTimeBalance balanceSpecial(DailyTime dailyTime) {
		LocalTime normalTime = LocalTimeUtils.between(dailyTime.getEntry(), dailyTime.getDeparture());
		LocalTime holidayTime = this.applyHolidayBonus(normalTime);
		LocalTime nightShift = this.nightShiftBalance(dailyTime);

		return new DailyTimeBalance(dailyTime)
			.setNormal(normalTime)
			.setNightShift(nightShift)
			.setHolidayTime(holidayTime);
	}

	@Override
	public DailyTimeBalance balanceMissedDaily(DailyTime dailyTime) {
		return new DailyTimeBalance(dailyTime)
			.setDueTime(FULL_TIME);
	}

	private LocalTime nightShiftBalance(DailyTime dailyTime) {
		LocalTime time = dailyTime.getDeparture().toLocalTime();

		if (time.compareTo(NIGHT_START) <= 0)
			return NO_TIME;

		LocalTime nightShift = LocalTimeUtils.between(NIGHT_START, time);

		return this.applyNightShiftBonus(nightShift);
	}

	private LocalTime applyNightShiftBonus(LocalTime nightShift) {
		return this.applyBonus(nightShift, NIGHT_SHIFT_BONUS);
	}

	private LocalTime applyHolidayBonus(LocalTime normalTime) {
		return this.applyBonus(normalTime, HOLIDAY_BONUS);
	}

	private LocalTime applyBonus(LocalTime nightShift, float bonusRate) {
		int hours = nightShift.getHour();
		int minutes = nightShift.getMinute();
		int bonus = (int) Math.floor(((hours * 60) + minutes) * bonusRate);

		return nightShift.plusMinutes(bonus);
	}
}
