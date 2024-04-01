package example.unit_test.domain.post.dto;

import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotNull;

public record UpdatePostCommand(
	@NotNull Long requestUserId,
	@NotNull Long postId,
	@Nullable String title,
	@Nullable String content
) {
}
