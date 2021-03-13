package br.com.driw.timesheet.modules.dailytime.report;

import java.time.LocalDate;

public interface DailyTimeReportService {
	DailyTimeReportResponseDTO balance(LocalDate from, LocalDate at);
}
