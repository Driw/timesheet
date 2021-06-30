package br.com.driw.timesheet.api;

import br.com.driw.timesheet.builder.ApiResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

class ResponseEntityBuildTest {

	private static final String SCHEMA = "http";
	private static final String SERVER_NAME = "localhost";
	private static final String REQUEST_URI = "/timesheet";
	private static final String URL = SCHEMA.concat("://")
			.concat(SERVER_NAME)
			.concat(REQUEST_URI);

	@BeforeAll
	public static void beforeAll() {
		MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpServletRequest.setScheme(SCHEMA);
		mockHttpServletRequest.setServerName(SERVER_NAME);
		mockHttpServletRequest.setServerPort(80);
		mockHttpServletRequest.setRequestURI(REQUEST_URI);

		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));
	}

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testBuildCreated() {
		assertThrows(NullPointerException.class, () -> ResponseEntityBuild.buildCreated(new Entity(null), Entity::getId));

		Entity entity = new Entity(1L);
		String locationURL = URL.concat("/").concat(entity.getId().toString());
		ResponseEntity<Void> responseEntity = ResponseEntityBuild.buildCreated(entity, Entity::getId);

		assertNotNull(entity.getId());
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getHeaders().getLocation());
		assertEquals(responseEntity.getHeaders().getLocation().toString(), locationURL);
	}

	@Test
	void testBuildGet() {
		Entity entity = new Entity(1L);
		ResponseEntity<ApiResponse<Entity>> responseEntity = ResponseEntityBuild.buildGet(entity);

		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getBody());
		assertTrue(responseEntity.getBody().getTimestamp() < System.currentTimeMillis());
		assertEquals(responseEntity.getBody().getCode(), ApiResponseCode.SUCCESSFULLY);
		assertEquals(responseEntity.getBody().getMessage(), ResponseEntityBuild.REQUEST_COMPLETED_SUCCESSFULLY);
		assertEquals(responseEntity.getBody().getResult(), entity);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

		responseEntity = ResponseEntityBuild.buildGet(null);

		assertNotNull(responseEntity);
		assertNull(responseEntity.getBody());
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Data
	@AllArgsConstructor
	private static class Entity implements Serializable {
		private Long id;
	}
}