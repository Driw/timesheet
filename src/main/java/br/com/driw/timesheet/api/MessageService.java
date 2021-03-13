package br.com.driw.timesheet.api;

import br.com.driw.timesheet.modules.AutoMessageConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MessageService {

	private final MessageSource messageSource; // Talvez

	public MessageService(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String get(String message, Object... args) {
		return this.messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
	}

	public String get(Enum<?> enumConstant, Object... args) {
		return this.messageSource.getMessage(this.parseMessage(enumConstant), args, LocaleContextHolder.getLocale());
	}

	private String parseMessage(Enum<?> enumConstant) {
		if (enumConstant instanceof AutoMessageConfig) {
			AutoMessageConfig autoMessageConfig = (AutoMessageConfig) enumConstant;

			if (Objects.nonNull(autoMessageConfig.getMessage())) {
				return autoMessageConfig.getMessage();
			}
		}

		return String.format("%s.%s", enumConstant.getClass().getSimpleName(), enumConstant.toString());
	}

}
