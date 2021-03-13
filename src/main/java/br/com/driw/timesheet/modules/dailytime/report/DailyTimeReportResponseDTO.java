package br.com.driw.timesheet.modules.dailytime.report;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class DailyTimeReportResponseDTO implements Serializable {
	private DailyTimeBalanceDescribedDTO balance;
	private List<DailyTimeDayDescribedDTO> balances;
}
