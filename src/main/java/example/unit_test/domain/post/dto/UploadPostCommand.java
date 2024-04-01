package example.unit_test.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UploadPostCommand(
	@NotNull Long writerId,
	@NotBlank String title,
	@NotBlank String content
) {
}
