package example.unit_test.web.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UploadPostRequest(
	@NotNull Long writerId,
	@NotBlank String title,
	@NotBlank String content
) {
}
