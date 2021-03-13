package br.com.driw.timesheet.modules.dailytime.report;

import br.com.driw.timesheet.Constants;
import br.com.driw.timesheet.utils.RepositoryFindPage;
import br.com.driw.timesheet.utils.RepositoryUtils;
import br.com.driw.timesheet.modules.dailytime.DailyTime;
import br.com.driw.timesheet.modules.dailytime.DailyTimeRepository;
import br.com.driw.timesheet.modules.dailytime.DailyTimeType;
import br.com.driw.timesheet.modules.dailytimecheck.DailyTimeCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DailyTimeReportServiceImpl implements DailyTimeReportService {

	private final DailyTimeRepository dailyTimeRepository;
	private final DailyTimeCheckRepository dailyTimeCheckRepository;
	private final DailyTimeBalanceService dailyTimeBalanceService;
	private final Map<DailyTimeType, Function<DailyTime, DailyTimeBalance>> balanceByType;

	@Autowired
	public DailyTimeReportServiceImpl(
		DailyTimeRepository dailyTimeRepository,
		DailyTimeCheckRepository dailyTimeCheckRepository,
		DailyTimeBalanceService dailyTimeBalanceService
	) {
		this.dailyTimeRepository = dailyTimeRepository;
		this.dailyTimeCheckRepository = dailyTimeCheckRepository;
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
	public DailyTimeReportResponse balance(LocalDate from, LocalDate at) {
		Pageable pageable = PageRequest.of(0, 1);
		Page<DailyTime> firstPage = this.dailyTimeRepository.findByDayBetween(from, at, pageable);
		List<DailyTime> dailyTimes = RepositoryUtils.findAllContentPages(firstPage, this.balanceFindPage(from, at));
		List<DailyTimeBalance> balances = dailyTimes.stream()
			.map(dailyTime -> this.balanceByType.get(dailyTime.getType()).apply(dailyTime))
			.collect(Collectors.toList());

		return new DailyTimeReportResponse()
			.setBalances(balances);
	}

	private RepositoryFindPage<DailyTime> balanceFindPage(LocalDate from, LocalDate at) {
		return page -> this.dailyTimeRepository.findByDayBetween(from, at, PageRequest.of(page, Constants.MAX_PAGE_LENGTH));
	}
}
