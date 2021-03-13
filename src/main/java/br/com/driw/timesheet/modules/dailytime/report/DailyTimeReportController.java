package br.com.driw.timesheet.modules.dailytime.report;

import br.com.driw.timesheet.Constants;
import br.com.driw.timesheet.api.ApiResponse;
import br.com.driw.timesheet.api.ResponseEntityBuild;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping(Constants.DAILY_TIME_REPORT_PATH)
@Api(value = "Sprint Rest Controller", tags = { "DailyTimeReport Controller" })
public class DailyTimeReportController {

	private final DailyTimeReportService dailyTimeReportService;

	@Autowired
	public DailyTimeReportController(
		DailyTimeReportService dailyTimeReportService
	) {
		this.dailyTimeReportService = dailyTimeReportService;
	}

	@GetMapping("/balance/day/{day}")
	@ApiOperation("Calculate balance time of a day")
	public ResponseEntity<ApiResponse<DailyTimeReportResponseDTO>> balanceDay(
		@PathVariable("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day
	) {
		DailyTimeReportResponseDTO response = this.dailyTimeReportService.balance(day, day);

		return ResponseEntityBuild.buildGet(response);
	}

	@GetMapping("/balance/period/{from}/{at}")
	@ApiOperation("Calculate balance time from period")
	public ResponseEntity<ApiResponse<DailyTimeReportResponseDTO>> balancePeriod(
		@PathVariable("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
		@PathVariable("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at
	) {
		DailyTimeReportResponseDTO response = this.dailyTimeReportService.balance(from, at);

		return ResponseEntityBuild.buildGet(response);
	}

	@GetMapping("/balance/month/{year}/{month}")
	@ApiOperation("Calculate balance time from period")
	public ResponseEntity<ApiResponse<DailyTimeReportResponseDTO>> balancePeriod(
		@PathVariable("year") int year,
		@PathVariable("month") int month
	) {
		LocalDate from = LocalDate.of(year, month, 16);
		LocalDate at = from.plusMonths(1).minusDays(1);
		DailyTimeReportResponseDTO response = this.dailyTimeReportService.balance(from, at);

		return ResponseEntityBuild.buildGet(response);
	}

}
