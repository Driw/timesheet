package br.com.driw.timesheet.modules.dailytime.report;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class DailyTimeReportResponseDTO implements Serializable {
	private DailyTimeBalanceDescribedDTO balance;
	private Map<LocalDate, String> resume;
	private List<DailyTimeDayDescribedDTO> balances;
}
