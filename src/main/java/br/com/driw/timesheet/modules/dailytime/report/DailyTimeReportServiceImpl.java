package br.com.driw.timesheet.modules.dailytime.report;

import br.com.driw.timesheet.Constants;
import br.com.driw.timesheet.modules.dailytime.DailyTime;
import br.com.driw.timesheet.modules.dailytime.DailyTimeRepository;
import br.com.driw.timesheet.modules.dailytime.DailyTimeType;
import br.com.driw.timesheet.utils.DailyTimeDayReportUtils;
import br.com.driw.timesheet.utils.DurationUtils;
import br.com.driw.timesheet.utils.RepositoryFindPage;
import br.com.driw.timesheet.utils.RepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DailyTimeReportServiceImpl implements DailyTimeReportService {

	private static final float NIGHT_SHIFT_BONUS = 0.25F;
	private static final float HOLIDAY_BONUS = 1F;
	private static final Duration LUNCH_TIME = DurationUtils.from(LocalTime.of(1, 12, 0));
	private static final Duration WORK_TIME = DurationUtils.from(LocalTime.of(8, 48, 0));

	private final DailyTimeRepository dailyTimeRepository;
	private final DailyTimeBalanceService dailyTimeBalanceService;
	private final Map<DailyTimeType, Function<DailyTime, DailyTimeBalanceDTO>> balanceByType;

	@Autowired
	public DailyTimeReportServiceImpl(
		DailyTimeRepository dailyTimeRepository,
		DailyTimeBalanceService dailyTimeBalanceService
	) {
		this.dailyTimeRepository = dailyTimeRepository;
		this.dailyTimeBalanceService = dailyTimeBalanceService;

		this.balanceByType = new TreeMap<>();
		this.balanceByType.put(DailyTimeType.NORMAL, this.dailyTimeBalanceService::balanceNormalDailyTime);
		this.balanceByType.put(DailyTimeType.ACCREDIT, this.dailyTimeBalanceService::balanceAccredit);
		this.balanceByType.put(DailyTimeType.MEDICAL_APPOINTMENT, this.dailyTimeBalanceService::balanceMedicalAppointment);
		this.balanceByType.put(DailyTimeType.MISSED_ENTRY_DEPARTURE, this.dailyTimeBalanceService::balanceMissedEntryOrDeparture);
		this.balanceByType.put(DailyTimeType.SPECIAL, this.dailyTimeBalanceService::balanceSpecial);
		this.balanceByType.put(DailyTimeType.MISSED_DAILY, this.dailyTimeBalanceService::balanceMissedDaily);
	}

	@Override
	public DailyTimeReportResponseDTO balance(LocalDate from, LocalDate at) {
		Pageable pageable = PageRequest.of(0, 1);
		Page<DailyTime> firstPage = this.dailyTimeRepository.findByDayBetween(from, at, pageable);
		List<DailyTime> dailyTimes = RepositoryUtils.findAllContentPages(firstPage, this.balanceFindPage(from, at));
		List<DailyTimeBalanceDTO> dailyTimeBalances = dailyTimes.stream()
			.map(dailyTime -> this.balanceByType.get(dailyTime.getType()).apply(dailyTime))
			.collect(Collectors.toList());

		List<DailyTimeDayReportDTO> balances = dailyTimeBalances.stream()
			.map(this::dailyTimeBalanceToReport)
			.collect(Collectors.toList());
		List<DailyTimeDayDescribedDTO> balancesDescribed = balances.stream()
			.map(DailyTimeDayDescribedDTO::new)
			.collect(Collectors.toList());

		DailyTimeBalanceReportDTO balanceReport = this.balanceReport(balances);
		DailyTimeBalanceDescribedDTO balanceDescribed = new DailyTimeBalanceDescribedDTO(balanceReport);

		return new DailyTimeReportResponseDTO()
			.setBalance(balanceDescribed)
			.setBalances(balancesDescribed);
	}

	private RepositoryFindPage<DailyTime> balanceFindPage(LocalDate from, LocalDate at) {
		return page -> this.dailyTimeRepository.findByDayBetween(from, at, PageRequest.of(page, Constants.MAX_PAGE_LENGTH));
	}

	private DailyTimeBalanceReportDTO balanceReport(List<DailyTimeDayReportDTO> balances) {
		Duration workTime = this.sumLocalTime(balances, DailyTimeDayReportDTO::getWorkTime);
		Duration nightShiftTime = this.sumLocalTime(balances, DailyTimeDayReportDTO::getNightShiftTime);
		Duration additionalNightShift = this.sumLocalTime(balances, DailyTimeDayReportDTO::getAdditionalNightShiftTime);
		Duration holidayTime = this.sumLocalTime(balances, DailyTimeDayReportDTO::getHolidayTime);
		Duration additionalHolidayTime = this.sumLocalTime(balances, DailyTimeDayReportDTO::getAdditionalHolidayTime);
		Duration dueTime = this.sumLocalTime(balances, DailyTimeDayReportDTO::getDueTime);

		List<DailyTimeDayReportDTO> availableBalances = balances.stream()
			.filter(DailyTimeDayReportUtils::canBalance)
			.collect(Collectors.toList());
		Duration balanceTime = this.sumLocalTime(availableBalances, DailyTimeDayReportDTO::getBalanceTime);

		return new DailyTimeBalanceReportDTO()
			.setWorkTime(workTime)
			.setNightShiftTime(nightShiftTime)
			.setAdditionalNightShiftTime(additionalNightShift)
			.setHolidayTime(holidayTime)
			.setAdditionalHolidayTime(additionalHolidayTime)
			.setDueTime(dueTime)
			.setBalanceTime(balanceTime);
	}

	private Duration applyNightShiftBonus(Duration nightShift) {
		return this.applyBonus(nightShift, NIGHT_SHIFT_BONUS);
	}

	private Duration applyHolidayBonus(Duration normalTime) {
		return this.applyBonus(normalTime, HOLIDAY_BONUS);
	}

	private Duration applyBonus(Duration nightShift, float bonusRate) {
		long seconds = nightShift.getSeconds();
		long bonus = (long) Math.floor(seconds * bonusRate);
		bonus -= bonus % Constants.MINUTE_TO_SECONDS;

		return Duration.ofSeconds(bonus);
	}

	private Duration sumLocalTime(List<DailyTimeDayReportDTO> balances, Function<DailyTimeDayReportDTO, Duration> getLocalTime) {
		return balances.stream()
			.map(getLocalTime)
			.reduce(DurationUtils.zero(), Duration::plus);
	}

	private boolean hasLunchTime(DailyTimeBalanceDTO dailyTimeBalance) {
		LocalTime normalTime = dailyTimeBalance.getNormalTime();

		return normalTime.getHour() >= 6;
	}

	private DailyTimeDayReportDTO dailyTimeBalanceToReport(DailyTimeBalanceDTO dailyTimeBalance) {
		boolean lunch = this.hasLunchTime(dailyTimeBalance);
		Duration work = DurationUtils.from(dailyTimeBalance.getNormalTime())
			.minus(lunch ? LUNCH_TIME : DurationUtils.zero());
		Duration nightShift = DurationUtils.from(dailyTimeBalance.getNightShiftTime());
		Duration additionalNightShift = this.applyNightShiftBonus(nightShift);
		Duration holiday = DurationUtils.from(dailyTimeBalance.getHolidayTime());
		Duration additionalHoliday = this.applyHolidayBonus(holiday);
		Duration due = DurationUtils.from(dailyTimeBalance.getDueTime());
		Duration balance = work
			.plus(nightShift)
			.plus(additionalNightShift)
			.plus(holiday)
			.plus(additionalHoliday);

		if (holiday.getSeconds() == 0)
			balance = balance.minus(WORK_TIME);

		return new DailyTimeDayReportDTO()
			.setDailyTime(dailyTimeBalance.getDailyTime())
			.setLunch(lunch)
			.setWorkTime(work)
			.setNightShiftTime(nightShift)
			.setAdditionalNightShiftTime(additionalNightShift)
			.setHolidayTime(holiday)
			.setAdditionalHolidayTime(additionalHoliday)
			.setDueTime(due)
			.setBalanceTime(balance);
	}
}
