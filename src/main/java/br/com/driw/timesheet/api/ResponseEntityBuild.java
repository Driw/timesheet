package br.com.driw.timesheet.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

public class ResponseEntityBuild {

	private ResponseEntityBuild() {
	}

	public static <T extends Serializable> ResponseEntity<Void> buildCreated(T object, Function<T, Long> idConsumer) {
		URI uri = generateCreatedUriLocation(object, idConsumer);

		return ResponseEntity.created(uri).build();
	}

	private static <T extends Serializable> URI generateCreatedUriLocation(T object, Function<T, Long> idConsumer) {
		return ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(map("id", idConsumer.apply(object)))
			.toUri();
	}

	public static <T extends Serializable> ResponseEntity<ApiResponse<T>> buildGet(Optional<T> optional) {
		return optional.map(ResponseEntityBuild::getSuccessfully)
			.orElse(ResponseEntity.notFound().build());
	}

	private static <T extends Serializable> ResponseEntity<ApiResponse<T>> getSuccessfully(T value) {
		return ResponseEntity.ok(generateApiResponse(value));
	}

	private static <T extends Serializable> ApiResponse<T> generateApiResponse(T value) {
		return new ApiResponse<T>()
			.setCode(1)
			.setTimestamp(System.currentTimeMillis())
			.setMessage("Request completed successfully") // TODO message source?
			.setResult(value);
	}

	private static Map<String, String> map(Object... parameters) {
		Map<String, String> map = new TreeMap<>();

		for (int i = 0; i + 1 < parameters.length; i += 2)
			map.put(parameters[i].toString(), parameters[i + 1].toString());

		return map;
	}
}
