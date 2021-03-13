package br.com.driw.timesheet.modules;

import br.com.driw.timesheet.api.ImHttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModuleRuntimeException extends RuntimeException implements ImHttpStatus {

	private static final long serialVersionUID = -4018473275846647393L;

	private HttpStatus httpStatus;
	private AutoMessageConfig autoMessageConfig;
	private Object[] arguments;

	public ModuleRuntimeException(AutoMessageConfig autoMessageConfig) {
		super(autoMessageConfig.toString());

		this.setAutoMessageConfig(autoMessageConfig);
		this.setHttpStatus(HttpStatus.BAD_REQUEST);
	}

	public ModuleRuntimeException(AutoMessageConfig autoMessageConfig, Object... args) {
		super(autoMessageConfig.toString());

		this.setAutoMessageConfig(autoMessageConfig);
		this.setHttpStatus(HttpStatus.BAD_REQUEST);
		this.setArguments(args);
	}
}
