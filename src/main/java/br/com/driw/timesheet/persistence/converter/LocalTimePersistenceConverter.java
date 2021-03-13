package br.com.driw.timesheet.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Objects;

@Converter(autoApply = true)
public class LocalTimePersistenceConverter implements AttributeConverter<LocalTime, Time> {

	@Override
	public Time convertToDatabaseColumn(LocalTime attribute) {
		if (Objects.isNull(attribute))
			return null;

		return Time.valueOf(attribute);
	}

	@Override
	public LocalTime convertToEntityAttribute(Time dbData) {
		if (Objects.isNull(dbData))
			return null;

		return dbData.toLocalTime();
	}

}
