package br.com.driw.timesheet.api;

import br.com.driw.timesheet.modules.AutoMessageConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class MessageServiceTest {

	public static final String MESSAGES_PROPERTIES_TEST = "messages.properties.test";

	private MessageService messageService;

	@Mock
	private MessageSource messageSource;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

		assertNotNull(this.messageSource);

		this.messageService = new MessageService(this.messageSource);
	}

	@Test
	void testGet() {
		String expectedMessage = "Message Service get by enum";
		when(this.messageSource.getMessage(MESSAGES_PROPERTIES_TEST, new Object[0], MessageService.locale())).thenReturn(expectedMessage);
		String message = this.messageService.get(MESSAGES_PROPERTIES_TEST);
		assertEquals(expectedMessage, message);

		when(this.messageSource.getMessage("MessageEnum.TEST", new Object[0], MessageService.locale())).thenReturn(expectedMessage);
		message = this.messageService.get(MessageEnum.TEST);
		assertEquals(expectedMessage, message);

		when(this.messageSource.getMessage("AutoMessageEnum.NULL_MESSAGE", new Object[0], MessageService.locale())).thenReturn(expectedMessage);
		message = this.messageService.get(AutoMessageEnum.NULL_MESSAGE);
		assertEquals(expectedMessage, message);

		when(this.messageSource.getMessage(MESSAGES_PROPERTIES_TEST, new Object[0], MessageService.locale())).thenReturn(expectedMessage);
		message = this.messageService.get(AutoMessageEnum.NON_NULL_MESSAGE);
		assertEquals(expectedMessage, message);
	}

	private enum MessageEnum {
		TEST
	}

	@AllArgsConstructor
	private enum AutoMessageEnum implements AutoMessageConfig {
		NULL_MESSAGE(null),
		NON_NULL_MESSAGE(MESSAGES_PROPERTIES_TEST);

		@Getter
		private String message;
	}
}