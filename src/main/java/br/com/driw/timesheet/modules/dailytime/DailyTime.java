package br.com.driw.timesheet.modules.dailytime;

import br.com.driw.timesheet.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_times", uniqueConstraints = {
	@UniqueConstraint(name = "uk_daily_times", columnNames = "day")
})
@Data
public class DailyTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@JsonFormat(pattern = Constants.JSON_DATE_FORMAT, locale = Constants.JSON_LOCALE)
	private LocalDate day;

	@NotNull
	@JsonFormat(pattern = Constants.JSON_DATE_TIME_FORMAT, locale = Constants.JSON_LOCALE)
	private LocalDateTime entry;

	@NotNull
	@JsonFormat(pattern = Constants.JSON_DATE_TIME_FORMAT, locale = Constants.JSON_LOCALE)
	private LocalDateTime departure;

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private DailyTimeType type;

	@Length(max = 128)
	private String observation;
}
