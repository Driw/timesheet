package br.com.driw.timesheet.modules.dailytime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@RepositoryRestResource(collectionResourceRel = "dailyTime", path = "dailyTime")
public interface DailyTimeRepository extends PagingAndSortingRepository<DailyTime, Long> {

	Page<DailyTime> findByDayBetween(
		@Param("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
		@Param("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at,
		Pageable pageable
	);
}
