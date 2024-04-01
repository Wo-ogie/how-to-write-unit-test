package example.unit_test.web.post.dto;

import org.springframework.lang.Nullable;

public record UpdatePostRequest(
	@Nullable String title,
	@Nullable String content
) {
}
