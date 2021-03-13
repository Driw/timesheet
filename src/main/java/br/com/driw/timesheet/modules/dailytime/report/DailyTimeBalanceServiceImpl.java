package br.com.driw.timesheet.modules.dailytime.report;

import br.com.driw.timesheet.modules.dailytime.DailyTime;
import br.com.driw.timesheet.utils.LocalTimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class DailyTimeBalanceServiceImpl implements DailyTimeBalanceService {

	private static final LocalTime NO_TIME = DailyTimeBalanceDTO.NO_TIME;
	private static final LocalTime FULL_TIME = LocalTime.of(8, 48, 0);
	private static final LocalTime NIGHT_START = LocalTime.of(22, 0, 0);

	@Override
	public DailyTimeBalanceDTO balanceNormalDailyTime(DailyTime dailyTime) {
		LocalTime normalTime = LocalTimeUtils.between(dailyTime.getEntry(), dailyTime.getDeparture());
		LocalTime nightShift = this.nightShiftBalance(dailyTime);

		return new DailyTimeBalanceDTO(dailyTime)
			.setNormalTime(normalTime)
			.setNightShiftTime(nightShift);
	}

	@Override
	public DailyTimeBalanceDTO balanceAccredit(DailyTime dailyTime) {
		return new DailyTimeBalanceDTO(dailyTime)
			.setAccredit(true);
	}

	@Override
	public DailyTimeBalanceDTO balanceMedicalAppointment(DailyTime dailyTime) {
		return new DailyTimeBalanceDTO(dailyTime)
			.setAccredit(true);
	}

	@Override
	public DailyTimeBalanceDTO balanceMissedEntryOrDeparture(DailyTime dailyTime) {
		LocalTime normalTime = LocalTimeUtils.between(dailyTime.getEntry(), dailyTime.getDeparture());
		LocalTime nightShift = this.nightShiftBalance(dailyTime);

		return new DailyTimeBalanceDTO(dailyTime)
			.setNormalTime(normalTime)
			.setNightShiftTime(nightShift);
	}

	@Override
	public DailyTimeBalanceDTO balanceSpecial(DailyTime dailyTime) {
		LocalTime holidayTime = LocalTimeUtils.between(dailyTime.getEntry(), dailyTime.getDeparture());
		LocalTime nightShift = this.nightShiftBalance(dailyTime);

		return new DailyTimeBalanceDTO(dailyTime)
			.setNightShiftTime(nightShift)
			.setHolidayTime(holidayTime);
	}

	@Override
	public DailyTimeBalanceDTO balanceMissedDaily(DailyTime dailyTime) {
		return new DailyTimeBalanceDTO(dailyTime)
			.setDueTime(FULL_TIME);
	}

	private LocalTime nightShiftBalance(DailyTime dailyTime) {
		LocalTime time = dailyTime.getDeparture().toLocalTime();

		if (time.compareTo(NIGHT_START) <= 0)
			return NO_TIME;

		return LocalTimeUtils.between(NIGHT_START, time);
	}
}
