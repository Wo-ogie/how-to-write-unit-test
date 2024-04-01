package example.unit_test.web.post.dto;

import example.unit_test.domain.post.model.Post;

public record UploadPostResponse(
	Long postId,
	Long writerId,
	String title,
	String content
) {
	public static UploadPostResponse from(Post post) {
		return new UploadPostResponse(
			post.getId(),
			post.getWriterId(),
			post.getTitle(),
			post.getContent()
		);
	}
}
