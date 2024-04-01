package example.unit_test.domain.post.dto;

import jakarta.validation.constraints.NotNull;

public record DeletePostCommand(
	@NotNull Long requestUserId,
	@NotNull Long postId
) {
}
